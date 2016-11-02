package controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Created by Zerbs on 02.11.2016.
 */
public class alertController {
    @FXML
    Label alertInfo;

    public void okPressed(MouseEvent actionEvent){
        Node src = (Node)actionEvent.getSource();
        Stage stage = (Stage)src.getScene().getWindow();
        stage.hide();
    }

    public void setLabelText(String labelText){
        alertInfo.setText(labelText);
    }


}
