package nullnumber1.lab4.repository;

import nullnumber1.lab4.model.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> getAllByInitiatorId(Long initiatorId);
}
