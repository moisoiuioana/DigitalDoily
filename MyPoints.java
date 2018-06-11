import java.awt.*;


// Create a customised point to draw in the drawing area
public class MyPoints {

    // Each MyPoints object holds the location of a point in a two-dimensional plane
    private double x;
    private double y;
    // Each MyPoints object has a set color
    private Color color;
    // Each MyPoints object has a set stroke
    private BasicStroke stroke;

    // Each MyPoints object has a reflection mode
    private boolean reflectionMode;

    // Constructor
    public MyPoints(Point point, Color color, BasicStroke stroke, boolean reflectionMode){
        setX(point.getX());
        setY(point.getY());
        setColor(color);
        setStroke(stroke);
        setReflectionMode(reflectionMode);
    }

    // Getters and setters
    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setColor(Color color) { this.color = color; }

    public Color getColor() {
        return color;
    }

    public void setStroke(BasicStroke stroke) { this.stroke = stroke; }

    public BasicStroke getStroke() {
        return stroke;
    }

    public boolean reflectionMode() { return reflectionMode; }

    public void setReflectionMode(boolean reflectionMode) { this.reflectionMode = reflectionMode; }
}
