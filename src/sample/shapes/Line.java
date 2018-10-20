package sample.shapes;

import sample.Controller;


public class Line extends AbstractShape{

    public Line(double[] startPoint, double[] endPoint) {
        super();
        setStartingPoints(startPoint);
        setEndPoints(endPoint);
    }

    @Override
    public String getType() {
        return "Linia";
    }

    @Override
    public void draw(){
        getLayer().getGraphicsContext2D().strokeLine(getStartingPoints()[0], getStartingPoints()[1], getEndPoints()[0], getEndPoints()[1]);
        Controller.getInstance().getCanvasHolder().getChildren().add(getLayer());
    }
}
