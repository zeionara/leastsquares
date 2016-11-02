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
import sample.LeastSquares;
import sample.Point;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;

public class mainController {
    private double[] lastsolution;
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

    private Parent fxmlApprox;
    private FXMLLoader fxmlLoader4 = new FXMLLoader();
    private editApproxController controller4;
    private Stage approxStage;
    @FXML
    private void initialize(){
        pointsContainer.add(new Point(0.75,2.5,1));
        pointsContainer.add(new Point(1.5,1.2,2));
        pointsContainer.add(new Point(2.25,1.12,3));
        pointsContainer.add(new Point(3,2.25,4));
        pointsContainer.add(new Point(3.75,4.28,2));
        initializeTableView();
        initializeAdditiveWindows();
    }

    //Handlers for the events

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
        lastsolution = null;
    }

    public void windowResized(Number newW, Number newH){
        graph.setWidth(newW.doubleValue() - tablePoints.getWidth() - 14);
        graph.setHeight(newH.doubleValue() - menubar.getHeight() - 14);

        Graph.setSizeX(graph.getWidth());
        Graph.setSizeY(graph.getHeight());

        Graph.redraw(graph,pointsContainer);
        if (lastsolution != null){
            Graph.drawPolynomialCurve(graph,lastsolution,1000);
        }
    }

    public void editPointClicked(javafx.event.ActionEvent actionEvent) throws IOException{
        Point selectedPoint = tablePoints.getSelectionModel().getSelectedItem();
        if (selectedPoint == null){
            showAlert("Nothing to edit, point not selected");
            return;
        }
        controller.setActualPoint(selectedPoint);
        if (editPointStage == null) {
            initializeEditPointStage(actionEvent);
        }
        editPointStage.show();
    }

    public void addPointClicked(javafx.event.ActionEvent actionEvent) throws IOException{
        if (addPointStage == null) {
            initializeAddPointStage(actionEvent);
        }
        addPointStage.show();
    }
    public void editApproxClicked(javafx.event.ActionEvent actionEvent) throws IOException{
        if (approxStage == null) {
            initializeApproxStage();
        }
        approxStage.show();
    }


    public void deletePointClicked(javafx.event.ActionEvent actionEvent) throws IOException{
        Point selectedPoint = tablePoints.getSelectionModel().getSelectedItem();
        if (selectedPoint == null){
            showAlert("Nothing to delete, point not selected");
            return;
        }
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

    public void showAlert(String str){
        if (alertStage == null) {
            initializeAlertStage();
        }

        controller3.setLabelText(str);
        alertStage.show();
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

    //Initializations

    private void initializeEditPointStage(javafx.event.ActionEvent actionEvent){
        editPointStage = new Stage();
        editPointStage.setResizable(false);
        editPointStage.setTitle("Edit Point");
        editPointStage.setScene(new Scene(fxmlEdit));
        editPointStage.initModality(Modality.APPLICATION_MODAL);
        editPointStage.initOwner(((MenuItem) actionEvent.getSource()).getParentPopup().getScene().getWindow());
    }

    private void initializeAddPointStage(ActionEvent actionEvent){
        addPointStage = new Stage();
        addPointStage.setResizable(false);
        addPointStage.setTitle("Add Point");
        addPointStage.setScene(new Scene(fxmlAdd));
        addPointStage.initModality(Modality.APPLICATION_MODAL);
        addPointStage.initOwner(((MenuItem) actionEvent.getSource()).getParentPopup().getScene().getWindow());
    }

    private void initializeAlertStage(){
        alertStage = new Stage();
        alertStage.setResizable(false);
        alertStage.setTitle("Error");
        alertStage.setScene(new Scene(fxmlAlert));
        alertStage.initModality(Modality.APPLICATION_MODAL);
        alertStage.initOwner(menubar.getScene().getWindow());
    }

    private void initializeApproxStage(){
        approxStage = new Stage();
        approxStage.setResizable(false);
        approxStage.setTitle("Approximating");
        approxStage.setScene(new Scene(fxmlAlert));
        approxStage.initModality(Modality.APPLICATION_MODAL);
        approxStage.initOwner(menubar.getScene().getWindow());
    }

    private void initializeAdditiveWindows(){
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

            fxmlLoader4.setLocation(getClass().getResource("../fxml/editApprox.fxml"));
            fxmlAlert = fxmlLoader4.load();
            controller4 = fxmlLoader4.getController();
            controller4.initializeTextField(LeastSquares.getRange()+"");
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void initializeTableView(){
        xColumn.setCellValueFactory(new PropertyValueFactory<>("X"));
        yColumn.setCellValueFactory(new PropertyValueFactory<>("Y"));
        tablePoints.setItems(pointsContainer.getSet());
    }

    public void buildApprox(ActionEvent actionEvent) {
        double[] solution = LeastSquares.getKoefficients(pointsContainer,10);
        lastsolution = solution;
        Graph.redraw(graph,pointsContainer);
        Graph.drawPolynomialCurve(graph,solution,1000);
    }
}
