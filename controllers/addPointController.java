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
        double[] coordinates;
        try {
            coordinates = tryParseXY(actionEvent);
        } catch (NumberFormatException e){
            return;
        }

        Point point = new Point(coordinates[0],coordinates[1],getPointsContainer().getSet().size()+1);
        Graph.drawPoint(getGraph(), point);
        getPointsContainer().add(point);
        cancelPressed(actionEvent);
    }
}
