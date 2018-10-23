package sample.color;

import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public final class RGB {

    private RGB() {

    }

    public static double[] toCMYK(int... rgb) {
        double computedC, computedM, computedY, computedK;

        computedC = 1 - ((double) rgb[0] / 255);
        computedM = 1 - ((double) rgb[1] / 255);
        computedY = 1 - ((double) rgb[2] / 255);

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

        return new double[] { computedC, computedM, computedY, computedK };
    }

    public static void bind(Rectangle colorDisplay, TextField rRGBLabel, TextField gRGBLabel, TextField bRGBLabel, Slider rRGBSlider, Slider gRGBSlider, Slider bRGBSlider) {
        rRGBLabel.setText("0");
        gRGBLabel.setText("0");
        bRGBLabel.setText("0");

        bindRGBSliderToInput(rRGBSlider, rRGBLabel);
        bindRGBSliderToInput(gRGBSlider, gRGBLabel);
        bindRGBSliderToInput(bRGBSlider, bRGBLabel);

        rRGBLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            rRGBSlider.setValue(Double.valueOf(newValue));
            colorDisplay.setFill(Color.rgb(Integer.valueOf(rRGBLabel.getText()), Integer.valueOf(gRGBLabel.getText()), Integer.valueOf(bRGBLabel.getText())));
        });
        gRGBLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            gRGBSlider.setValue(Double.valueOf(newValue));
            colorDisplay.setFill(Color.rgb(Integer.valueOf(rRGBLabel.getText()), Integer.valueOf(gRGBLabel.getText()), Integer.valueOf(bRGBLabel.getText())));
        });
        bRGBLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            bRGBSlider.setValue(Double.valueOf(newValue));
            colorDisplay.setFill(Color.rgb(Integer.valueOf(rRGBLabel.getText()), Integer.valueOf(gRGBLabel.getText()), Integer.valueOf(bRGBLabel.getText())));
        });
    }

    private static void bindRGBSliderToInput(Slider slider, TextField textField) {
        slider.valueProperty().addListener((obs, oldval, newVal) ->
                textField.setText(String.valueOf(newVal.intValue())));
    }

}
