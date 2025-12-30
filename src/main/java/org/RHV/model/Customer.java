package org.RHV.model;

public class Customer {

    private static int counter = 1;

    private int id;
    private String name;
    private String address;
    private String email;
    private String phone;

    // Constructor para nuevos clientes (ID autogenerado)
    public Customer(String name, String address, String email, String phone) {
        this.id = counter++;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
    }

    // Constructor para clientes cargados desde CSV (ID restaurado)
    public Customer(int id, String name, String address, String email, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
    }

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

    public static void setCounter(int value) {
        counter = value;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name; // o name + " (" + email + ")";
    }
}
