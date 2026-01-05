package org.RHV.repository;

import org.RHV.database.DatabaseConnection;
import org.RHV.model.Consumption;

import java.sql.*;

/**
 * Repositorio encargado de realizar operaciones relacionadas con la tabla
 * 'consumptions' en la base de datos MySQL.
 *
 * Su responsabilidad principal es guardar consumos eléctricos y devolver
 * el ID generado automáticamente por MySQL.
 */
public class ConsumptionRepository {

    /**
     * Guarda un consumo en la base de datos y devuelve el ID generado.
     *
     * @param c Objeto Consumption que contiene kWh y fechas del periodo.
     * @return ID autogenerado por MySQL o -1 si ocurre un error.
     *
     * Flujo:
     *  1. Crear sentencia SQL con parámetros.
     *  2. Abrir conexión a MySQL.
     *  3. Ejecutar INSERT.
     *  4. Obtener el ID generado (PRIMARY KEY AUTO_INCREMENT).
     *  5. Devolver el ID para asignarlo al objeto Consumption.
     */
    public int save(Consumption c) {

        // Sentencia SQL para insertar un nuevo consumo
        String sql = "INSERT INTO consumptions (kwh, start_date, end_date) VALUES (?, ?, ?)";

        // try-with-resources: cierra automáticamente la conexión y el statement
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Asignar valores a los parámetros del INSERT
            stmt.setDouble(1, c.getKWh());
            stmt.setDate(2, Date.valueOf(c.getStartDate()));
            stmt.setDate(3, Date.valueOf(c.getEndDate()));

            // Ejecutar la inserción
            stmt.executeUpdate();

            // Obtener el ID generado por MySQL
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next())
                return rs.getInt(1); // Devuelve el ID autogenerado

        } catch (Exception e) {
            // Imprimir error en consola para depuración
            e.printStackTrace();
        }

        // Si algo falla, devolver -1
        return -1;
    }
}
