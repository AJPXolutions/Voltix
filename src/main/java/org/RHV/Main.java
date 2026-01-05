package org.RHV;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación Voltix.
 * Extiende Application, lo que permite iniciar el ciclo de vida de JavaFX.
 *
 * Responsabilidades:
 *  - Inicializar el Stage principal
 *  - Cargar la vista inicial (main-view.fxml)
 *  - Registrar el Stage en AppNavigator para navegación global
 */
public class Main extends Application {

    /**
     * Método llamado automáticamente por JavaFX al iniciar la aplicación.
     * Aquí se configura la ventana principal y se carga la UI inicial.
     *
     * @param stage Ventana principal creada por el runtime de JavaFX.
     */
    @Override
    public void start(Stage stage) throws Exception {

        // Registrar el Stage principal en el AppNavigator
        // para permitir navegación centralizada desde cualquier parte de la app.
        AppNavigator.setStage(stage);

        // Establecer icono de la aplicación (ubicado en resources/org/RHV/icon.png)
        stage.getIcons().add(
                new javafx.scene.image.Image(
                        Main.class.getResourceAsStream("icon.png")
                )
        );

        // Cargar la vista principal (dashboard)
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
        Scene scene = new Scene(loader.load(), 900, 600);

        // Configurar ventana
        stage.setTitle("Voltix");
        stage.setScene(scene);
        stage.show(); // Mostrar ventana
    }

    /**
     * Método main estándar.
     * Llama a launch(), que inicia el motor de JavaFX.
     */
    public static void main(String[] args) {
        launch();
    }
}
