package org.RHV.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.RHV.model.Invoice;

import java.util.logging.Logger;

/**
 * Controlador encargado de mostrar la lista de facturas en la interfaz gráfica.
 * Se conecta al InvoiceController para obtener los datos desde MySQL y los
 * muestra en una TableView con columnas personalizadas.
 */
public class ListInvoicesController {

    // Tabla principal donde se mostrarán las facturas
    @FXML private TableView<Invoice> tableInvoices;

    // Columnas que mostrarán información específica de cada factura
    @FXML private TableColumn<Invoice, String> colCustomer;
    @FXML private TableColumn<Invoice, String> colPeriod;
    @FXML private TableColumn<Invoice, Double> colKwh;
    @FXML private TableColumn<Invoice, Double> colTotal;

    // Controlador que provee acceso al servicio de facturación
    private InvoiceController invoiceController;

    private static final Logger logger = Logger.getLogger(ListInvoicesController.class.getName());

    /**
     * Método llamado automáticamente por JavaFX al cargar el FXML.
     * Configura las columnas de la tabla y carga las facturas desde MySQL.
     */
    @FXML
    public void initialize() {

        // Ajusta automáticamente el tamaño de las columnas para ocupar todo el ancho disponible
        tableInvoices.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Obtener el controlador principal que contiene el InvoiceController compartido
        invoiceController = MainControllers.invoiceController;

        logger.info("Initializing ListInvoicesController");

        // Configurar columna: nombre del cliente
        colCustomer.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
                        cell.getValue().getCustomer().getName()
                )
        );

        // Configurar columna: periodo de consumo (inicio → fin)
        colPeriod.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
                        cell.getValue().getConsumption().getStartDate()
                                + " → " +
                                cell.getValue().getConsumption().getEndDate()
                )
        );

        // Configurar columna: kWh consumidos
        colKwh.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleDoubleProperty(
                        cell.getValue().getConsumption().getKWh()
                ).asObject()
        );

        // Configurar columna: total a pagar
        colTotal.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleDoubleProperty(
                        cell.getValue().getTotalToPay()
                ).asObject()
        );

        // Cargar las facturas desde MySQL
        loadInvoices();
    }

    /**
     * Carga todas las facturas desde el servicio y las muestra en la tabla.
     * También registra información en logs para depuración.
     */
    private void loadInvoices() {
        var invoices = invoiceController.getAllInvoices();

        if (invoices.isEmpty()) {
            logger.info("No invoices found to display");
        } else {
            logger.info("Loaded " + invoices.size() + " invoices");
        }

        // Convertir la lista en un ObservableList y asignarla a la tabla
        tableInvoices.setItems(FXCollections.observableArrayList(invoices));
    }
}
