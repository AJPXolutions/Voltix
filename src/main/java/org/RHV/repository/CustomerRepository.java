package org.RHV.repository;

import org.RHV.model.Customer;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {

    private final List<Customer> customers = new ArrayList<>();

    /**
     * Guarda un cliente en la lista.
     * Evita duplicados por ID.
     */
    public void saveCustomer(Customer customer) {

        // Si ya existe un cliente con ese ID, lo reemplazamos
        Customer existing = findById(customer.getId());
        if (existing != null) {
            customers.remove(existing);
        }

        customers.add(customer);
    }

    /**
     * Busca un cliente por su ID.
     */
    public Customer findById(int id) {
        for (Customer c : customers) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    /**
     * Devuelve la lista completa de clientes.
     */
    public List<Customer> listCustomers() {
        return customers;
    }
}
