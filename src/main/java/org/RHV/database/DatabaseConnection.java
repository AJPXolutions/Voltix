package org.RHV.database;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Clase encargada de gestionar la conexión a la base de datos MySQL.
 * Proporciona un método estático para obtener conexiones reutilizables
 * desde cualquier parte del sistema (repositorios, servicios, etc.).
 *
 * Esta clase actúa como un "Database Provider" centralizado.
 */
public class DatabaseConnection {

    // URL de conexión a MySQL (base de datos 'voltix' en localhost)
    private static final String URL = "jdbc:mysql://localhost:3306/voltix";

    // Usuario y contraseña de MySQL
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    /**
     * Devuelve una conexión activa a la base de datos.
     * Si ocurre un error, se imprime el mensaje y se devuelve null.
     *
     * @return Connection activa o null si falla la conexión.
     */
    public static Connection getConnection() {
        try {
            // Intentar conectar usando DriverManager
            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (Exception e) {
            // Mostrar error en consola para depuración
            System.out.println("Error connecting to MySQL: " + e.getMessage());
            return null;
        }
    }
}
