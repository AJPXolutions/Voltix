package org.RHV.model;

/*
 * Representa una factura generada para un cliente.
 * Contiene: cliente, consumo, tarifa aplicada y total a pagar.
 */
public class Invoice {

    private static int counter = 1;

    private final int invoiceId;
    private final Customer customer;
    private final Consumption consumption;
    private final double appliedRate;
    private final double totalToPay;

    /**
     * Constructor usado cuando el sistema genera una nueva factura.
     * El ID se asigna automáticamente.
     */
    public Invoice(Customer customer, Consumption consumption, double appliedRate, double totalToPay) {
        this.invoiceId = counter++;
        this.customer = customer;
        this.consumption = consumption;
        this.appliedRate = appliedRate;
        this.totalToPay = totalToPay;
    }

    /**
     * Constructor usado al cargar facturas desde archivo (CSV).
     * Permite restaurar el ID original.
     */
    public Invoice(int invoiceId, Customer customer, Consumption consumption, double appliedRate, double totalToPay) {
        this.invoiceId = invoiceId;
        this.customer = customer;
        this.consumption = consumption;
        this.appliedRate = appliedRate;
        this.totalToPay = totalToPay;

        // Mantener el contador sincronizado
        if (invoiceId >= counter) {
            counter = invoiceId + 1;
        }
    }

    public int getInvoiceId() {
        return invoiceId;
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

    @Override
    public String toString() {
        return "Invoice #" + invoiceId +
                " | Customer: " + customer.getName() +
                " | kWh: " + consumption.getKWh() +
                " | Total: $" + totalToPay;
    }
}
