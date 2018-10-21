package sample.shapes.util;

import sample.Controller;
import sample.Main;
import sample.shapes.Position;
import sample.shapes.Shape;

public class ShapeSelector {
    public static Shape getSelectedShape(double x, double y) {
        for (Shape s : Main.getInstance().getShapes()) {
            if (isInRange(x, s.getStartingPoints()[0], y, s.getStartingPoints()[1])) {
                Controller.getInstance().position = Position.START;
                return s;
            } else if (isInRange(x, s.getEndPoints()[0], y, s.getEndPoints()[1])) {
                Controller.getInstance().position = Position.END;
                return s;
            }
        }
        return null;
    }

    private static boolean isInRange(double x1, double x2, double y1, double y2) {
        return Math.abs(x1 - x2) < 15 && Math.abs(y1 - y2) < 15;
    }
}
