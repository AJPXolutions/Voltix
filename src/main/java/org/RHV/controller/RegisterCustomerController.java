package org.RHV.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.RHV.model.Customer;

/**
 * Controlador encargado del formulario de registro de clientes.
 * Recibe los datos ingresados por el usuario, crea un objeto Customer
 * y lo envía al CustomerController para guardarlo en MySQL.
 */
public class RegisterCustomerController {

    // Campos de texto del formulario
    @FXML private TextField txtName;
    @FXML private TextField txtAddress;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;

    // Botón para guardar el cliente
    @FXML private Button btnSave;

    // Controlador principal que maneja la lógica de clientes
    private CustomerController customerController;

    /**
     * Método llamado automáticamente por JavaFX al cargar el FXML.
     * Inicializa el controlador principal y configura la acción del botón.
     */
    @FXML
    public void initialize() {

        // Obtener el controlador global compartido
        customerController = MainControllers.customerController;

        // Acción del botón: guardar cliente
        btnSave.setOnAction(e -> saveCustomer());
    }

    /**
     * Crea un nuevo cliente usando los datos ingresados en el formulario
     * y lo envía al CustomerController para guardarlo en la base de datos.
     * Luego limpia los campos del formulario.
     */
    private void saveCustomer() {

        // Validación básica: asegurar que el controlador existe
        if (customerController == null) {
            System.out.println("CustomerController is null");
            return;
        }

        // Crear objeto Customer con los datos ingresados
        Customer customer = new Customer(
                txtName.getText(),
                txtAddress.getText(),
                txtEmail.getText(),
                txtPhone.getText()
        );

        // Guardar el cliente mediante el controlador principal
        customerController.addCustomer(customer);

        // Limpiar los campos del formulario
        txtName.clear();
        txtAddress.clear();
        txtEmail.clear();
        txtPhone.clear();
    }
}
