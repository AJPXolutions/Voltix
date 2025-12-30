package org.RHV.repository;

import org.RHV.model.Customer;

import java.io.*;
import java.util.logging.Logger;

public class FileCustomerRepository extends CustomerRepository {

    private final File file;
    private static final Logger logger = Logger.getLogger(FileCustomerRepository.class.getName());

    public FileCustomerRepository(String filename) {

        File folder = new File("data");
        if (!folder.exists()) folder.mkdirs();

        this.file = new File(filename);

        if (!file.exists() || file.length() == 0) {
            writeHeader();
        }

        loadFromFile();
    }

    private void writeHeader() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("id,name,address,email,phone");
            bw.newLine();
        } catch (IOException e) {
            logger.severe("Error writing header: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;
            br.readLine(); // skip header

            int maxId = 0;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");

                if (parts.length != 5) {
                    logger.warning("Skipping corrupt line: " + line);
                    continue;
                }

                try {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String address = parts[2];
                    String email = parts[3];
                    String phone = parts[4];

                    Customer c = new Customer(name, address, email, phone);
                    c.setId(id);

                    super.saveCustomer(c);

                    if (id > maxId) maxId = id;

                } catch (Exception e) {
                    logger.warning("Skipping invalid line: " + line);
                }
            }

            // Mantener el contador sincronizado
            Customer.setCounter(maxId + 1);

        } catch (IOException e) {
            logger.severe("Error loading customers: " + e.getMessage());
        }
    }

    private void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

            bw.write("id,name,address,email,phone");
            bw.newLine();

            for (Customer c : super.listCustomers()) {
                String line = c.getId() + "," +
                        c.getName() + "," +
                        c.getAddress() + "," +
                        c.getEmail() + "," +
                        c.getPhone();
                bw.write(line);
                bw.newLine();
            }

        } catch (IOException e) {
            logger.severe("Error saving customers: " + e.getMessage());
        }
    }

    @Override
    public void saveCustomer(Customer customer) {
        super.saveCustomer(customer);
        saveToFile();
    }
}
