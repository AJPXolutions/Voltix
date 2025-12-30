package org.RHV.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.RHV.model.Customer;

public class RegisterCustomerController {

    @FXML private TextField txtName;
    @FXML private TextField txtAddress;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;

    @FXML private Button btnSave;

    private CustomerController customerController;

    @FXML
    public void initialize() {

        // Inicializar el controlador principal
        customerController = MainControllers.customerController;

        btnSave.setOnAction(e -> saveCustomer());
    }

    private void saveCustomer() {

        if (customerController == null) {
            System.out.println("CustomerController is null");
            return;
        }

        Customer customer = new Customer(
                txtName.getText(),
                txtAddress.getText(),
                txtEmail.getText(),
                txtPhone.getText()
        );

        customerController.addCustomer(customer);

        txtName.clear();
        txtAddress.clear();
        txtEmail.clear();
        txtPhone.clear();
    }
}
