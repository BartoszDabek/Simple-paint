package sample.color;

import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public final class CMYK {

    private CMYK() {

    }

    public static int[] toRGB(float... cmyk) {
        float cyan = cmyk[0] / 100;
        float magneta = cmyk[1] / 100;
        float yellow = cmyk[2] / 100;
        float black = cmyk[3] / 100;

        double computedRed = 1 - Math.min(1,(double) cyan * (1 - black) + black);
        double computedGreen = 1 - Math.min(1,(double) magneta * (1 - black) + black);
        double computedBlue = 1 - Math.min(1,(double) yellow * (1 - black) + black);

        int r = (int) Math.round(computedRed * 255);
        int g = (int) Math.round(computedGreen * 255);
        int b = (int) Math.round(computedBlue * 255);

        return new int[] { r, g, b };
    }

    public static void bind(TextField cCMYKLabel, TextField mCMYKLabel, TextField yCMYKLabel, TextField kCMYKLabel, Slider cCMYKSlider, Slider mCMYKSlider, Slider yCMYKSlider, Slider kCMYKSlider) {
        cCMYKLabel.setText("0");
        mCMYKLabel.setText("0");
        yCMYKLabel.setText("0");
        kCMYKLabel.setText("0");

        bindCMYKSliderToInput(cCMYKSlider, cCMYKLabel);
        bindCMYKSliderToInput(mCMYKSlider, mCMYKLabel);
        bindCMYKSliderToInput(yCMYKSlider, yCMYKLabel);
        bindCMYKSliderToInput(kCMYKSlider, kCMYKLabel);

        cCMYKLabel.textProperty().addListener((observable, oldValue, newValue) -> cCMYKSlider.setValue(Double.valueOf(newValue)));
        mCMYKLabel.textProperty().addListener((observable, oldValue, newValue) -> mCMYKSlider.setValue(Double.valueOf(newValue)));
        yCMYKLabel.textProperty().addListener((observable, oldValue, newValue) -> yCMYKSlider.setValue(Double.valueOf(newValue)));
        kCMYKLabel.textProperty().addListener((observable, oldValue, newValue) -> kCMYKSlider.setValue(Double.valueOf(newValue)));
    }


    private static void bindCMYKSliderToInput(Slider slider, TextField textField) {
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!textField.isFocused()) {
                textField.setText(String.format("%.2f", newValue));
            }
        });
    }
}
