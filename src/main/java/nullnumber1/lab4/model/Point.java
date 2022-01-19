package nullnumber1.lab4.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "points")
@Entity
public class Point extends BasicPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "x")
    private double x;

    @Column(name = "y")
    private double y;

    @Column(name = "r")
    private double r;

    @Column(name = "hit")
    private boolean hit;

    @Column(name = "initiator_id")
    private Long initiatorId;

    public void validate() {
        this.hit = isCircleHit() || isTriangleHit() || isRectangleHit();
    }

    private boolean isRectangleHit() {
        return x <= 0 && y <= 0 && x >= -r && y >= -r;
    }

    private boolean isCircleHit() {
        return x >= 0 && y <= 0 && (x*x + y*y <= r/2*r/2);
    }

    private boolean isTriangleHit() {
        return x >= 0 && y >= 0 && y <= 2 * -x + r;
    }

    @Override
    public String toString() {
        return "Point{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", r=" + r +
                ", hit=" + hit +
                ", initiatorId=" + initiatorId +
                '}';
    }
}
