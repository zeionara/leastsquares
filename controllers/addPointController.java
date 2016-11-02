package controllers;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Graph;
import sample.Point;

/**
 * Created by Zerbs on 02.11.2016.
 */
public class addPointController extends editPointController {
    @Override
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
        Point point = new Point(x,y,getPointsContainer().getSet().size()+1);
        Graph.drawPoint(getGraph(), point);
        getPointsContainer().add(point);
        cancelPressed(actionEvent);
    }
}
