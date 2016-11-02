package controllers;

import interfaces.impls.SetPointsContainer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.Graph;
import sample.Point;

import javax.imageio.ImageIO;
import java.io.File;
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

    private Parent fxmlAdd;
    private FXMLLoader fxmlLoader2 = new FXMLLoader();
    private editPointController controller2;
    private Stage addPointStage;

    private Parent fxmlAlert;
    private FXMLLoader fxmlLoader3 = new FXMLLoader();
    private alertController controller3;
    private Stage alertStage;

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

            fxmlLoader2.setLocation(getClass().getResource("../fxml/addPoint.fxml"));
            fxmlAdd = fxmlLoader2.load();
            controller2 = fxmlLoader2.getController();
            controller2.setTablePoints(tablePoints);
            controller2.setGraph(graph);
            controller2.setPointsContainer(pointsContainer);

            fxmlLoader3.setLocation(getClass().getResource("../fxml/alert.fxml"));
            fxmlAlert = fxmlLoader3.load();
            controller3 = fxmlLoader3.getController();
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
        if (selectedPoint == null){
            showAlert("Nothing to edit, point not selected");
            return;
        }

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

    public void addPointClicked(javafx.event.ActionEvent actionEvent) throws IOException{
        if (addPointStage == null) {
            addPointStage = new Stage();
            addPointStage.setResizable(false);
            addPointStage.setTitle("Add Point");
            addPointStage.setScene(new Scene(fxmlAdd));
            addPointStage.initModality(Modality.APPLICATION_MODAL);
            addPointStage.initOwner(((MenuItem) actionEvent.getSource()).getParentPopup().getScene().getWindow());
        }
        addPointStage.show();
    }

    public void deletePointClicked(javafx.event.ActionEvent actionEvent) throws IOException{
        Point selectedPoint = tablePoints.getSelectionModel().getSelectedItem();
        if (selectedPoint == null){
            showAlert("Nothing to delete, point not selected");
            return;
        }
        if (selectedPoint != null){
            System.out.println("deleted");
            int numOfDeletedPoint = selectedPoint.getNum();
            pointsContainer.delete(selectedPoint);
            for(Point point : pointsContainer.getSet()){
                if (point.getNum() > numOfDeletedPoint){
                    point.setNum(point.getNum()-1);
                }
            }

            tablePoints.refresh();
            Graph.redraw(graph,pointsContainer);
        }
    }

    public void showAlert(String str){
            if (alertStage == null) {
                alertStage = new Stage();
                alertStage.setResizable(false);
                alertStage.setTitle("Error");
                alertStage.setScene(new Scene(fxmlAlert));
                alertStage.initModality(Modality.APPLICATION_MODAL);
                alertStage.initOwner(menubar.getScene().getWindow());
            }

            controller3.setLabelText(str);
            alertStage.show();
            return;
        }

    public void saveClicked(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();//Класс работы с диалогом выборки и сохранения
        fileChooser.setTitle("Select File");//Заголовок диалога
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");//Расширение
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(((MenuItem) actionEvent.getSource()).getParentPopup().getScene().getWindow());//Указываем текущую сцену CodeNote.mainStage
        if (file == null){
            return;
        }

        WritableImage wim = new WritableImage((int)graph.getWidth(), (int)graph.getHeight());
        graph.snapshot(null,wim);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
        } catch (Exception s) {
        }
    }
}
