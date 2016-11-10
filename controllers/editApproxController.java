package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Graph;
import sample.LeastSquares;
import sample.Point;

import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * Created by Zerbs on 02.11.2016.
 */
public class editApproxController {
    @FXML
    TextField setRangeField;

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

    public void initializeTextField(String str){
        setRangeField.setText(str);
    }

    public void okAction(javafx.event.ActionEvent actionEvent){
        int range;
        try {
            range = tryParseRangeField(actionEvent);
        } catch (NumberFormatException e){
            return;
        }
        LeastSquares.setRange(range);
        cancelPressed(actionEvent);
    }

    private int tryParseRangeField(javafx.event.ActionEvent actionEvent){
        int range = 0;
        try{
            range = Integer.parseInt(setRangeField.getText());
        } catch (NumberFormatException e){
            initializeAlert(actionEvent);
            alertStage.show();
            throw e;
        }
        if (range < 2){
            initializeAlert(actionEvent);
            alertStage.show();
            throw new NumberFormatException();
        }
        return range;
    }

    protected void initializeAlert(javafx.event.ActionEvent actionEvent){
        if (alertStage == null) {
            controller.setLabelText("Wrong range!");
            alertStage = new Stage();
            alertStage.setResizable(false);
            alertStage.setTitle("Error");
            alertStage.setScene(new Scene(fxmlAlert));
            alertStage.initModality(Modality.APPLICATION_MODAL);
            alertStage.initOwner(((Button)actionEvent.getSource()).getScene().getWindow());
        }
    }

    public void cancelPressed(javafx.event.ActionEvent actionEvent){
        Node src = (Node)actionEvent.getSource();
        Stage stage = (Stage)src.getScene().getWindow();
        stage.hide();
    }
}
