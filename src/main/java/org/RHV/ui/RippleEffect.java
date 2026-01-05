package org.RHV.ui;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * Clase utilitaria que aplica un efecto "ripple" (onda expansiva)
 * a cualquier nodo de JavaFX, simulando el estilo Material Design.
 *
 * El efecto consiste en un círculo que aparece donde el usuario hace clic
 * y se expande con una animación suave hasta desaparecer.
 */
public class RippleEffect {

    /**
     * Aplica el efecto ripple al nodo especificado.
     *
     * @param node Nodo al que se le aplicará el efecto (Button, Label, etc.)
     *
     * Requisitos:
     *  - El nodo debe estar dentro de un Pane (VBox, HBox, StackPane, etc.)
     *  - El Pane debe permitir agregar nodos hijos dinámicamente
     */
    public static void applyTo(Node node) {

        // Verifica que el padre del nodo sea un Pane.
        // Si no lo es, no se puede agregar el círculo del ripple.
        if (!(node.getParent() instanceof Pane parent)) {
            return;
        }

        // Manejar el evento de clic del mouse
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {

            // Crear el círculo inicial del efecto
            Circle ripple = new Circle(0);
            ripple.getStyleClass().add("ripple"); // Clase CSS para estilo

            // Agregar el círculo al contenedor padre
            parent.getChildren().add(ripple);

            // Posicionar el círculo exactamente donde el usuario hizo clic
            ripple.setCenterX(node.getLayoutX() + e.getX());
            ripple.setCenterY(node.getLayoutY() + e.getY());

            // Calcular el radio máximo para cubrir el nodo completo
            double radius = Math.max(
                    node.getBoundsInParent().getWidth(),
                    node.getBoundsInParent().getHeight()
            ) * 1.5;

            // Crear animación del ripple
            Timeline animation = new Timeline(
                    // Estado inicial: pequeño y visible
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(ripple.opacityProperty(), 0.4),
                            new KeyValue(ripple.radiusProperty(), 0)
                    ),
                    // Estado final: grande y transparente
                    new KeyFrame(Duration.millis(400),
                            new KeyValue(ripple.opacityProperty(), 0),
                            new KeyValue(ripple.radiusProperty(), radius)
                    )
            );

            // Cuando termina la animación, eliminar el círculo del Pane
            animation.setOnFinished(ev -> parent.getChildren().remove(ripple));

            // Ejecutar animación
            animation.play();
        });
    }
}
