package sample.shape;

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
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    @Override
    public void recreate() {
        Main.getInstance().getShapes().remove(this);
        remove();
        getLayer().getGraphicsContext2D().clearRect(0, 0, getLayer().getWidth(), getLayer().getHeight());
        Main.getInstance().getShapes().push(this);
        draw();
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

    @Override
    public void setStartingPoints(double[] startPoints) {
        this.startPoints = startPoints;
    }

    @Override
    public void setEndingPoints(double[] endingPoints) {
        this.endPoints = endingPoints;
    }
}
