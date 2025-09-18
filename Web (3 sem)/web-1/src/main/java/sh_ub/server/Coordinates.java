package sh_ub.server;

import java.time.*;

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
}
