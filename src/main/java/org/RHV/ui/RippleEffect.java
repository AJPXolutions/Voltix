package org.RHV.ui;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class RippleEffect {

    public static void applyTo(Node node) {

        // Asegura que el padre es un Pane (VBox, HBox, etc.)
        if (!(node.getParent() instanceof Pane parent)) {
            return;
        }

        node.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {

            Circle ripple = new Circle(0);
            ripple.getStyleClass().add("ripple");

            parent.getChildren().add(ripple);

            ripple.setCenterX(node.getLayoutX() + e.getX());
            ripple.setCenterY(node.getLayoutY() + e.getY());

            double radius = Math.max(node.getBoundsInParent().getWidth(),
                    node.getBoundsInParent().getHeight()) * 1.5;

            Timeline animation = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(ripple.opacityProperty(), 0.4),
                            new KeyValue(ripple.radiusProperty(), 0)
                    ),
                    new KeyFrame(Duration.millis(400),
                            new KeyValue(ripple.opacityProperty(), 0),
                            new KeyValue(ripple.radiusProperty(), radius)
                    )
            );

            animation.setOnFinished(ev -> parent.getChildren().remove(ripple));
            animation.play();
        });
    }
}
