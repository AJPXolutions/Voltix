package org.RHV.controller;

import org.RHV.model.Customer;
import org.RHV.service.CustomerService;

import java.util.List;
import java.util.logging.Logger;

/*
 * Controlador que actúa como puente entre la UI y el servicio.
 * Se encarga de recibir datos desde la interfaz y delegarlos al servicio.
 */
public class CustomerController {

    private static final Logger logger = Logger.getLogger(CustomerController.class.getName());
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
        logger.info("CustomerController initialized");
    }

    /**
     * Crea un nuevo cliente usando los datos del formulario.
     * El ID se genera automáticamente en la clase Customer.
     */
    public void createCustomer(String name, String address, String email, String phone) {

        logger.info("Creating new customer: " + name);

        Customer customer = new Customer(name, address, email, phone);

        customerService.registerCustomer(customer);

        logger.info("Customer created successfully with ID: " + customer.getId());
    }

    /**
     * Agrega un cliente ya construido (usado por controladores internos).
     */
    public void addCustomer(Customer customer) {
        logger.info("Adding customer: " + customer.getName());
        customerService.registerCustomer(customer);
        logger.info("Customer added successfully with ID: " + customer.getId());
    }

    /**
     * Busca un cliente por ID.
     */
    public Customer getCustomer(int id) {
        logger.info("Request to get customer with ID: " + id);
        Customer customer = customerService.getCustomerById(id);

        if (customer == null) {
            logger.warning("Customer not found with ID: " + id);
        } else {
            logger.info("Customer found: " + customer.getName());
        }

        return customer;
    }

    /**
     * Devuelve la lista completa de clientes.
     */
    public List<Customer> getAllCustomers() {
        logger.info("Request to list all customers");
        return customerService.getAllCustomers();
    }
}
