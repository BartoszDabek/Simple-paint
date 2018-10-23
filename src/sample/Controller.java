package sample;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
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
    private TextField labelStartX, labelStartY, labelEndX, labelEndY, rLabel, gLabel, bLabel;
    @FXML
    private ToggleButton move, resize, resizeByMove;
    @FXML
    private Slider rRGB, gRGB, bRGB, sliderC, sliderM, sliderY, sliderK;
    @FXML
    private javafx.scene.shape.Rectangle rgbColor;
    @FXML
    private TextField cCMYK, mCMYK, yCMYK, kCMYK;

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
        rLabel.setText("0");
        gLabel.setText("0");
        bLabel.setText("0");

        rgbColor.setFill(new Color(0, 0, 0, 1));
        bindRGBSliderToInput(rRGB, rLabel);
        bindRGBSliderToInput(gRGB, gLabel);
        bindRGBSliderToInput(bRGB, bLabel);
        rLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            rRGB.setValue(Double.valueOf(newValue));
            rgbColor.setFill(Color.rgb(Integer.valueOf(rLabel.getText()), Integer.valueOf(gLabel.getText()), Integer.valueOf(bLabel.getText())));
        });
        gLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            gRGB.setValue(Double.valueOf(newValue));
            rgbColor.setFill(Color.rgb(Integer.valueOf(rLabel.getText()), Integer.valueOf(gLabel.getText()), Integer.valueOf(bLabel.getText())));
        });
        bLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            bRGB.setValue(Double.valueOf(newValue));
            rgbColor.setFill(Color.rgb(Integer.valueOf(rLabel.getText()), Integer.valueOf(gLabel.getText()), Integer.valueOf(bLabel.getText())));
        });
    }

    private void initializeCMYK() {
        cCMYK.setText("0");
        mCMYK.setText("0");
        yCMYK.setText("0");
        kCMYK.setText("0");

        bindCMYKSliderToInput(sliderC, cCMYK);
        bindCMYKSliderToInput(sliderM, mCMYK);
        bindCMYKSliderToInput(sliderY, yCMYK);
        bindCMYKSliderToInput(sliderK, kCMYK);

        cCMYK.textProperty().addListener((observable, oldValue, newValue) -> {
            sliderC.setValue(Double.valueOf(newValue));
        });

        mCMYK.textProperty().addListener((observable, oldValue, newValue) -> {
            sliderM.setValue(Double.valueOf(newValue));
        });

        yCMYK.textProperty().addListener((observable, oldValue, newValue) -> {
            sliderY.setValue(Double.valueOf(newValue));
        });

        kCMYK.textProperty().addListener((observable, oldValue, newValue) -> {
            sliderK.setValue(Double.valueOf(newValue));
        });
    }

    private void bindRGBSliderToInput(Slider slider, TextField textField) {
        slider.valueProperty().addListener((obs, oldval, newVal) ->
                textField.setText(String.valueOf(newVal.intValue())));
    }

    private void bindCMYKSliderToInput(Slider slider, TextField textField) {
        slider.valueProperty().addListener((obs, oldval, newVal) ->
                textField.setText(String.format("%.2f", newVal)));
    }

    @FXML
    private void rgb2cmyk() {
        double computedC, computedM, computedY, computedK;

        computedC = 1 - ((double) Integer.valueOf(rLabel.getText())/255);
        computedM = 1 - ((double) Integer.valueOf(gLabel.getText())/255);
        computedY = 1 - ((double) Integer.valueOf(bLabel.getText())/255);

        var minCMY = Math.min(computedC,
                Math.min(computedM,computedY));
        computedC = (computedC - minCMY) / (1 - minCMY) ;
        computedM = (computedM - minCMY) / (1 - minCMY) ;
        computedY = (computedY - minCMY) / (1 - minCMY) ;
        computedK = minCMY;

        computedC *= 100;
        computedM *= 100;
        computedY *= 100;
        computedK *= 100;

        cCMYK.setText(String.format("%.2f", computedC));
        mCMYK.setText(String.format("%.2f", computedM));
        yCMYK.setText(String.format("%.2f", computedY));
        kCMYK.setText(String.format("%.2f", computedK));
    }

    @FXML
    private void cmyk2rgb() {
        int r, g, b;

        float cyan = Float.valueOf(cCMYK.getText()) / 100;
        float magneta = Float.valueOf(mCMYK.getText()) / 100;
        float yellow = Float.valueOf(yCMYK.getText()) / 100;
        float black = Float.valueOf(kCMYK.getText()) / 100;

        double computedRed = 1 - Math.min(1,(double) cyan * (1 - black) + black);
        r = (int) Math.round(computedRed * 255);

        double computedGreen = 1 - Math.min(1,(double) magneta * (1 - black) + black);
        g = (int) Math.round(computedGreen * 255);

        double computedBlue = 1 - Math.min(1,(double) yellow * (1 - black) + black);
        b = (int) Math.round(computedBlue * 255);

        rLabel.setText(String.valueOf(r));
        gLabel.setText(String.valueOf(g));
        bLabel.setText(String.valueOf(b));
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
