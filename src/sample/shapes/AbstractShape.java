package sample.shapes;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import sample.Controller;
import sample.Main;

abstract class AbstractShape implements Shape{

    private double[] startPoints = new double[2];
    private double[] endPoints = new double[2];
    private Color fillColor = Color.TRANSPARENT;
    private Canvas layer;

    AbstractShape() {
        layer = new Canvas(Controller.getInstance().getMainWidth(), Controller.getInstance().getMainHeight());
        Main.getInstance().getShapes().push(this);
    }

    Canvas getLayer(){
        return layer;
    }

    @Override
    public Color getFillColor() {
        return fillColor;
    }

    @Override
    public double[] getStartingPoints() {
        return startPoints;
    }

    @Override
    public double[] getEndPoints() {
        return endPoints;
    }

    @Override
    public void remove() {
        Controller.getInstance().getCanvasHolder().getChildren().remove(getLayer());
    }

    void setStartingPoints(double[] startPoints) {
        this.startPoints = startPoints;
    }


    void setEndPoints(double[] endPoints) {
        this.endPoints = endPoints;
    }
}
