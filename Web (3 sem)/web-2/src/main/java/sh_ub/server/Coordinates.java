package sh_ub.server;

import java.time.*;
import java.util.Arrays;
import java.util.List;

public class Coordinates {
    private final double x;
    private final double y;
    private final double r;
    private final LocalDate time;
    private final boolean isHit;

    public Coordinates(double x, double y, double R) {
        this.x = x;
        this.y = y;
        this.r = R;
        time = LocalDate.now();
        isHit = calculate();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getR() {
        return r;
    }

    public LocalDate getTime() {
        return time;
    }
    
    public String getTimeString() {
        return time.toString();
    }

    public boolean isHit() {
        return isHit;
    }

    private boolean calculate() {
        double R = this.r;

        boolean inRectangle = (x <= R && x >= 0) && (y >= 0 && y <= R);
        boolean inQuarterCircle = (x <= 0 && y <= 0) && ((double)(x * x)  + y * y <= (R * R) / 4.0);
        boolean inTriangle = (x >= 0 && y <= 0) && (y >= (x - R/2));

        return inRectangle || inQuarterCircle || inTriangle;
    }

    public boolean isValidCoordinates() {
        List<Double> allowedY = Arrays.asList(-2.0, 1.5, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5, 2.0);
        boolean xOk = (-3 < x) && (x < 5);
        boolean yOk = allowedY.contains(y);
        boolean rOk = (2 < r) && (r < 5);
        return xOk && yOk && rOk;
    }
}
