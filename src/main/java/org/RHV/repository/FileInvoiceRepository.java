package org.RHV.repository;

import org.RHV.model.Consumption;
import org.RHV.model.Customer;
import org.RHV.model.Invoice;

import java.io.*;
import java.time.LocalDate;
import java.util.logging.Logger;

/*
 * Repositorio basado en archivo CSV para almacenar facturas.
 * Formato:
 * invoiceId,customerName,customerAddress,customerEmail,customerPhone,kWh,startDate,endDate,rate,total
 */
public class FileInvoiceRepository extends InvoiceRepository {

    private final File file;
    private static final Logger logger = Logger.getLogger(FileInvoiceRepository.class.getName());

    public FileInvoiceRepository(String filename) {

        File folder = new File("data");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        this.file = new File(filename);

        if (!file.exists() || file.length() == 0) {
            writeHeader();
        }

        loadFromFile();
    }

    private void writeHeader() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("invoiceId,customerName,customerAddress,customerEmail,customerPhone,kWh,startDate,endDate,rate,total");
            bw.newLine();
        } catch (IOException e) {
            logger.severe("Error writing invoice CSV header: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");

                if (parts.length != 10) {
                    logger.warning("Skipping corrupt invoice line: " + line);
                    continue;
                }

                try {
                    int invoiceId = Integer.parseInt(parts[0]);

                    if (super.findById(invoiceId) != null) continue;

                    String name = parts[1];
                    String address = parts[2];
                    String email = parts[3];
                    String phone = parts[4];
                    double kWh = Double.parseDouble(parts[5]);
                    LocalDate start = LocalDate.parse(parts[6]);
                    LocalDate end = LocalDate.parse(parts[7]);
                    double rate = Double.parseDouble(parts[8]);
                    double total = Double.parseDouble(parts[9]);

                    Customer customer = new Customer(name, address, email, phone);
                    Consumption consumption = new Consumption(kWh, start, end);

                    Invoice invoice = new Invoice(invoiceId, customer, consumption, rate, total);

                    super.saveInvoice(invoice);

                } catch (Exception e) {
                    logger.warning("Skipping invalid invoice line: " + line);
                }
            }

        } catch (IOException e) {
            logger.severe("Error loading invoices: " + e.getMessage());
        }
    }

    private void appendInvoiceToFile(Invoice inv) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {

            String line = inv.getInvoiceId() + "," +
                    inv.getCustomer().getName() + "," +
                    inv.getCustomer().getAddress() + "," +
                    inv.getCustomer().getEmail() + "," +
                    inv.getCustomer().getPhone() + "," +
                    inv.getConsumption().getKWh() + "," +
                    inv.getConsumption().getStartDate() + "," +
                    inv.getConsumption().getEndDate() + "," +
                    inv.getAppliedRate() + "," +
                    inv.getTotalToPay();

            bw.write(line);
            bw.newLine();

        } catch (IOException e) {
            logger.severe("Error appending invoice: " + e.getMessage());
        }
    }

    @Override
    public void saveInvoice(Invoice invoice) {
        super.saveInvoice(invoice);
        appendInvoiceToFile(invoice);
    }
}
