package sample;

import controllers.mainController;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private FXMLLoader fxmlLoader = new FXMLLoader();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = null;
        try{
            fxmlLoader.setLocation(getClass().getResource("../fxml/main.fxml"));
            root = fxmlLoader.load();
            System.out.println(root);
        } catch (IOException e){
            e.printStackTrace();
        }

        primaryStage.setMinHeight(480);
        primaryStage.setMinWidth(720);
        primaryStage.setScene(new Scene(root, 720, 480));

        primaryStage.widthProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) -> {
            ((mainController)fxmlLoader.getController()).windowResized(newSceneWidth,primaryStage.getHeight());
        });
        primaryStage.heightProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) -> {
            ((mainController)fxmlLoader.getController()).windowResized(primaryStage.getWidth(),newSceneHeight);
        });
        primaryStage.setTitle("Predictor-Corrector Method");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
