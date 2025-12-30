package org.RHV.repository;

import org.RHV.model.Invoice;

import java.util.ArrayList;
import java.util.List;

/*
 * Repositorio en memoria para almacenar facturas.
 * Funciona como una base de datos simple.
 * Permite: guardar, buscar por ID y listar todas las facturas.
 */
public class InvoiceRepository {

    private final List<Invoice> invoices = new ArrayList<>();

    /**
     * Guarda una factura en la lista.
     */
    public void saveInvoice(Invoice invoice) {
        invoices.add(invoice);
    }

    /**
     * Busca una factura por su ID.
     */
    public Invoice findById(int invoiceId) {
        for (Invoice invoice : invoices) {
            if (invoice.getInvoiceId() == invoiceId) {
                return invoice;
            }
        }
        return null;
    }

    /**
     * Devuelve la lista completa de facturas.
     */
    public List<Invoice> listInvoices() {
        return invoices;
    }
}
