package org.RHV.controller;

import org.RHV.model.Consumption;
import org.RHV.model.Customer;
import org.RHV.model.Invoice;
import org.RHV.service.InvoiceService;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

/**
 * Controlador intermedio entre la UI y el servicio de facturación.
 * Su responsabilidad es recibir datos desde la interfaz gráfica,
 * delegar la lógica al InvoiceService y devolver resultados a la UI.
 */
public class InvoiceController {

    private static final Logger logger = Logger.getLogger(InvoiceController.class.getName());

    // Servicio encargado de la lógica de negocio de facturas
    private InvoiceService invoiceService;

    /**
     * Constructor vacío requerido por JavaFX.
     * JavaFX crea controladores mediante reflexión, por lo que
     * siempre necesita un constructor sin parámetros.
     */
    public InvoiceController() {
        this.invoiceService = new InvoiceService();
        logger.info("InvoiceController initialized");
    }

    /**
     * Constructor alternativo usado por MainControllers para inyección manual.
     * Permite pasar un InvoiceService ya configurado.
     */
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
        logger.info("InvoiceController initialized with injected service");
    }

    /**
     * Crea una factura usando los datos recibidos desde la UI.
     *
     * @param customer  Cliente seleccionado en la interfaz
     * @param kwh       Consumo en kWh ingresado por el usuario
     * @param startDate Fecha de inicio del periodo de consumo
     * @param endDate   Fecha de fin del periodo de consumo
     *
     * Este método:
     * 1. Valida que el cliente no sea nulo
     * 2. Crea un objeto Consumption con los datos ingresados
     * 3. Llama al servicio para generar y guardar la factura
     * 4. Registra en logs el ID generado por MySQL
     */
    public void createInvoice(Customer customer, double kwh, LocalDate startDate, LocalDate endDate) {

        // Validación básica: el cliente es obligatorio
        if (customer == null) {
            logger.warning("Cannot create invoice: customer is null");
            throw new IllegalArgumentException("Customer cannot be null.");
        }

        // Crear el consumo asociado a la factura
        Consumption consumption = new Consumption(kwh, startDate, endDate);

        // Delegar la creación de la factura al servicio
        Invoice invoice = invoiceService.generateInvoice(customer, consumption);

        // Confirmación en logs
        logger.info("Invoice created successfully with ID: " + invoice.getInvoiceId());
    }

    /**
     * Devuelve todas las facturas almacenadas en MySQL.
     * Este método es usado por ListInvoicesController para llenar la tabla.
     */
    public List<Invoice> getAllInvoices() {
        logger.info("Request to list all invoices");
        return invoiceService.getAllInvoices();
    }
}
