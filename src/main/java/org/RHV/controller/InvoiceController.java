package org.RHV.controller;

import org.RHV.model.Consumption;
import org.RHV.model.Customer;
import org.RHV.model.Invoice;
import org.RHV.service.InvoiceService;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

/*
 * Controlador que actúa como puente entre la UI y el servicio de facturas.
 * Permite crear, buscar y listar facturas.
 */
public class InvoiceController {

    private static final Logger logger = Logger.getLogger(InvoiceController.class.getName());
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
        logger.info("InvoiceController initialized");
    }

    /**
     * Crea una factura para un cliente existente.
     * El ID se genera automáticamente en la clase Invoice.
     */
    public void createInvoice(int customerId, double kwh, LocalDate startDate, LocalDate endDate) {

        Customer customer = MainControllers.customerController.getCustomer(customerId);

        if (customer == null) {
            logger.warning("Cannot create invoice: customer not found with ID: " + customerId);
            throw new IllegalArgumentException("Customer not found.");
        }

        Consumption consumption = new Consumption(kwh, startDate, endDate);

        Invoice invoice = invoiceService.generateInvoice(customer, consumption);

        logger.info("Invoice created successfully with ID: " + invoice.getInvoiceId());
    }

    public Invoice getInvoice(int id) {
        logger.info("Request to get invoice with ID: " + id);
        Invoice invoice = invoiceService.getInvoiceById(id);

        if (invoice == null) {
            logger.warning("Invoice not found with ID: " + id);
        } else {
            logger.info("Invoice found: " + id);
        }

        return invoice;
    }

    public List<Invoice> getAllInvoices() {
        logger.info("Request to list all invoices");
        return invoiceService.getAllInvoices();
    }
}
