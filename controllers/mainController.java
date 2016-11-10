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
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

public class mainController {
    private double[] lastbadsolution;
    private double[] lastgoodsolution;
    private SetPointsContainer pointsContainer = new SetPointsContainer();
    @FXML
    MenuBar menubar;

    @FXML
    MenuItem buildMenuItem;

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

    @FXML
    private Label badSolutionTextField;

    @FXML
    private Label goodSolutionTextField;

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
        pointsContainer.getSet().addListener(new ListChangeListener<Point>() {
            @Override
            public void onChanged(Change<? extends Point> c) {
                if (pointsContainer.getSet().size() < 2){
                    buildMenuItem.setDisable(true);
                } else {
                    buildMenuItem.setDisable(false);
                }
            }
        });
        initializeTableView();
        initializeAdditiveWindows();
    }

    //Handlers for the events

    public void canvasClicked(MouseEvent e){
        double x = new BigDecimal(Graph.XGraphtoReal(e.getX())).setScale(2, RoundingMode.UP).doubleValue();
        double y = new BigDecimal(Graph.YGraphtoReal(e.getY())).setScale(2, RoundingMode.UP).doubleValue();
        Point point = new Point(x,y,pointsContainer.getSet().size()+1);
        Graph.drawPoint(graph, point);
        pointsContainer.add(point);
    }

    public void canvasScrolled(ScrollEvent e){
        if ((e.getDeltaY() < 0)){
            Graph.setScale(Graph.getScale()+Graph.SCALE_DELTA);
            Graph.redraw(graph,pointsContainer);
            if (lastbadsolution != null){
                Graph.drawPolynomialCurve(graph,lastbadsolution,1000,Color.RED);
                Graph.drawPolynomialCurve(graph,lastgoodsolution,1000,Color.GREEN);
            }
        } else if (Graph.getScale() > Graph.SCALE_DELTA*2){
            Graph.setScale(Graph.getScale()-Graph.SCALE_DELTA);
            Graph.redraw(graph,pointsContainer);
            if (lastbadsolution != null){
                Graph.drawPolynomialCurve(graph,lastbadsolution,1000,Color.RED);
                Graph.drawPolynomialCurve(graph,lastgoodsolution,1000,Color.GREEN);
            }
        }
    }

    public void canvasMouseMoved(MouseEvent e){
        double x = new BigDecimal(Graph.XGraphtoReal(e.getX())).setScale(2, RoundingMode.UP).doubleValue();
        double y = new BigDecimal(Graph.XGraphtoReal(e.getY())).setScale(2, RoundingMode.UP).doubleValue();
        coordsLabel.setText("x : "+x+" y : "+y);
    }

    public void clearDataClicked(javafx.event.ActionEvent actionEvent){
        pointsContainer.clear();
        Graph.erasePoints(graph);
        lastbadsolution = null;
        lastgoodsolution = null;
    }

    public void windowResized(Number newW, Number newH){
        graph.setWidth(newW.doubleValue() - tablePoints.getWidth() - 14);
        graph.setHeight(newH.doubleValue() - menubar.getHeight() - 14);

        Graph.setSizeX(graph.getWidth());
        Graph.setSizeY(graph.getHeight());

        Graph.redraw(graph,pointsContainer);
        if (lastbadsolution != null){
            Graph.drawPolynomialCurve(graph,lastbadsolution,1000,Color.RED);
            Graph.drawPolynomialCurve(graph,lastgoodsolution,1000,Color.GREEN);
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
        double[] solution = LeastSquares.getKoefficients(pointsContainer);
        lastbadsolution = solution;
        Graph.redraw(graph,pointsContainer);
        Graph.drawPolynomialCurve(graph,solution,1000,Color.BLUE);

        double maximaldelta = 0;
        Point sillyDot = new Point(0,0,0);
        for (Point point : pointsContainer.getSet()){
            if (Math.abs(getY(solution,point.getX())-point.getY())>maximaldelta){
                maximaldelta = Math.abs(getY(solution,point.getX())-point.getY());
                sillyDot = point;
            }
        }
        pointsContainer.delete(sillyDot);
        tablePoints.refresh();
        solution = LeastSquares.getKoefficients(pointsContainer);
        pointsContainer.add(sillyDot);
        tablePoints.refresh();
        lastgoodsolution = solution;
        //Graph.redraw(graph,pointsContainer);
        Graph.drawPolynomialCurve(graph,lastbadsolution,1000,Color.RED);
        Graph.drawPolynomialCurve(graph,lastgoodsolution,1000,Color.GREEN);

        badSolutionTextField.setText(getStringRepresentation(lastbadsolution));
        goodSolutionTextField.setText(getStringRepresentation(lastgoodsolution));
    }

    private double getY(double[] solution,double x){
        double rezult = 0d;
        for (int i = 0; i < solution.length; i++){
            rezult += solution[i]*Math.pow(x,i);
        }
        return rezult;
    }

    private String getStringRepresentation(double[] solution){
        String result = "y = ";
        double cur = 0d;
        for (int i = 0; i < solution.length; i++){
            cur = new BigDecimal(solution[i]).setScale(2, RoundingMode.UP).doubleValue();
            if (cur > 0){
                result+="+";
            }
            if (i == 0){
                result += cur+"";
            } else if (i == 1){
                result += cur+"x";
            } else {
                result += cur+"x^"+i;
            }

        }
        return result;
    }
}
