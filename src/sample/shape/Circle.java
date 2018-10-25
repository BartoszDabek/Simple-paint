package sample.shape;

import sample.Controller;

public class Circle extends AbstractShape {

    public Circle(double[] startPoint, double[] endPoint) {
        super();
        setStartingPoints(startPoint);
        setEndingPoints(endPoint);
    }

    @Override
    public ShapeType getType() {
        return ShapeType.CIRCLE;
    }

    @Override
    public void draw() {
        getLayer().getGraphicsContext2D().setFill(getFillColor());
        fixDimensions();
        getLayer().getGraphicsContext2D().fillOval(getStartingPoints()[0], getStartingPoints()[1], Math.abs(getEndPoints()[0] - getStartingPoints()[0]), Math.abs(getEndPoints()[0] - getStartingPoints()[0]));
        getLayer().getGraphicsContext2D().strokeOval(getStartingPoints()[0], getStartingPoints()[1], Math.abs(getEndPoints()[0] - getStartingPoints()[0]), Math.abs(getEndPoints()[0] - getStartingPoints()[0]));
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
