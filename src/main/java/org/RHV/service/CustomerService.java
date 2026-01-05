package org.RHV.service;

import org.RHV.model.Customer;
import org.RHV.repository.CustomerRepository;

import java.util.List;
import java.util.logging.Logger;

/**
 * Servicio encargado de la lógica de negocio relacionada con clientes.
 *
 * Este servicio:
 *  - Valida los datos antes de guardar un cliente.
 *  - Delegar el acceso a la base de datos al CustomerRepository.
 *  - Proporciona métodos usados por controladores JavaFX.
 *
 * Sigue el patrón Service Layer, separando la lógica de negocio
 * del acceso a datos y de la interfaz gráfica.
 */
public class CustomerService {

    private static final Logger logger = Logger.getLogger(CustomerService.class.getName());

    // Repositorio encargado de interactuar con MySQL
    private final CustomerRepository customerRepository;

    /**
     * Constructor vacío requerido por JavaFX y controladores que
     * crean el servicio sin inyección manual.
     */
    public CustomerService() {
        this.customerRepository = new CustomerRepository();
    }

    /**
     * Constructor alternativo que permite inyección de dependencias.
     * Útil para pruebas unitarias o controladores personalizados.
     */
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Registra un nuevo cliente después de validar sus datos.
     *
     * @param customer Objeto Customer con los datos ingresados desde la UI.
     *
     * Flujo:
     *  1. Registrar en logs la operación.
     *  2. Validar campos obligatorios.
     *  3. Delegar el guardado al repositorio.
     *  4. Confirmar en logs la operación exitosa.
     */
    public void registerCustomer(Customer customer) {

        logger.info("Registering new customer: " + customer.getName());

        // Validaciones de campos obligatorios
        if (customer.getName().isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }

        if (customer.getAddress().isBlank()) {
            throw new IllegalArgumentException("Address cannot be empty.");
        }

        if (customer.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }

        if (customer.getPhone().isBlank()) {
            throw new IllegalArgumentException("Phone cannot be empty.");
        }

        // Guardar cliente en MySQL
        customerRepository.save(customer);

        logger.info("Customer registered successfully.");
    }

    /**
     * Devuelve todos los clientes almacenados en la base de datos.
     *
     * @return Lista de objetos Customer.
     */
    public List<Customer> getAllCustomers() {
        logger.info("Listing all customers");
        return customerRepository.getAll();
    }
}
