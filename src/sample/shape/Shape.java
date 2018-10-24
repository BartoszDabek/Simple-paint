package sample.shape;

import javafx.scene.paint.Color;

public interface Shape {
    Color getFillColor();
    double[] getStartingPoints();
    double[] getEndPoints();
    void draw();
    ShapeType getType();
    void remove();
    void setFillColor(Color color);
    void recreate();
    void setStartingPoints(double[] startingPoints);
    void setEndingPoints(double[] endingPoints);
}
