package sample;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Created by Zerbs on 29.10.2016.
 */
public class Point {

    private int num = 0;
    private SimpleDoubleProperty X = new SimpleDoubleProperty(0);
    private SimpleDoubleProperty Y = new SimpleDoubleProperty(0);

    public Point(double X, double Y, int num) {
        this.X.set(X);
        this.Y.set(Y);
        this.num = num;
    }

    public void setX(double X){
        this.X.set(X);
    }
    public void setY(double Y){
        this.Y.set(Y);
    }
    public int getNum(){
        return num;
    }
    public void setNum(int newNum){
        num = newNum;
    }

    public double getX(){
        return X.getValue();
    }
    public double getY(){
        return Y.getValue();
    }

    public double getGraphicalX(){
        return (Graph.getSizeX()/2 + X.getValue()/Graph.getScale());
    }
    public double getGraphicalY(){
        return (Graph.getSizeY()/2 - Y.getValue()/Graph.getScale());
    }

    public DoubleProperty xProperty() {return X;}
    public DoubleProperty yProperty() {return Y;}

    public String toString(){
        return " x : "+X+" y : "+Y;
    }

    public boolean equals(Object obj){
        Point point = (Point)obj;
        if ((point.getY() == getY()) && (point.getX() == getX())){
            return true;
        }
        return false;
    }
}
