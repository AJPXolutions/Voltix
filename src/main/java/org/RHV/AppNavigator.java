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

/**
 * Clase centralizada para gestionar la navegación entre ventanas (FXML)
 * en la aplicación Voltix.
 *
 * Su propósito es:
 *  - Evitar duplicar código de carga de vistas
 *  - Aplicar animaciones suaves en cada transición
 *  - Mantener un único Stage principal para toda la app
 *
 * Funciona como un "router" o "navigator" global.
 */
public class AppNavigator {

    // Stage principal de la aplicación (ventana)
    private static Stage mainStage;

    private static final Logger logger = Logger.getLogger(AppNavigator.class.getName());

    /**
     * Asigna el Stage principal. Debe llamarse desde Main.java.
     */
    public static void setStage(Stage stage) {
        mainStage = stage;
    }

    // ============================
    //   NAVEGACIÓN PÚBLICA
    // ============================

    /**
     * Carga la vista principal (dashboard).
     */
    public static void goToMainMenu() {
        loadView("main-view.fxml", null);
    }

    /**
     * Carga la vista de listado de clientes.
     */
    public static void goToListCustomers() {
        loadView("list-customers.fxml", null);
    }

    /**
     * Carga la vista para generar facturas.
     */
    public static void goToGenerateInvoice() {
        loadView("generate-invoice.fxml", null);
    }

    /**
     * Carga la vista de listado de facturas.
     */
    public static void goToListInvoices() {
        loadView("list-invoices.fxml", null);
    }

    /**
     * Carga la vista para registrar un nuevo cliente.
     */
    public static void goToRegisterCustomer() {
        loadView("register-customer.fxml", null);
    }

    // ============================
    //   MÉTODO ÚNICO Y CENTRALIZADO
    // ============================

    /**
     * Carga cualquier vista FXML dentro del Stage principal.
     *
     * @param fxml Nombre del archivo FXML a cargar.
     * @param controllerSetup Acción opcional para configurar el controlador cargado.
     *
     * Flujo:
     *  1. Verificar que el Stage esté inicializado
     *  2. Cargar el FXML con FXMLLoader
     *  3. Aplicar configuración opcional al controlador
     *  4. Crear nueva escena y asignarla al Stage
     *  5. Aplicar animaciones suaves
     */
    private static void loadView(String fxml, Consumer<Object> controllerSetup) {
        try {
            if (mainStage == null) {
                logger.severe("Main stage has not been initialized. Call AppNavigator.setStage() first.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxml));
            Parent root = loader.load();

            // Configuración opcional del controlador
            if (controllerSetup != null) {
                controllerSetup.accept(loader.getController());
            }

            // Crear escena y asignarla al Stage
            Scene scene = new Scene(root, 900, 600);
            mainStage.setScene(scene);

            // Animaciones suaves
            applyTransitions(root);

        } catch (Exception e) {
            logger.severe("Error loading view '" + fxml + "': " + e.getMessage());
        }
    }

    // ============================
    //   TRANSICIONES SUAVES
    // ============================

    /**
     * Aplica todas las animaciones definidas a la vista cargada.
     */
    private static void applyTransitions(Parent root) {
        applyFadeTransition(root);
        applySlideTransition(root);
        // applyZoomTransition(root); // opcional
    }

    /**
     * Animación de desvanecimiento (fade-in).
     */
    private static void applyFadeTransition(Parent root) {
        FadeTransition ft = new FadeTransition(Duration.millis(450), root);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setInterpolator(Interpolator.EASE_OUT);
        ft.play();
    }

    /**
     * Animación de desplazamiento horizontal (slide-in).
     */
    private static void applySlideTransition(Parent root) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(350), root);
        tt.setFromX(40);
        tt.setToX(0);
        tt.setInterpolator(Interpolator.EASE_BOTH);
        tt.play();
    }

    /**
     * Animación opcional de zoom suave.
     */
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
