package interfaces.impls;

import interfaces.PointsContainer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import sample.Point;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Zerbs on 01.11.2016.
 */
public class SetPointsContainer implements PointsContainer{

    private ObservableList<Point> points = FXCollections.observableArrayList();

    @Override
    public void update(Point point) {
        throw new NotImplementedException();
    }

    @Override
    public void add(Point point) {
        points.add(point);
    }

    @Override
    public void delete(Point point) {
        points.remove(point);
    }

    public ObservableList<Point> getSet(){
        return points;
    }

    @Override
    public void clear() {
        points.clear();
    }
}
