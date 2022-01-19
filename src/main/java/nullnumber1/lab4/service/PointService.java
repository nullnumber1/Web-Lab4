package nullnumber1.lab4.service;

import nullnumber1.lab4.model.Point;

import java.util.List;

public interface PointService {

    List<Point> getAllByUser(Long id);

    boolean save(Point point);
}
