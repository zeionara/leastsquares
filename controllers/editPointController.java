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

import java.io.IOException;
import java.util.Set;

/**
 * Created by Zerbs on 30.10.2016.
 */
public class editPointController {
    @FXML
    Button okAddingPointButton;
    private TableView<Point> tablePoints;
    private Canvas graph;
    private SetPointsContainer pointsContainer;
    private Point actualPoint;

    //
    protected Parent fxmlAlert;
    protected FXMLLoader fxmlLoader = new FXMLLoader();
    protected alertController controller;
    protected Stage alertStage;
    //

    public void setPointsContainer (SetPointsContainer pointsContainer){
        this.pointsContainer = pointsContainer;
    }

    public void setTablePoints (TableView<Point> pointsData){
        this.tablePoints = pointsData;
    }

    public void setGraph (Canvas graph){
        this.graph = graph;
    }


    public SetPointsContainer getPointsContainer (){
        return this.pointsContainer;
    }

    public TableView<Point> getTablePoints (){
        return this.tablePoints;
    }

    public Canvas getGraph (){
        return this.graph;
    }

    @FXML
    private void initialize(){
        try{
            fxmlLoader.setLocation(getClass().getResource("../fxml/alert.fxml"));
            fxmlAlert = fxmlLoader.load();
            controller = fxmlLoader.getController();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    TextField setxField;

    @FXML
    TextField setyField;

    private double x = 0;
    private double y = 0;

    public void setActualPoint(Point newPoint){
        actualPoint = newPoint;
        setxField.setText(actualPoint.getX()+"");
        setyField.setText(actualPoint.getY()+"");
    }

    public void okAction(javafx.event.ActionEvent actionEvent){
        double x;
        double y;
        try{
            x = Double.parseDouble(setxField.getText());
            y = Double.parseDouble(setyField.getText());
        } catch (NumberFormatException e){
            if (alertStage == null) {
                alertStage = new Stage();
                alertStage.setResizable(false);
                alertStage.setTitle("Error");
                alertStage.setScene(new Scene(fxmlAlert));
                alertStage.initModality(Modality.APPLICATION_MODAL);
                alertStage.initOwner(((Button)actionEvent.getSource()).getScene().getWindow());
            }
            alertStage.show();
            return;
        }
        actualPoint.setX(x);
        actualPoint.setY(y);
        tablePoints.refresh();
        Graph.redraw(graph,pointsContainer);
        cancelPressed(actionEvent);
    }

    public void cancelPressed(javafx.event.ActionEvent actionEvent){
        Node src = (Node)actionEvent.getSource();
        Stage stage = (Stage)src.getScene().getWindow();
        stage.hide();
    }
}
