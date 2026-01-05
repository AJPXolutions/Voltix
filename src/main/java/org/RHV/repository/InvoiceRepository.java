package org.RHV.repository;

import org.RHV.database.DatabaseConnection;
import org.RHV.model.Consumption;
import org.RHV.model.Customer;
import org.RHV.model.Invoice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio encargado de gestionar todas las operaciones relacionadas
 * con la tabla 'invoices' en la base de datos MySQL.
 *
 * Esta clase sigue el patrón Repository:
 *  - Aísla la lógica de acceso a datos
 *  - Facilita mantenimiento y pruebas
 *  - Evita repetir código SQL en controladores o servicios
 */
public class InvoiceRepository {

    /**
     * Guarda una factura en la base de datos y asigna el ID generado por MySQL.
     *
     * @param invoice Objeto Invoice que contiene cliente, consumo, tarifa y total.
     *
     * Flujo:
     *  1. Crear sentencia SQL parametrizada.
     *  2. Abrir conexión a MySQL.
     *  3. Asignar valores a los parámetros.
     *  4. Ejecutar el INSERT.
     *  5. Obtener el ID autogenerado (PRIMARY KEY AUTO_INCREMENT).
     *  6. Asignarlo al objeto Invoice.
     */
    public void saveInvoice(Invoice invoice) {

        String sql = "INSERT INTO invoices (customer_id, consumption_id, rate, total) VALUES (?, ?, ?, ?)";

        // try-with-resources: cierra automáticamente conexión y statement
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Asignar valores a los parámetros del INSERT
            stmt.setInt(1, invoice.getCustomer().getId());
            stmt.setInt(2, invoice.getConsumption().getId());
            stmt.setDouble(3, invoice.getAppliedRate());
            stmt.setDouble(4, invoice.getTotalToPay());

            // Ejecutar la inserción
            stmt.executeUpdate();

            // Obtener el ID generado por MySQL
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                invoice.setInvoiceId(rs.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Recupera todas las facturas almacenadas en la base de datos,
     * incluyendo sus clientes y consumos asociados mediante JOIN.
     *
     * @return Lista de objetos Invoice completamente construidos.
     *
     * Flujo:
     *  1. Ejecutar SELECT con JOIN a customers y consumptions.
     *  2. Recorrer el ResultSet.
     *  3. Construir Customer, Consumption e Invoice por cada fila.
     *  4. Agregar cada factura a la lista final.
     */
    public List<Invoice> listInvoices() {

        List<Invoice> list = new ArrayList<>();

        // Consulta SQL con JOIN para obtener toda la información relacionada
        String sql = """
            SELECT i.id AS invoice_id,
                   c.id AS customer_id, c.name, c.address, c.email, c.phone,
                   con.id AS consumption_id, con.kwh, con.start_date, con.end_date,
                   i.rate, i.total
            FROM invoices i
            JOIN customers c ON i.customer_id = c.id
            JOIN consumptions con ON i.consumption_id = con.id
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Recorrer cada fila del resultado
            while (rs.next()) {

                // Construir Customer con los datos del JOIN
                Customer customer = new Customer(
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
                customer.setId(rs.getInt("customer_id"));

                // Construir Consumption con los datos del JOIN
                Consumption consumption = new Consumption(
                        rs.getDouble("kwh"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate()
                );
                consumption.setId(rs.getInt("consumption_id"));

                // Construir Invoice con todos los datos
                Invoice invoice = new Invoice(
                        rs.getInt("invoice_id"),
                        customer,
                        consumption,
                        rs.getDouble("rate"),
                        rs.getDouble("total")
                );

                list.add(invoice);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
