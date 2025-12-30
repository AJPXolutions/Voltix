package org.RHV.service;

import org.RHV.model.Customer;
import org.RHV.repository.CustomerRepository;

import java.util.List;
import java.util.logging.Logger;

/*
 * Servicio encargado de la lógica de negocio relacionada con clientes.
 * Valida datos, delega en el repositorio y mantiene la aplicación consistente.
 */
public class CustomerService {

    private static final Logger logger = Logger.getLogger(CustomerService.class.getName());
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void registerCustomer(Customer customer) {

        logger.info("Registering new customer: " + customer.getName());

        // Validaciones
        if (customer.getName().isBlank()) {
            logger.warning("Customer name is empty");
            throw new IllegalArgumentException("Name cannot be empty.");
        }

        if (customer.getAddress().isBlank()) {
            logger.warning("Customer address is empty");
            throw new IllegalArgumentException("Address cannot be empty.");
        }

        if (customer.getEmail().isBlank()) {
            logger.warning("Customer email is empty");
            throw new IllegalArgumentException("Email cannot be empty.");
        }

        if (customer.getPhone().isBlank()) {
            logger.warning("Customer phone is empty");
            throw new IllegalArgumentException("Phone cannot be empty.");
        }

        // Guardar cliente
        customerRepository.saveCustomer(customer);
        logger.info("Customer registered successfully with ID: " + customer.getId());
    }

    public Customer getCustomerById(int id) {
        logger.info("Searching for customer with ID: " + id);
        return customerRepository.findById(id);
    }

    public List<Customer> getAllCustomers() {
        logger.info("Listing all customers");
        return customerRepository.listCustomers();
    }
}
