package sample.shape;

import javafx.scene.canvas.GraphicsContext;
import sample.Controller;

public class Rectangle extends AbstractShape {

    public Rectangle(double[] startPoint, double[] endPoints) {
        super();
        setStartingPoints(startPoint);
        setEndPoints(endPoints);
    }

    @Override
    public ShapeType getType() {
        return ShapeType.RECTANGLE;
    }

    @Override
    public void draw() {
        GraphicsContext gc = getLayer().getGraphicsContext2D();
        gc.setFill(getFillColor());
        fixDimensions();
        gc.fillRect(getStartingPoints()[0], getStartingPoints()[1], Math.abs(getEndPoints()[0] - getStartingPoints()[0]), Math.abs(getEndPoints()[1] - getStartingPoints()[1]));
        gc.strokeRect(getStartingPoints()[0], getStartingPoints()[1], Math.abs(getEndPoints()[0] - getStartingPoints()[0]), Math.abs(getEndPoints()[1] - getStartingPoints()[1]));
        Controller.getInstance().getCanvasHolder().getChildren().add(getLayer());
    }

    private void fixDimensions() {
        if (getEndPoints()[0] - getStartingPoints()[0] < 0) {
            double temp = getEndPoints()[0];
            getEndPoints()[0] = getStartingPoints()[0];
            getStartingPoints()[0] = temp;
        }
        if (getEndPoints()[1] - getStartingPoints()[1] < 0) {
            double temp = getEndPoints()[1];
            getEndPoints()[1] = getStartingPoints()[1];
            getStartingPoints()[1] = temp;
        }
    }
}
