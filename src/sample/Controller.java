package sample;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import sample.shapes.*;
import sample.shapes.util.ShapeSelector;

import java.util.Optional;

public class Controller {

    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private double mainWidth = 1195;
    private double mainHeight = 629;
    private Optional<Shape> tempShape = Optional.empty();
    private static Controller instance;
    public Position position;

    @FXML
    private Canvas mainCanvas;

    @FXML
    private ToggleGroup shapesGroup;

    @FXML
    private StackPane canvasHolder;

    @FXML
    private Label coordinates;

    @FXML
    private TextField labelStartX, labelStartY, labelEndX, labelEndY;

    @FXML
    private ToggleButton move, resize, resizeByMove;

    @FXML
    public void initialize() {
        setInstance(this);

        canvasHolder.setMaxHeight(mainHeight);
        canvasHolder.setMaxWidth(mainWidth);


        GraphicsContext gc = mainCanvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, this.mainCanvas.getWidth(), this.mainCanvas.getHeight());

        mainCanvas.setHeight(mainHeight);
        mainCanvas.setWidth(mainWidth);

        canvasHolder.setOnMouseMoved(event -> {
            coordinates.setText("X: " + event.getX() + "  Y: " + event.getY());
        });

        canvasHolder.setOnMousePressed(event -> {
            startX = event.getX();
            startY = event.getY();

            if (move.isSelected() || resize.isSelected() || resizeByMove.isSelected()) {
                tempShape = Optional.ofNullable(ShapeSelector.getSelectedShape(event.getX(), event.getY()));

                if (resize.isSelected()) {
                    tempShape.ifPresent(shape -> {
                        labelStartX.setText(String.valueOf(shape.getStartingPoints()[0]));
                        labelStartY.setText(String.valueOf(shape.getStartingPoints()[1]));
                        labelEndX.setText(String.valueOf(shape.getEndPoints()[0]));
                        labelEndY.setText(String.valueOf(shape.getEndPoints()[1]));
                    });
                }
            }
        });

        canvasHolder.setOnMouseReleased(event -> {
            endX = event.getX();
            endY = event.getY();
            double translateX = endX -startX;
            double translateY = endY - startY;

            if (move.isSelected()) {
                tempShape.ifPresent(shape -> {
                    startX = shape.getStartingPoints()[0] + (translateX);
                    startY = shape.getStartingPoints()[1] + (translateY);
                    endX = shape.getEndPoints()[0] + translateX;
                    endY = shape.getEndPoints()[1] + translateY;

                    Main.getInstance().getShapes().remove(shape);
                    shape.remove();
                    drawShape(shape.getType());
                });
                return;
            } else if (resizeByMove.isSelected()) {
                tempShape.ifPresent(shape -> {
                    if (Position.START.equals(position)) {
                        startX = shape.getStartingPoints()[0] + (translateX);
                        startY = shape.getStartingPoints()[1] + (translateY);
                        endX = shape.getEndPoints()[0];
                        endY = shape.getEndPoints()[1];
                    } else if (Position.END.equals(position)) {
                        startX = shape.getStartingPoints()[0];
                        startY = shape.getStartingPoints()[1];
                        endX = shape.getEndPoints()[0] + translateX;
                        endY = shape.getEndPoints()[1] + translateY;
                    }
                    Main.getInstance().getShapes().remove(shape);
                    shape.remove();
                    drawShape(shape.getType());
                });
                return;
            } else if (resize.isSelected()) {
                return;
            }

            drawSelectedShape();
        });
    }

    @FXML
    private void draw() {
        startX = parseInputToDouble(labelStartX);
        startY = parseInputToDouble(labelStartY);
        endX = parseInputToDouble(labelEndX);
        endY = parseInputToDouble(labelEndY);

        try {
            if (resize.isSelected()) {
                tempShape.ifPresent(shape -> {
                    Main.getInstance().getShapes().remove(shape);
                    shape.remove();
                    drawShape(shape.getType());
                });
                return;
            }

            drawSelectedShape();
        } finally {
            clearLabels();
        }
    }

    private double parseInputToDouble(TextField labelStartX) {
        return Double.parseDouble(Optional.of(labelStartX.getText()).filter(s -> !s.isEmpty()).orElse("0"));
    }

    private void clearLabels() {
        labelStartX.clear();
        labelStartY.clear();
        labelEndX.clear();
        labelEndY.clear();
    }

    private void drawSelectedShape() {
        ToggleButton selectedToggle = (ToggleButton) shapesGroup.getSelectedToggle();
        if (selectedToggle != null) {
            String type = selectedToggle.getText();
            ShapeType shapeType = ShapeType.getShapeType(type);
            drawShape(shapeType);
        }
    }

    private void drawShape(ShapeType type) {
        switch (type) {
            case LINE:
                new Line(new double[]{startX, startY}, new double[]{endX, endY})
                        .draw();
                break;

            case RECTANGLE:
                new Rectangle(new double[]{startX, startY}, new double[]{endX, endY})
                        .draw();
                break;

            case CIRCLE:
                new Circle(new double[]{startX, startY}, new double[]{endX, endY})
                        .draw();
                break;
        }
    }

    public static Controller getInstance() {
        return instance;
    }

    private static void setInstance(Controller instance) {
        Controller.instance = instance;
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
