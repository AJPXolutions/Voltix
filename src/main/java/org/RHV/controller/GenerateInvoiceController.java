package org.RHV.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.RHV.model.Customer;
import org.RHV.service.CustomerService;
import org.RHV.service.InvoiceService;

/**
 * Controlador encargado de la pantalla de generación de facturas.
 * Conecta la UI con los servicios.
 */
public class GenerateInvoiceController {

    @FXML private ComboBox<Customer> comboCustomers;
    @FXML private TextField txtKwh;
    @FXML private DatePicker dateIssue;
    @FXML private DatePicker dateDue;
    @FXML private Button btnGenerate;

    private CustomerService customerService;
    private InvoiceService invoiceService;

    @FXML
    public void initialize() {

        // Inicializar servicios
        customerService = new CustomerService();
        invoiceService = new InvoiceService();

        // Cargar clientes desde MySQL
        comboCustomers.getItems().addAll(customerService.getAllCustomers());

        // Mostrar solo el nombre del cliente en el ComboBox
        comboCustomers.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Customer customer, boolean empty) {
                super.updateItem(customer, empty);
                setText(empty || customer == null ? null : customer.getName());
            }
        });

        comboCustomers.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Customer customer, boolean empty) {
                super.updateItem(customer, empty);
                setText(empty || customer == null ? null : customer.getName());
            }
        });

        // Acción del botón
        btnGenerate.setOnAction(e -> generateInvoice());
    }

    /**
     * Lógica para generar una factura desde la UI.
     */
    private void generateInvoice() {
        try {
            Customer customer = comboCustomers.getValue();
            String kwhText = txtKwh.getText();
            var startDate = dateIssue.getValue();
            var endDate = dateDue.getValue();

            // Validaciones básicas
            if (customer == null || kwhText.isBlank() || startDate == null || endDate == null) {
                showAlert("All fields are required");
                return;
            }

            double kwh = Double.parseDouble(kwhText);

            // Generar factura
            invoiceService.generateInvoice(customer, kwh, startDate, endDate);

            showAlert("Invoice generated successfully");

            // Limpiar formulario
            txtKwh.clear();
            dateIssue.setValue(null);
            dateDue.setValue(null);
            comboCustomers.getSelectionModel().clearSelection();

        } catch (NumberFormatException ex) {
            showAlert("kWh must be a valid number");
        } catch (Exception ex) {
            showAlert("Error: " + ex.getMessage());
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
