package controllers;

import interfaces.PointsContainer;
import interfaces.impls.SetPointsContainer;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.DiffEquations;
import sample.Graph;
import sample.LeastSquares;
import sample.Point;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class mainController {
    private double[] lastbadsolution;
    private double[] lastgoodsolution;
    private SetPointsContainer pointsContainer = new SetPointsContainer();

    @FXML
    AnchorPane sourceFieldsPane;

    @FXML
    TextField equationField;

    @FXML
    TextField x0Field;

    @FXML
    TextField y0Field;

    @FXML
    TextField epsilonField;

    @FXML
    Canvas graph;

    private Parent fxmlAlert;
    private FXMLLoader fxmlLoader3 = new FXMLLoader();
    private alertController controller3;
    private Stage alertStage;
    @FXML
    private void initialize(){
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
        new Thread(() -> buildButtonClicked(new ActionEvent())).start();
    }

    public void windowResized(Number newW, Number newH){
        graph.setWidth(newW.doubleValue());
        graph.setHeight(newH.doubleValue() - sourceFieldsPane.getHeight() - 14);

        Graph.setSizeX(graph.getWidth());
        Graph.setSizeY(graph.getHeight());

        Graph.redraw(graph,pointsContainer);
        if (lastbadsolution != null){
            Graph.drawPolynomialCurve(graph,lastbadsolution,1000,Color.RED);
            Graph.drawPolynomialCurve(graph,lastgoodsolution,1000,Color.GREEN);
        }
        buildButtonClicked(new ActionEvent());
    }

    public void showAlert(String str){
        if (alertStage == null) {
            initializeAlertStage();
        }

        controller3.setLabelText(str);
        alertStage.show();
    }

    //Initializations


    private void initializeAlertStage(){
        alertStage = new Stage();
        alertStage.setResizable(false);
        alertStage.setTitle("Error");
        alertStage.setScene(new Scene(fxmlAlert));
        alertStage.initModality(Modality.APPLICATION_MODAL);
        alertStage.initOwner(sourceFieldsPane.getScene().getWindow());
    }

    private void initializeAdditiveWindows(){
        try{
            fxmlLoader3.setLocation(getClass().getResource("../fxml/alert.fxml"));
            fxmlAlert = fxmlLoader3.load();
            controller3 = fxmlLoader3.getController();
        } catch (IOException e){
            e.printStackTrace();
        }
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
        //tablePoints.refresh();
        solution = LeastSquares.getKoefficients(pointsContainer);
        pointsContainer.add(sillyDot);
        //tablePoints.refresh();
        lastgoodsolution = solution;
        //Graph.redraw(graph,pointsContainer);
        Graph.drawPolynomialCurve(graph,lastbadsolution,1000,Color.RED);
        Graph.drawPolynomialCurve(graph,lastgoodsolution,1000,Color.GREEN);
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

    public void buildButtonClicked(ActionEvent actionEvent) {
        double x0 = 0;
        double y0 = 0;
        double epsilon = 0;
        String equation = null;
        try {
            equation = equationField.getText();
            x0 = Double.parseDouble(x0Field.getText());
            y0 = Double.parseDouble(y0Field.getText());
            epsilon = Double.parseDouble(epsilonField.getText());
        } catch (Exception e){
            showAlert("Illegal input!");
            return;
        }
        ArrayList<Point> points = DiffEquations.solveEquation(equation,x0,y0,epsilon);
        Graph.drawCurve(graph,points,Color.GREEN);
    }
}
