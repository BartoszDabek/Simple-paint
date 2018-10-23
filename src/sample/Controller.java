package sample;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import sample.color.CMYK;
import sample.color.RGB;
import sample.shape.*;
import sample.shape.util.ShapeSelector;

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
    private TextField labelStartX, labelStartY, labelEndX, labelEndY, rRGBLabel, gRGBLabel, bRGBLabel;
    @FXML
    private ToggleButton move, resize, resizeByMove;
    @FXML
    private Slider rRGBSlider, gRGBSlider, bRGBSlider, cCMYKSlider, mCMYKSlider, yCMYKSlider, kCMYKSlider;
    @FXML
    private javafx.scene.shape.Rectangle colorDisplay;
    @FXML
    private TextField cCMYKLabel, mCMYKLabel, yCMYKLabel, kCMYKLabel;

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
        colorDisplay.setFill(new Color(0, 0, 0, 1));
        initializeRGB();
        initializeCMYK();

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

    private void initializeRGB() {
        RGB.bind(colorDisplay, rRGBLabel, gRGBLabel, bRGBLabel, rRGBSlider, gRGBSlider, bRGBSlider);
    }

    private void initializeCMYK() {
        CMYK.bind(cCMYKLabel, mCMYKLabel, yCMYKLabel, kCMYKLabel, cCMYKSlider, mCMYKSlider, yCMYKSlider, kCMYKSlider);
    }

    @FXML
    private void rgb2cmyk() {
        double[] cmyk = RGB.toCMYK(Integer.valueOf(rRGBLabel.getText()),
                                    Integer.valueOf(gRGBLabel.getText()),
                                    Integer.valueOf(bRGBLabel.getText()));

        cCMYKLabel.setText(String.format("%.2f", cmyk[0]));
        mCMYKLabel.setText(String.format("%.2f", cmyk[1]));
        yCMYKLabel.setText(String.format("%.2f", cmyk[2]));
        kCMYKLabel.setText(String.format("%.2f", cmyk[3]));
    }

    @FXML
    private void cmyk2rgb() {
        int[] rgb = CMYK.toRGB(Float.valueOf(cCMYKLabel.getText()),
                                Float.valueOf(mCMYKLabel.getText()),
                                Float.valueOf(yCMYKLabel.getText()),
                                Float.valueOf(kCMYKLabel.getText()));

        rRGBLabel.setText(String.valueOf(rgb[0]));
        gRGBLabel.setText(String.valueOf(rgb[1]));
        bRGBLabel.setText(String.valueOf(rgb[2]));
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

    private void clearLabels() {
        labelStartX.clear();
        labelStartY.clear();
        labelEndX.clear();
        labelEndY.clear();
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
