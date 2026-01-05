package org.RHV.controller;

import org.RHV.service.CustomerService;
import org.RHV.service.InvoiceService;

import java.util.logging.Logger;

/**
 * Clase centralizadora que inicializa y expone instancias únicas (singleton)
 * de los controladores principales de la aplicación.
 *
 * Su propósito es permitir que cualquier pantalla o controlador de JavaFX
 * pueda acceder a los mismos servicios y controladores sin duplicarlos.
 *
 * Esta clase NO se instancia. Todo se maneja mediante un bloque estático.
 */
public class MainControllers {

    private static final Logger logger = Logger.getLogger(MainControllers.class.getName());

    // Controladores accesibles globalmente en la aplicación
    public static final CustomerController customerController;
    public static final InvoiceController invoiceController;

    /**
     * Bloque estático ejecutado una sola vez cuando la clase se carga.
     * Aquí se inicializan los servicios y controladores compartidos.
     */
    static {
        logger.info("Initializing services and controllers...");

        // Crear servicios conectados a MySQL
        CustomerService customerService = new CustomerService();
        InvoiceService invoiceService = new InvoiceService();

        // Crear controladores usando los servicios creados
        customerController = new CustomerController(customerService);
        invoiceController = new InvoiceController(invoiceService);

        logger.info("MainControllers initialized successfully.");
    }

    /**
     * Constructor privado para evitar que alguien intente instanciar esta clase.
     * MainControllers funciona únicamente como contenedor estático.
     */
    private MainControllers() {
        // Evitar instanciación
    }
}
