package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.MethodPredictoraCorrectora;
import sample.Graph;
import sample.Point;

import java.io.IOException;
import java.util.ArrayList;

public class mainController {

    @FXML
    AnchorPane sourceFieldsPane;

    @FXML
    TextField equationField;

    @FXML
    TextField x0Field;

    @FXML
    TextField xnField;

    @FXML
    TextField y0Field;

    @FXML
    TextField epsilonField;

    @FXML
    Canvas graph;

    private int numResizing = 1;
    private Parent fxmlAlert;
    private FXMLLoader fxmlLoader3 = new FXMLLoader();
    private alertController controller3;
    private Stage alertStage;
    @FXML
    private void initialize(){
        try{
            fxmlLoader3.setLocation(getClass().getResource("../fxml/alert.fxml"));
            fxmlAlert = fxmlLoader3.load();
            controller3 = fxmlLoader3.getController();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //Handlers for the events

    public void canvasScrolled(ScrollEvent e){
        if ((e.getDeltaY() < 0)){
            Graph.setScale(Graph.getScale()+Graph.SCALE_DELTA);
        } else if (Graph.getScale() > Graph.SCALE_DELTA*2){
            Graph.setScale(Graph.getScale()-Graph.SCALE_DELTA);
        }
        new Thread(() -> buildButtonClicked(new ActionEvent())).start();
    }

    public void windowResized(Number newW, Number newH){
        graph.setWidth(newW.doubleValue());
        graph.setHeight(newH.doubleValue() - sourceFieldsPane.getHeight() - 14);

        Graph.setSizeX(graph.getWidth());
        Graph.setSizeY(graph.getHeight());

        System.out.println("Resized"+numResizing);
        Graph.redraw(graph);
        if (numResizing <= 2){
            numResizing++;
            return;
        }
        new Thread(() -> buildButtonClicked(new ActionEvent())).start();
    }

    public void buildButtonClicked(ActionEvent actionEvent) {
        double x0;
        double y0;
        double xn;
        double epsilon;
        String equation;
        try {
            equation = equationField.getText();
            x0 = Double.parseDouble(x0Field.getText());
            y0 = Double.parseDouble(y0Field.getText());
            xn = Double.parseDouble(xnField.getText());
            epsilon = Double.parseDouble(epsilonField.getText());
            checkParameters(x0,xn,epsilon);
        } catch (NumberFormatException e){
            showAlert("Illegal input \n "+e.getMessage());
            return;
        }
        ArrayList<Point> points = MethodPredictoraCorrectora.findPoints(equation,x0,y0,epsilon,xn);
        Graph.redraw(graph);
        Graph.drawCurve(graph,points,Color.GREEN);
    }

    //Initializations

    private void initializeAlertStage(){
        alertStage = new Stage();
        alertStage.setResizable(false);
        alertStage.setTitle("Error");
        alertStage.setScene(new Scene(fxmlAlert));
        alertStage.initModality(Modality.APPLICATION_MODAL);
        alertStage.initOwner(sourceFieldsPane.getScene().getWindow());
    }

    //Additive methods

    public void showAlert(String str){
        if (alertStage == null) {
            initializeAlertStage();
        }

        controller3.setLabelText(str);
        alertStage.show();
    }

    private void checkParameters(double x0, double xn, double eps){
        if (eps <= 0){
            throw new NumberFormatException("Accuracy must be more than zero");
        }
        if (x0 >= xn){
            throw new NumberFormatException("x0 must be less than xn");
        }
    }
}