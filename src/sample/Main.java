package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.shape.Shape;

import java.util.Stack;

public class Main extends Application {

    private static Main instance;
    private Stack<Shape> shapes = new Stack<>();

    @Override
    public void start(Stage primaryStage) throws Exception{
        setInstance(this);
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private static void setInstance(Main instance) {
        Main.instance = instance;
    }

    public static Main getInstance() {
        return instance;
    }

    public Stack<Shape> getShapes() {
        return shapes;
    }
}
