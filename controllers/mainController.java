package controllers;

import interfaces.impls.SetPointsContainer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.Graph;
import sample.Point;

import java.io.IOException;
import java.text.NumberFormat;

public class mainController {
    private double old_x = 0;
    private double old_y = 0;

    private SetPointsContainer pointsContainer = new SetPointsContainer();
    @FXML
    MenuBar menubar;

    @FXML
    javafx.scene.control.Label coordsLabel;

    @FXML
    Canvas graph;

    @FXML
    private TableView<Point> tablePoints;

    @FXML
    private TableColumn<Point, Double> xColumn;

    @FXML
    private TableColumn<Point, Double> yColumn;

    private Parent fxmlEdit;
    private FXMLLoader fxmlLoader = new FXMLLoader();
    private editPointController controller;
    private Stage editPointStage;

    @FXML
    private void initialize(){

        pointsContainer.add(new Point(12.0,12.0,1));

        xColumn.setCellValueFactory(new PropertyValueFactory<>("X"));
        yColumn.setCellValueFactory(new PropertyValueFactory<>("Y"));
        tablePoints.setItems(pointsContainer.getSet());

        pointsContainer.getSet().addListener(new ListChangeListener<Point>() {
            @Override
            public void onChanged(Change<? extends Point> c) {
                System.out.println("changed!");
            }
        });

        try{
            fxmlLoader.setLocation(getClass().getResource("../fxml/editPoint.fxml"));
            fxmlEdit = fxmlLoader.load();
            controller = fxmlLoader.getController();
            controller.setTablePoints(tablePoints);
            controller.setGraph(graph);
            controller.setPointsContainer(pointsContainer);
        } catch (IOException e){
            e.printStackTrace();
        }

    }


    public void canvasClicked(MouseEvent e){
        Point point = new Point(Graph.XGraphtoReal(e.getX()),Graph.YGraphtoReal(e.getY()),pointsContainer.getSet().size()+1);
        Graph.drawPoint(graph, point);
        pointsContainer.add(point);
    }

    public void canvasMouseMoved(MouseEvent e){
        coordsLabel.setText("x : "+ Graph.XGraphtoReal(e.getX())+" y : "+Graph.YGraphtoReal(e.getY()));
    }

    public void clearDataClicked(javafx.event.ActionEvent actionEvent){
        pointsContainer.clear();
        Graph.erasePoints(graph);
        Graph.drawGrid(graph);
    }

    public void windowResized(Number newW, Number newH){
        graph.setWidth(newW.doubleValue() - tablePoints.getWidth() - 14);
        graph.setHeight(newH.doubleValue() - menubar.getHeight() - 14);
        Graph.setSizeX(graph.getWidth());
        Graph.setSizeY(graph.getHeight());
        Graph.erasePoints(graph);

        Graph.drawGrid(graph);
        Graph.drawPoints(graph, pointsContainer);
    }

    public void editPointClicked(javafx.event.ActionEvent actionEvent) throws IOException{
        Point selectedPoint = tablePoints.getSelectionModel().getSelectedItem();
        old_x = selectedPoint.getX();
        old_y = selectedPoint.getY();
        controller.setActualPoint(selectedPoint);

        if (editPointStage == null) {
            editPointStage = new Stage();
            editPointStage.setResizable(false);
            editPointStage.setTitle("Edit Point");
            editPointStage.setScene(new Scene(fxmlEdit));
            editPointStage.initModality(Modality.APPLICATION_MODAL);
            editPointStage.initOwner(((MenuItem) actionEvent.getSource()).getParentPopup().getScene().getWindow());
        }
        editPointStage.show();
    }
}
