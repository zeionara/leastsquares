package controllers;

import interfaces.impls.SetPointsContainer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Graph;
import sample.Point;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Set;

/**
 * Created by Zerbs on 30.10.2016.
 */
public class editPointController {
    //FXML Objects

    @FXML
    Button okAddingPointButton;

    @FXML
    TextField setxField;

    @FXML
    TextField setyField;

    //For the alert window
    protected Parent fxmlAlert;
    protected FXMLLoader fxmlLoader = new FXMLLoader();
    protected alertController controller;
    protected Stage alertStage;

    @FXML
    private void initialize(){
        //initialize alert window
        try{
            fxmlLoader.setLocation(getClass().getResource("../fxml/alert.fxml"));
            fxmlAlert = fxmlLoader.load();
            controller = fxmlLoader.getController();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //External objects
    private TableView<Point> tablePoints;
    public void setTablePoints (TableView<Point> pointsData){
        this.tablePoints = pointsData;
    }
    public TableView<Point> getTablePoints (){
        return this.tablePoints;
    }

    private Canvas graph;
    public void setGraph (Canvas graph){
        this.graph = graph;
    }
    public Canvas getGraph (){
        return this.graph;
    }

    private SetPointsContainer pointsContainer;
    public void setPointsContainer (SetPointsContainer pointsContainer){
        this.pointsContainer = pointsContainer;
    }
    public SetPointsContainer getPointsContainer (){
        return this.pointsContainer;
    }

    private Point actualPoint;
    public void setActualPoint(Point newPoint){
        actualPoint = newPoint;
        setxField.setText(actualPoint.getX()+"");
        setyField.setText(actualPoint.getY()+"");
    }

    protected void initializeAlert(javafx.event.ActionEvent actionEvent){
        if (alertStage == null) {
            alertStage = new Stage();
            alertStage.setResizable(false);
            alertStage.setTitle("Error");
            alertStage.setScene(new Scene(fxmlAlert));
            alertStage.initModality(Modality.APPLICATION_MODAL);
            alertStage.initOwner(((Button)actionEvent.getSource()).getScene().getWindow());
        }
    }

    //Handlers for the events

    public void okAction(javafx.event.ActionEvent actionEvent){
        double[] coordinates;
        try {
            coordinates = tryParseXY(actionEvent);
        } catch (NumberFormatException e){
            return;
        }

        actualPoint.setX(coordinates[0]);
        actualPoint.setY(coordinates[1]);
        tablePoints.refresh();
        Graph.redraw(graph,pointsContainer);
        cancelPressed(actionEvent);
    }

    protected double[] tryParseXY(javafx.event.ActionEvent actionEvent){
        double[] coordinates = {0.0,0.0};
        try{
            coordinates[0] = Double.parseDouble(setxField.getText());
            coordinates[1] = Double.parseDouble(setyField.getText());
        } catch (NumberFormatException e){
            initializeAlert(actionEvent);
            alertStage.show();
            throw e;
        }
        return coordinates;
    }

    public void cancelPressed(javafx.event.ActionEvent actionEvent){
        Node src = (Node)actionEvent.getSource();
        Stage stage = (Stage)src.getScene().getWindow();
        stage.hide();
    }
}
