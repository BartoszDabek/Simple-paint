package sample;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import sample.shapes.Circle;
import sample.shapes.Line;
import sample.shapes.Rectangle;
import sample.shapes.Shape;

public class Controller {

    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private double mainWidth = 1195;
    private double mainHeight = 629;

    @FXML
    private Canvas canvas;

    @FXML
    private ToggleGroup toggleGroup;

    @FXML
    private StackPane canvasHolder;

    @FXML
    private Label coordinates;

    private GraphicsContext gc;
    private static Controller instance;

    public static Controller getInstance() {
        return instance;
    }

    public static void setInstance(Controller instance) {
        Controller.instance = instance;
    }

    @FXML
    public void initialize() {
        setInstance(this);

        canvasHolder.setMaxHeight(mainHeight);
        canvasHolder.setMaxWidth(mainWidth);


        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());

        canvas.setHeight(mainHeight);
        canvas.setWidth(mainWidth);

        canvasHolder.setOnMouseMoved(event -> coordinates.setText("X: " + event.getX() + "Y: " + event.getY()));

        canvasHolder.setOnMousePressed(event -> {
//            Shape s = ShapeSelector.getSelectedShape(event.getX(), event.getY());

            startX = event.getX();
            startY = event.getY();
        });

        canvasHolder.setOnMouseReleased(event -> {
            endX = event.getX();
            endY = event.getY();
            ToggleButton selectedToggle = (ToggleButton) toggleGroup.getSelectedToggle();

            if (selectedToggle != null) {
                String text = selectedToggle.getText();

                switch (text) {
                    case "Linia":
                        new Line(new double[]{startX, startY}, new double[]{endX, endY})
                                .draw();
                        break;

                    case "Prostokąt":
                        new Rectangle(new double[]{startX, startY}, new double[]{endX, endY})
                                .draw();
                        break;

                    case "Okrąg":
                        new Circle(new double[]{startX, startY}, new double[]{endX, endY})
                                .draw();
                        break;
                }
            }

        });
    }

    public StackPane getCanvasHolder() {
        return canvasHolder;
    }

    public double getMainWidth() {
        return mainWidth;
    }

    public double getMainHeight() {
        return mainHeight;
    }

}

class ShapeSelector {
    public static Shape getSelectedShape(double x, double y) {
        for (Shape s : Main.getInstance().getShapes()) {
            if (isInRange(x, s.getStartingPoints()[0], y, s.getStartingPoints()[1]))
                return s;
            if (isInRange(x, s.getEndPoints()[0], y, s.getEndPoints()[0]))
                return s;
        }
        return null;
    }

    private static boolean isInRange(double x1, double x2, double y1, double y2) {
        return Math.abs(x1 - x2) < 5 && Math.abs(y1 - y2) < 5;
    }
}
