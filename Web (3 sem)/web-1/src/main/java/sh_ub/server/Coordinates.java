package sh_ub.server;

import java.time.*;
import java.util.Arrays;
import java.util.List;

public class Coordinates {
    private final int x;
    private final double y;
    private final double r;
    private final LocalDate time;
    private final boolean isHit;

    public Coordinates(int x, double y, double R) {
        this.x = x;
        this.y = y;
        this.r = R;
        time = LocalDate.now();
        isHit = calculate();
    }

    public int getX() {
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

    public boolean isHit() {
        return isHit;
    }

    private boolean calculate() {
        double R = this.r;

        boolean inRectangle = (x >= -R && x <= 0) && (y >= 0 && y <= R);
        boolean inQuarterCircle = (x <= 0 && y <= 0) && ((double)(x * x)  + y * y <= (R * R) / 4.0);
        boolean inTriangle = (x >= 0 && y <= 0) && (y >= (x - R/2));

        return inRectangle || inQuarterCircle || inTriangle;
    }

    public boolean isValidCoordinates() {
        List<Integer> X = Arrays.asList(-4, -3,-2, -1, 0, 1, 2, 3, 4);
        List<Double> R = Arrays.asList(1., 1.5, 2., 2.5, 3.);
        return X.contains(x) && R.contains(r) && -3 < y && y  < 5 ;
    }
}
