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

    @FXML
    Canvas graph;

    @FXML
    private void initialize(){
        GraphicsContext graphicsContext = graph.getGraphicsContext2D();
        graphicsContext.fillRect(0,0,100,100);
        graph.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.print("Thanks!");
                GraphicsContext graphicsContext = graph.getGraphicsContext2D();
                graphicsContext.fillRect(0,0,100,100);
            }
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = null;
        mainController controller;
        try{
            System.out.print("--");
            fxmlLoader.setLocation(getClass().getResource("../fxml/main.fxml"));

            root = fxmlLoader.load();
            System.out.println(root);

            controller = fxmlLoader.getController();
        } catch (IOException e){
            e.printStackTrace();
        }

        primaryStage.setTitle("Least Squares");
        primaryStage.setMinHeight(480);
        primaryStage.setMinWidth(720);
        primaryStage.setScene(new Scene(root, 720, 480));

        primaryStage.widthProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) -> {
            ((mainController)fxmlLoader.getController()).windowResized(newSceneWidth,primaryStage.getHeight());
        });
        primaryStage.heightProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) -> {
            ((mainController)fxmlLoader.getController()).windowResized(primaryStage.getWidth(),newSceneHeight);
        });

        primaryStage.show();
    }


    public static void main(String[] args) {
        System.out.println("ok");launch(args);
    }

}
