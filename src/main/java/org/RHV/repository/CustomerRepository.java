package org.RHV.repository;

import org.RHV.model.Customer;
import org.RHV.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio encargado de gestionar todas las operaciones relacionadas
 * con la tabla 'customers' en la base de datos MySQL.
 *
 * Esta clase sigue el patrón Repository:
 *  - Aísla la lógica de acceso a datos
 *  - Facilita mantenimiento y pruebas
 *  - Evita repetir código SQL en controladores o servicios
 */
public class CustomerRepository {

    /**
     * Guarda un nuevo cliente en la base de datos.
     *
     * @param customer Objeto Customer con los datos ingresados desde la UI.
     *
     * Flujo:
     *  1. Crear sentencia SQL parametrizada.
     *  2. Abrir conexión a MySQL.
     *  3. Asignar valores a los parámetros.
     *  4. Ejecutar el INSERT.
     */
    public void save(Customer customer) {

        // Sentencia SQL para insertar un nuevo cliente
        String sql = "INSERT INTO customers (name, address, email, phone) VALUES (?, ?, ?, ?)";

        // try-with-resources: cierra automáticamente conexión y statement
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Asignar valores a los parámetros del INSERT
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getAddress());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPhone());

            // Ejecutar la inserción
            stmt.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error saving customer: " + e.getMessage());
        }
    }

    /**
     * Obtiene todos los clientes almacenados en la base de datos.
     *
     * @return Lista de objetos Customer.
     *
     * Flujo:
     *  1. Ejecutar SELECT * FROM customers
     *  2. Recorrer el ResultSet
     *  3. Crear objetos Customer por cada fila
     *  4. Agregarlos a una lista
     */
    public List<Customer> getAll() {

        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Recorrer cada fila del resultado
            while (rs.next()) {

                // Crear objeto Customer con los datos de la fila
                Customer c = new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("email"),
                        rs.getString("phone")
                );

                list.add(c);
            }

        } catch (Exception e) {
            System.out.println("Error loading customers: " + e.getMessage());
        }

        return list;
    }
}
