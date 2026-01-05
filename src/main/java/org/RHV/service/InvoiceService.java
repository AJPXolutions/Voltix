package org.RHV.service;

import org.RHV.model.Consumption;
import org.RHV.model.Customer;
import org.RHV.model.Invoice;
import org.RHV.repository.ConsumptionRepository;
import org.RHV.repository.InvoiceRepository;
import org.RHV.util.TariffCalculator;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

/**
 * Servicio encargado de la lógica de negocio relacionada con facturas.
 * Aquí se coordinan:
 *  - Validaciones de datos
 *  - Cálculo de tarifas y totales
 *  - Persistencia de consumo y factura en MySQL
 *
 * Este servicio actúa como intermediario entre los controladores y los repositorios.
 */
public class InvoiceService {

    private static final Logger logger = Logger.getLogger(InvoiceService.class.getName());

    // Repositorios que interactúan con la base de datos
    private final InvoiceRepository invoiceRepository;
    private final ConsumptionRepository consumptionRepository;

    /**
     * Constructor usado por controladores JavaFX.
     * Crea repositorios por defecto.
     */
    public InvoiceService() {
        this.invoiceRepository = new InvoiceRepository();
        this.consumptionRepository = new ConsumptionRepository();
    }

    /**
     * Genera una factura completa a partir de un cliente y un consumo.
     * El flujo es:
     *  1. Validar datos
     *  2. Guardar consumo en MySQL
     *  3. Calcular tarifa y total
     *  4. Crear factura
     *  5. Guardar factura en MySQL
     *
     * @param customer    Cliente asociado a la factura
     * @param consumption Consumo eléctrico del periodo
     * @return Factura generada con ID asignado por MySQL
     */
    public Invoice generateInvoice(Customer customer, Consumption consumption) {

        logger.info("Generating invoice for customer: " + customer.getName());

        // --- VALIDACIONES ---
        if (consumption.getKWh() <= 0)
            throw new IllegalArgumentException("kWh must be greater than zero.");

        if (consumption.getEndDate().isBefore(consumption.getStartDate()))
            throw new IllegalArgumentException("End date cannot be before start date.");

        // --- GUARDAR CONSUMO ---
        // Inserta el consumo en MySQL y obtiene el ID generado
        int consumptionId = consumptionRepository.save(consumption);
        consumption.setId(consumptionId);

        // --- CÁLCULOS ---
        double rate = TariffCalculator.getRate(consumption.getKWh());
        double total = TariffCalculator.calculateTotal(consumption.getKWh());

        // --- CREAR FACTURA ---
        Invoice invoice = new Invoice(customer, consumption, rate, total);

        // --- GUARDAR FACTURA ---
        invoiceRepository.saveInvoice(invoice);

        logger.info("Invoice generated successfully with ID: " + invoice.getInvoiceId());

        return invoice;
    }

    /**
     * Sobrecarga para generar factura usando parámetros individuales.
     * Facilita el uso desde controladores JavaFX.
     */
    public Invoice generateInvoice(Customer customer, double kwh, LocalDate startDate, LocalDate endDate) {
        Consumption consumption = new Consumption(kwh, startDate, endDate);
        return generateInvoice(customer, consumption);
    }

    /**
     * Recupera todas las facturas almacenadas en la base de datos.
     *
     * @return Lista completa de facturas con cliente y consumo asociados.
     */
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.listInvoices();
    }
}
