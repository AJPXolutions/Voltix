package org.RHV.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.RHV.model.Customer;

public class GenerateInvoiceController {

    @FXML private ComboBox<Customer> comboCustomers;
    @FXML private TextField txtKwh;
    @FXML private DatePicker dateIssue;
    @FXML private DatePicker dateDue;
    @FXML private Button btnGenerate;

    private CustomerController customerController;
    private InvoiceController invoiceController;

    @FXML
    public void initialize() {

        customerController = MainControllers.customerController;
        invoiceController = MainControllers.invoiceController;

        // Cargar clientes en el ComboBox
        comboCustomers.getItems().addAll(customerController.getAllCustomers());

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

        btnGenerate.setOnAction(e -> generateInvoice());
    }

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

            // Crear factura
            invoiceController.createInvoice(customer.getId(), kwh, startDate, endDate);

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
