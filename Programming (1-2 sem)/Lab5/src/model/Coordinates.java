package model;

public class Coordinates{
    private Double x; //Значение поля должно быть больше -605, Поле не может быть null
    private Float y; //Максимальное значение поля: 243, Поле не может быть null

    public Coordinates(double x, float y) {
        this.x = x;
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "("+x + "; " + y+")";
    }
}
