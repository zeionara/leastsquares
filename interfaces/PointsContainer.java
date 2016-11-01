package interfaces;

import sample.Point;

/**
 * Created by Zerbs on 01.11.2016.
 */
public interface PointsContainer{
    void add (Point point);
    void delete (Point point);
    void update (Point point);
    void clear();
}
