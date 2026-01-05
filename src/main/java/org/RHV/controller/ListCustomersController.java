package org.RHV.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.RHV.model.Customer;
import org.RHV.repository.CustomerRepository;

/**
 * Controlador encargado de mostrar la lista de clientes en la UI.
 * Se conecta al CustomerRepository para obtener los datos desde MySQL
 * y los carga en una TableView.
 */
public class ListCustomersController {

    // Tabla principal donde se mostrarán los clientes
    @FXML private TableView<Customer> tableCustomers;

    // Columnas de la tabla, cada una vinculada a un atributo del modelo Customer
    @FXML private TableColumn<Customer, Integer> colId;
    @FXML private TableColumn<Customer, String> colName;
    @FXML private TableColumn<Customer, String> colAddress;
    @FXML private TableColumn<Customer, String> colEmail;
    @FXML private TableColumn<Customer, String> colPhone;

    // Repositorio encargado de obtener los clientes desde MySQL
    private final CustomerRepository repo = new CustomerRepository();

    /**
     * Método llamado automáticamente por JavaFX al cargar el FXML.
     * Configura las columnas y carga los datos desde la base de datos.
     */
    public void initialize() {

        // Ajusta automáticamente el tamaño de las columnas para ocupar todo el ancho
        tableCustomers.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Vincula cada columna con el atributo correspondiente del modelo Customer
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        // Cargar los clientes desde MySQL y mostrarlos en la tabla
        tableCustomers.getItems().setAll(repo.getAll());
    }
}
