package org.RHV;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws Exception {

        AppNavigator.setStage(stage);

        stage.getIcons().add( new javafx.scene.image.Image( Main.class.getResourceAsStream("icon.png") ) );

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
        Scene scene = new Scene(loader.load(), 900, 600);

        stage.setTitle("Voltix");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
