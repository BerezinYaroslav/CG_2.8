package ru.cs.vsu.berezin_y_a;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class HelloController {
    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;

    @FXML
    private void initialize() throws Exception {
        anchorPane.prefWidthProperty().addListener(
                (ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue())
        );
        anchorPane.prefHeightProperty().addListener(
                (ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue())
        );

        Sector.drawSector(
                canvas.getGraphicsContext2D(),
                500,
                350,
                200,
                Math.PI / 3,
                5 * Math.PI / 3,
                new Color(1, 1, 0, 1.0),
                new Color(1, 0, 1, 1.0)
        );
    }
}
