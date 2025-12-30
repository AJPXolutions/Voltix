package org.RHV.controller;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MainViewController {

    @FXML private StackPane contentArea;

    @FXML private Button btnRegisterCustomer;
    @FXML private Button btnListCustomers;
    @FXML private Button btnGenerateInvoice;
    @FXML private Button btnListInvoices;

    @FXML private ImageView logoVoltix;

    @FXML private Label lblTotalCustomers;
    @FXML private Label lblTotalInvoices;
    @FXML private Label lblPending;

    @FXML private BarChart<String, Number> chartConsumptionByCustomer;
    @FXML private LineChart<String, Number> chartMonthlyConsumption;
    @FXML private PieChart chartInvoicesByCustomer;

    // NUEVO
    @FXML private Label labelVoltix;
    @FXML private Label labelVoltixHeader;
    @FXML private VBox dashboardRoot;

    private static final Logger logger = Logger.getLogger(MainViewController.class.getName());

    @FXML
    public void initialize() {

        // Logo
        try {
            logoVoltix.setImage(new Image(
                    MainViewController.class.getResourceAsStream("/org/RHV/icon.png")
            ));
        } catch (Exception ignored) {}

        // Navegación
        btnRegisterCustomer.setOnAction(e -> loadCenterView("/org/RHV/register-customer.fxml"));
        btnListCustomers.setOnAction(e -> loadCenterView("/org/RHV/list-customers.fxml"));
        btnGenerateInvoice.setOnAction(e -> loadCenterView("/org/RHV/generate-invoice.fxml"));
        btnListInvoices.setOnAction(e -> loadCenterView("/org/RHV/list-invoices.fxml"));

        // NUEVO: volver al dashboard desde el sidebar
        labelVoltix.setOnMouseClicked(e -> showDashboard());

        // NUEVO: volver al dashboard desde el header
        labelVoltixHeader.setOnMouseClicked(e -> showDashboard());

        // Dashboard numbers
        try {
            lblTotalCustomers.setText(String.valueOf(MainControllers.customerController.getAllCustomers().size()));
            lblTotalInvoices.setText(String.valueOf(MainControllers.invoiceController.getAllInvoices().size()));
        } catch (Exception e) {
            lblTotalCustomers.setText("0");
            lblTotalInvoices.setText("0");
        }

        lblPending.setText("0");

        loadCharts();
    }

    // NUEVO
    private void showDashboard() {
        contentArea.getChildren().setAll(dashboardRoot);
        animateView(dashboardRoot);
    }

    private void loadCenterView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();

            contentArea.getChildren().setAll(view);

            animateView(view);

        } catch (Exception ex) {
            logger.severe("Error loading view: " + fxmlPath + " | " + ex.getMessage());
        }
    }

    private void animateView(Parent view) {
        FadeTransition fade = new FadeTransition(Duration.millis(350), view);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setInterpolator(Interpolator.EASE_OUT);

        TranslateTransition slide = new TranslateTransition(Duration.millis(300), view);
        slide.setFromX(30);
        slide.setToX(0);
        slide.setInterpolator(Interpolator.EASE_BOTH);

        fade.play();
        slide.play();
    }

    private void loadCharts() {
        try {
            var invoices = MainControllers.invoiceController.getAllInvoices();

            chartConsumptionByCustomer.getData().clear();
            chartMonthlyConsumption.getData().clear();
            chartInvoicesByCustomer.getData().clear();

            // --- BARRAS ---
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("kWh");

            Map<String, Double> consumptionByCustomer = invoices.stream()
                    .collect(Collectors.groupingBy(
                            inv -> inv.getCustomer().getName(),
                            Collectors.summingDouble(inv -> inv.getConsumption().getKWh())
                    ));

            consumptionByCustomer.forEach((customer, kwh) ->
                    series.getData().add(new XYChart.Data<>(customer, kwh))
            );

            chartConsumptionByCustomer.getData().add(series);

            // --- LÍNEAS ---
            XYChart.Series<String, Number> lineSeries = new XYChart.Series<>();
            lineSeries.setName("Monthly kWh");

            Map<String, Double> monthly = invoices.stream()
                    .collect(Collectors.groupingBy(
                            inv -> inv.getConsumption().getStartDate()
                                    .getMonth()
                                    .getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                            Collectors.summingDouble(inv -> inv.getConsumption().getKWh())
                    ));

            monthly.forEach((month, kwh) ->
                    lineSeries.getData().add(new XYChart.Data<>(month, kwh))
            );

            chartMonthlyConsumption.getData().add(lineSeries);

            // --- PIE ---
            Map<String, Long> invoicesByCustomer = invoices.stream()
                    .collect(Collectors.groupingBy(
                            inv -> inv.getCustomer().getName(),
                            Collectors.counting()
                    ));

            invoicesByCustomer.forEach((customer, count) ->
                    chartInvoicesByCustomer.getData().add(
                            new PieChart.Data(customer, count)
                    )
            );

        } catch (Exception ex) {
            logger.warning("Error loading charts: " + ex.getMessage());
        }
    }
}
