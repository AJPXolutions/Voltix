package org.RHV.service;

import org.RHV.model.Consumption;
import org.RHV.model.Customer;
import org.RHV.model.Invoice;
import org.RHV.repository.InvoiceRepository;
import org.RHV.util.TariffCalculator;

import java.util.List;
import java.util.logging.Logger;

/*
 * Servicio encargado de generar facturas y manejar toda la lógica relacionada:
 * - Validar consumo
 * - Calcular tarifa
 * - Calcular total
 * - Crear factura
 * - Guardarla en el repositorio
 * - Buscar y listar facturas
 */
public class InvoiceService {

    private static final Logger logger = Logger.getLogger(InvoiceService.class.getName());
    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * Genera una nueva factura con ID automático.
     */
    public Invoice generateInvoice(Customer customer, Consumption consumption) {

        logger.info("Generating invoice for customer: " + customer.getName());

        // Validaciones
        if (consumption.getKWh() <= 0) {
            logger.warning("Invalid kWh value: " + consumption.getKWh());
            throw new IllegalArgumentException("kWh must be greater than zero.");
        }

        if (consumption.getEndDate().isBefore(consumption.getStartDate())) {
            logger.warning("Invalid date range for invoice");
            throw new IllegalArgumentException("End date cannot be before start date.");
        }

        // Cálculos
        double rate = TariffCalculator.getRate(consumption.getKWh());
        double total = TariffCalculator.calculateTotal(consumption.getKWh());

        // Crear factura (ID autogenerado)
        Invoice invoice = new Invoice(customer, consumption, rate, total);

        // Guardar
        invoiceRepository.saveInvoice(invoice);

        logger.info("Invoice generated successfully with ID: " + invoice.getInvoiceId());

        return invoice;
    }

    public Invoice getInvoiceById(int id) {
        logger.info("Searching for invoice with ID: " + id);
        return invoiceRepository.findById(id);
    }

    public List<Invoice> getAllInvoices() {
        logger.info("Listing all invoices");
        return invoiceRepository.listInvoices();
    }
}
