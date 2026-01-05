package org.RHV.model;

/**
 * Representa un cliente dentro del sistema Voltix.
 * Contiene información básica como nombre, dirección, email y teléfono.
 *
 * Esta clase es utilizada tanto por la UI como por los servicios y repositorios
 * para gestionar clientes en memoria y en la base de datos.
 */
public class Customer {

    // Contador interno usado cuando los clientes se creaban desde CSV.
    // Actualmente no se usa para MySQL, pero se mantiene por compatibilidad.
    private static int counter = 1;

    // Identificador único del cliente.
    // Cuando se usa MySQL, este ID es asignado por la base de datos.
    private int id;

    // Información básica del cliente
    private String name;
    private String address;
    private String email;
    private String phone;

    /**
     * Constructor usado cuando se crea un nuevo cliente desde la UI.
     * Antes de usar MySQL, el ID se generaba automáticamente con un contador.
     * Ahora, el ID real será asignado por la base de datos mediante setId().
     */
    public Customer(String name, String address, String email, String phone) {
        this.id = counter++; // Valor temporal hasta que MySQL asigne el ID real
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
    }

    /**
     * Constructor usado cuando se cargan clientes desde un archivo CSV
     * o desde una fuente externa donde el ID ya existe.
     */
    public Customer(int id, String name, String address, String email, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
    }

    // Getters estándar
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    /**
     * Permite actualizar el contador cuando se cargan clientes desde CSV.
     * Ya no se usa con MySQL, pero se mantiene por compatibilidad.
     */
    public static void setCounter(int value) {
        counter = value;
    }

    /**
     * Setter necesario para asignar el ID generado por MySQL
     * después de insertar el cliente en la base de datos.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Representación amigable del cliente.
     * Se usa automáticamente en ComboBox y listas de JavaFX.
     */
    @Override
    public String toString() {
        return name; // También podría ser: name + " (" + email + ")"
    }
}
