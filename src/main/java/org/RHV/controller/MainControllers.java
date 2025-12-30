package org.RHV.controller;

import org.RHV.repository.FileCustomerRepository;
import org.RHV.repository.FileInvoiceRepository;
import org.RHV.service.CustomerService;
import org.RHV.service.InvoiceService;

import java.util.logging.Logger;

public class MainControllers {

    private static final Logger logger = Logger.getLogger(MainControllers.class.getName());

    public static final CustomerController customerController;
    public static final InvoiceController invoiceController;

    private static final String CUSTOMER_FILE = "data/customers.csv";
    private static final String INVOICE_FILE = "data/invoices.csv";

    static {
        logger.info("Initializing repositories and controllers...");

        // Repositories
        FileCustomerRepository customerRepo = new FileCustomerRepository(CUSTOMER_FILE);
        FileInvoiceRepository invoiceRepo = new FileInvoiceRepository(INVOICE_FILE);

        // Services
        CustomerService customerService = new CustomerService(customerRepo);
        InvoiceService invoiceService = new InvoiceService(invoiceRepo);

        // Controllers
        customerController = new CustomerController(customerService);
        invoiceController = new InvoiceController(invoiceService);

        logger.info("MainControllers initialized successfully.");
    }

    private MainControllers() {
        // Evitar instanciación
    }
}
