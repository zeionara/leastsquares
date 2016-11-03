package sample;

import interfaces.impls.SetPointsContainer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

/**
 * Created by Zerbs on 01.11.2016.
 */
public abstract class Graph {
    public static final double SIZE_OF_POINT = 3;
    public static final double SIZE_OF_TAG = 5;
    public static final double OFFSET_TO_TAG = 10;
    public static final double SCALE_DELTA = 0.05;

    private static double scale = 1d;
    private static double sizeX = 2000d;
    private static double sizeY = 2000d;

    public static void setScale(double scale){
        Graph.scale = scale;
    }

    public static double getScale(){
        return Graph.scale;
    }

    public static double YGraphtoReal(double y){
        return (Graph.getSizeY()/2 - y)*scale;
    }
    public static double XGraphtoReal(double x){
        return (x - Graph.getSizeX()/2)*scale;
    }

    public static void drawPoints(Canvas canvas, SetPointsContainer pointsContainer){
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        for (Point point : pointsContainer.getSet()){
            graphicsContext.fillOval(point.getGraphicalX()-SIZE_OF_POINT/2,point.getGraphicalY()-SIZE_OF_POINT/2,SIZE_OF_POINT,SIZE_OF_POINT);
            graphicsContext.fillText(point.getNum()+"",point.getGraphicalX(),point.getGraphicalY());
        }
    }

    public static void erasePoints(Canvas canvas){
        canvas.getGraphicsContext2D().clearRect(0,0,sizeX,sizeY);
        Graph.drawGrid(canvas);
    }

    public static void drawPoint(Canvas canvas, Point point){
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        graphicsContext.fillOval(point.getGraphicalX()-Graph.SIZE_OF_POINT/2,
                                    point.getGraphicalY()-Graph.SIZE_OF_POINT/2,
                                    Graph.SIZE_OF_POINT, Graph.SIZE_OF_POINT);
        graphicsContext.fillText(point.getNum()+"",point.getGraphicalX(),point.getGraphicalY());
    }

    public static void drawGrid(Canvas canvas){
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        graphicsContext.setLineWidth(0.5);
        graphicsContext.strokeLine(sizeX/2,sizeY,sizeX/2,0);
        graphicsContext.strokeLine(sizeX,sizeY/2,0,sizeY/2);

        Font oldFont = graphicsContext.getFont();
        graphicsContext.setFont(new Font("Arial",7));
        double pointy = -sizeY/2*scale;
        while (pointy < sizeY/2*scale){
            drawTagY(canvas,new Point(0,pointy,0));
            pointy+=sizeY/20*scale;
        }
        pointy = -sizeX/2*scale;
        while (pointy < sizeX/2*scale){
            drawTagX(canvas,new Point(pointy,0,0));
            pointy+=sizeX/20*scale;
        }
        graphicsContext.setFont(oldFont);
    }

    public static void drawTagY(Canvas canvas, Point point){
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        graphicsContext.setLineWidth(0.5);
        graphicsContext.strokeLine(sizeX/2-SIZE_OF_TAG/2,point.getGraphicalY(),sizeX/2+SIZE_OF_TAG/2,point.getGraphicalY());
        graphicsContext.fillText((int)point.getY()+"",point.getGraphicalX()+OFFSET_TO_TAG,point.getGraphicalY());
    }

    public static void drawTagX(Canvas canvas, Point point){
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        graphicsContext.setLineWidth(0.5);
        graphicsContext.strokeLine(point.getGraphicalX(),sizeY/2-SIZE_OF_TAG/2,point.getGraphicalX(),sizeY/2+SIZE_OF_TAG/2);
        graphicsContext.fillText((int)point.getX()+"",point.getGraphicalX(),point.getGraphicalY()+OFFSET_TO_TAG);
    }

    public static void redraw(Canvas canvas, SetPointsContainer pointsContainer){
        Graph.erasePoints(canvas);
        Graph.drawPoints(canvas, pointsContainer);
    }

    public static void drawPolynomialCurve(Canvas canvas, double[] solution,int accuracy,Paint color){
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        Paint fill = graphicsContext.getFill();
        graphicsContext.setFill(color);
        double pointx = -sizeX/2*scale;
        double pointy = 0;
        Point tmpoint;
        while (pointx < sizeX/2*scale){
            pointy = 0.0;
            for (int i = 0; i < solution.length; i++){
                pointy += solution[i]*Math.pow(pointx,i);
            }
            tmpoint = new Point(pointx,pointy,0);
            graphicsContext.fillOval(tmpoint.getGraphicalX()-Graph.SIZE_OF_POINT/4,
                    tmpoint.getGraphicalY()-Graph.SIZE_OF_POINT/4,
                    Graph.SIZE_OF_POINT/2, Graph.SIZE_OF_POINT/2);
            pointx+=sizeX/accuracy*scale;
        }
        graphicsContext.setFill(fill);
    }

    public static  double getSizeX(){
        return sizeX;
    }

    public static double getSizeY(){
        return sizeY;
    }

    public static void setSizeX(double newSize){
        sizeX = newSize;
    }

    public static void setSizeY(double newSize){
        sizeY = newSize;
    }
}
