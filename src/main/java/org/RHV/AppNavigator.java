package org.RHV;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.RHV.controller.ListCustomersController;
import org.RHV.controller.MainControllers;

import java.util.function.Consumer;
import java.util.logging.Logger;

public class AppNavigator {

    private static Stage mainStage;
    private static final Logger logger = Logger.getLogger(AppNavigator.class.getName());

    public static void setStage(Stage stage) {
        mainStage = stage;
    }

    // ============================
    //   NAVEGACIÓN PÚBLICA
    // ============================

    public static void goToMainMenu() {
        loadView("main-view.fxml", null);
    }

    public static void goToListCustomers() {
        loadView("list-customers.fxml", null);
    }

    public static void goToGenerateInvoice() {
        loadView("generate-invoice.fxml", null);
    }

    public static void goToListInvoices() {
        loadView("list-invoices.fxml", null);
    }

    public static void goToRegisterCustomer() {
        loadView("register-customer.fxml", null);
    }

    // ============================
    //   MÉTODO ÚNICO Y CENTRALIZADO
    // ============================

    private static void loadView(String fxml, Consumer<Object> controllerSetup) {
        try {
            if (mainStage == null) {
                logger.severe("Main stage has not been initialized. Call AppNavigator.setStage() first.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxml));
            Parent root = loader.load();

            if (controllerSetup != null) {
                controllerSetup.accept(loader.getController());
            }

            Scene scene = new Scene(root, 900, 600);
            mainStage.setScene(scene);

            applyTransitions(root);

        } catch (Exception e) {
            logger.severe("Error loading view '" + fxml + "': " + e.getMessage());
        }
    }

    // ============================
    //   TRANSICIONES SUAVES
    // ============================

    private static void applyTransitions(Parent root) {
        applyFadeTransition(root);
        applySlideTransition(root);
        // applyZoomTransition(root); // opcional
    }

    private static void applyFadeTransition(Parent root) {
        FadeTransition ft = new FadeTransition(Duration.millis(450), root);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setInterpolator(Interpolator.EASE_OUT);
        ft.play();
    }

    private static void applySlideTransition(Parent root) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(350), root);
        tt.setFromX(40);
        tt.setToX(0);
        tt.setInterpolator(Interpolator.EASE_BOTH);
        tt.play();
    }

    private static void applyZoomTransition(Parent root) {
        ScaleTransition st = new ScaleTransition(Duration.millis(300), root);
        st.setFromX(0.95);
        st.setFromY(0.95);
        st.setToX(1);
        st.setToY(1);
        st.setInterpolator(Interpolator.EASE_OUT);
        st.play();
    }
}
