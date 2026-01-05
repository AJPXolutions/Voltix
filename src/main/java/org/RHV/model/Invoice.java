package org.RHV.model;

/**
 * Representa una factura generada para un cliente.
 * Contiene: cliente, consumo asociado, tarifa aplicada y total a pagar.
 * El ID es generado automáticamente por MySQL.
 */
public class Invoice {

    // ID generado por MySQL
    private int invoiceId;

    // Cliente al que pertenece la factura
    private final Customer customer;

    // Consumo eléctrico asociado a esta factura
    private final Consumption consumption;

    // Tarifa aplicada según el consumo
    private final double appliedRate;

    // Total a pagar calculado
    private final double totalToPay;

    /**
     * Constructor usado cuando se genera una nueva factura desde la UI.
     * El ID será asignado después de guardar en MySQL.
     */
    public Invoice(Customer customer, Consumption consumption, double appliedRate, double totalToPay) {
        this.customer = customer;
        this.consumption = consumption;
        this.appliedRate = appliedRate;
        this.totalToPay = totalToPay;
    }

    /**
     * Constructor usado al cargar facturas desde MySQL.
     */
    public Invoice(int invoiceId, Customer customer, Consumption consumption, double appliedRate, double totalToPay) {
        this.invoiceId = invoiceId;
        this.customer = customer;
        this.consumption = consumption;
        this.appliedRate = appliedRate;
        this.totalToPay = totalToPay;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    // Setter necesario para asignar el ID generado por MySQL
    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Consumption getConsumption() {
        return consumption;
    }

    public double getAppliedRate() {
        return appliedRate;
    }

    public double getTotalToPay() {
        return totalToPay;
    }
}
