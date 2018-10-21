package sample.shapes;

import javafx.scene.paint.Color;

public interface Shape {
    Color getFillColor();
    double[] getStartingPoints();
    double[] getEndPoints();
    void draw();
    ShapeType getType();
    void remove();
}
