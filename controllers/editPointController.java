package controllers;

import interfaces.impls.SetPointsContainer;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sample.Graph;
import sample.Point;

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

    public void setPointsContainer (SetPointsContainer pointsContainer){
        this.pointsContainer = pointsContainer;
    }

    public void setTablePoints (TableView<Point> pointsData){
        this.tablePoints = pointsData;
    }

    public void setGraph (Canvas graph){
        this.graph = graph;
    }

    @FXML
    private void initialize(){
        okAddingPointButton.setDisable(true);
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
            return;
        }
        actualPoint.setX(x);
        actualPoint.setY(y);
        tablePoints.refresh();
        Graph.redraw(graph,pointsContainer);
        //redrawGraph();
        cancelPressed(actionEvent);
    }

    private boolean isNumericChar(String s){
        if ((s.charAt(0) != '0') && (s.charAt(0) != '1') && (s.charAt(0) != '2') && (s.charAt(0)!= '3')
                && (s.charAt(0) != '4') && (s.charAt(0) != '5') && (s.charAt(0) != '6') && (s.charAt(0) != '7') && (s.charAt(0) != '8') &&
                (s.charAt(0) != '9') && (s.charAt(0) != '.')){
            return false;
        }
        return true;
    }

    public void newxCoordinateChanged(KeyEvent keyEvent){
        try{
            System.out.println(setxField.getText()+keyEvent.getText());
            x = Double.parseDouble(setxField.getText()+keyEvent.getText());
            ((Node)setxField).setStyle("-fx-text-inner-color: black;");
            tryDisable();
        } catch (NumberFormatException e){
            ((Node)setxField).setStyle("-fx-text-inner-color: red;");
            okAddingPointButton.setDisable(true);
        }
    }
    private void tryDisable(){
        try{
            x = Double.parseDouble(setxField.getText());
            y = Double.parseDouble(setyField.getText());
        } catch (NumberFormatException e){
            return;
        }
        if ((setxField.getText().length() != 0) && (setyField.getText().length() != 0)){
            okAddingPointButton.setDisable(false);
        }
    }

    public void newyCoordinateChanged(KeyEvent keyEvent){
        try{
            System.out.println(setyField.getText()+keyEvent.getText());
            y = Double.parseDouble(setyField.getText()+keyEvent.getText());
            ((Node)setyField).setStyle("-fx-text-inner-color: black;");
            tryDisable();

        } catch (NumberFormatException e){
            ((Node)setyField).setStyle("-fx-text-inner-color: red;");
            okAddingPointButton.setDisable(true);
        }
    }

    public void cancelPressed(javafx.event.ActionEvent actionEvent){
        Node src = (Node)actionEvent.getSource();
        Stage stage = (Stage)src.getScene().getWindow();
        stage.hide();

    }

}
