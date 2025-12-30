package org.RHV.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.RHV.model.Invoice;

import java.util.logging.Logger;

public class ListInvoicesController {

    @FXML private TableView<Invoice> tableInvoices;

    @FXML private TableColumn<Invoice, String> colCustomer;
    @FXML private TableColumn<Invoice, String> colPeriod;
    @FXML private TableColumn<Invoice, Double> colKwh;
    @FXML private TableColumn<Invoice, Double> colTotal;

    private InvoiceController invoiceController;

    private static final Logger logger = Logger.getLogger(ListInvoicesController.class.getName());

    @FXML
    public void initialize() {

        invoiceController = MainControllers.invoiceController;

        logger.info("Initializing ListInvoicesController");

        // Nombre del cliente
        colCustomer.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
                        cell.getValue().getCustomer().getName()
                )
        );

        // Periodo de consumo
        colPeriod.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
                        cell.getValue().getConsumption().getStartDate()
                                + " → " +
                                cell.getValue().getConsumption().getEndDate()
                )
        );

        // kWh consumidos
        colKwh.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleDoubleProperty(
                        cell.getValue().getConsumption().getKWh()
                ).asObject()
        );

        // Total a pagar
        colTotal.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleDoubleProperty(
                        cell.getValue().getTotalToPay()
                ).asObject()
        );

        loadInvoices();
    }

    private void loadInvoices() {
        var invoices = invoiceController.getAllInvoices();

        if (invoices.isEmpty()) {
            logger.info("No invoices found to display");
        } else {
            logger.info("Loaded " + invoices.size() + " invoices");
        }

        tableInvoices.setItems(FXCollections.observableArrayList(invoices));
    }
}
