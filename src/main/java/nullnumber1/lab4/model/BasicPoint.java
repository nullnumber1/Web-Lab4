package nullnumber1.lab4.model;

import lombok.Data;

@Data
public class BasicPoint {
    private double x;
    private double y;
    private double r;
    private boolean hit;

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
        return "BasicPoint{" +
                "x=" + x +
                ", y=" + y +
                ", r=" + r +
                ", hit=" + hit +
                '}';
    }
}
