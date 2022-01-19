package nullnumber1.lab4.service.impl;

import lombok.extern.slf4j.Slf4j;
import nullnumber1.lab4.model.Point;
import nullnumber1.lab4.repository.PointRepository;
import nullnumber1.lab4.service.PointService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PointServiceImpl implements PointService {
    private final PointRepository pointRepository;

    public PointServiceImpl(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    @Override
    public List<Point> getAllByUser(Long id) {
        return pointRepository.getAllByInitiatorId(id);
    }

    @Override
    public boolean save(Point point) {
        pointRepository.save(point);
        return true;
    }
}
