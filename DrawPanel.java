import javax.swing.*;
import java.awt.*;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

// Creating the drawing area
public class DrawPanel extends JPanel{

    private ControlPanel controlPanel;
    private Double sectors;
    // points list holds all drawn points and lines, each in a list of customised points
    private List<List<MyPoints>> points;

    //Constructor
    public DrawPanel(){
        super();
        // Set the background color of the drawing panel to white
        setBackground(Color.WHITE);
        points = new ArrayList<>();
        // Add MouseListener and MouseMotionLister to allow drawing
        AddDrawingListener addDrawingListener = new AddDrawingListener();
        addMouseListener(addDrawingListener);
        addMouseMotionListener(addDrawingListener);
    }

    // Set the control panel
    public void setControlPanel(ControlPanel controlPanel){
        this.controlPanel = controlPanel;
    }

    // Get the points array size
    public int getPointsSize() { return  points.size(); }

    // Get the points array
    public java.util.List getPointsArray(){ return points; }

    // Call repaint on this panel
    public void drawPanelRepaint(){ repaint(); }

    // Paint panel
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Calculate the angle to split the ellipse
        sectors = controlPanel.getSectors();
        double delta = (double) 360 / sectors;

        // Translate the origin of the panel to the origin of the rectangle that represents the drawing panel
        g2d.translate(getWidth() / 2, getHeight() / 2);

        // For each list of points
        for (List list : points) {
            // Use another point which holds previous drawn point in case there is a line to draw
            MyPoints from = null;
            // For each point in the list
            for (Object aPoint : list) {
                double xCoordinate = ((MyPoints) aPoint).getX();
                double yCoordinate = ((MyPoints) aPoint).getY();
                g2d.setColor(((MyPoints) aPoint).getColor());
                g2d.setStroke(((MyPoints) aPoint).getStroke());
                // Draw a point
                if (from == null) {
                    // For each sector
                    for (int sect = 0; sect < sectors; sect++) {
                        // Draw a point by drawing a line from the same coordinates to the same coordinates; change coordinates to fit in the translated panel
                        Line2D line = new Line2D.Double(xCoordinate - getWidth() / 2, yCoordinate - getHeight() / 2, xCoordinate - getWidth() / 2, yCoordinate - getHeight() / 2);
                        g2d.draw(line);
                        // Reflect point if user chooses to reflect point
                        if (((MyPoints) aPoint).reflectionMode()) {
                            Line2D reflectedLine = new Line2D.Double(-xCoordinate + getWidth() / 2, yCoordinate - getHeight() / 2, -xCoordinate + getWidth() / 2, yCoordinate - getHeight() / 2);
                            g2d.draw(reflectedLine);
                        }
                        //Rotate each line to appear in every sector
                        g2d.rotate(Math.toRadians(delta));
                    }
                 // If multiple points are drawn, draw a line between each two points
                } else {
                    for (int sect = 0; sect < sectors; sect++) {
                        Line2D line = new Line2D.Double(from.getX() - getWidth() / 2, from.getY() - getHeight() / 2, xCoordinate - getWidth() / 2, yCoordinate - getHeight() / 2);
                        g2d.draw(line);
                        if (((MyPoints) aPoint).reflectionMode()) {
                            Line2D reflectedLine = new Line2D.Double(-from.getX() + getWidth() / 2, from.getY() - getHeight() / 2, -xCoordinate + getWidth() / 2, yCoordinate - getHeight() / 2);
                            g2d.draw(reflectedLine);
                        }
                        g2d.rotate(Math.toRadians(delta));
                    }
                }
                // Update the last drawn point
                from = (MyPoints) aPoint;
            }
        }

        // If line sectors are visible, draw all lines, otherwise not
        if (controlPanel.getToggle()) {
            // Calculate the radius, which represents each line
            int radius = Math.min(getWidth() / 2, getHeight() / 2);
            // For all number of sectors draw a line
            for (int i = 0; i < sectors; i++) {
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.drawLine(0, 0, 0, radius);
                // Rotate each line to the appropriate angle so that they are spread in a circular manner
                g2d.rotate(Math.toRadians(delta));
            }
        }

    }



    private class AddDrawingListener extends  MouseAdapter{

        // myPoints list is a temporary list which holds all the drawn points at once
        private java.util.List<MyPoints> myPoints;


        @Override
        public void mousePressed(MouseEvent e) {
            // When the user starts drawing a point or a line make a new list to hold the point(s)
            myPoints = new ArrayList<>();
            // Add the point(s) to the list holding all drawings
            points.add(myPoints);
            // Create a point
            MyPoints point = new MyPoints(e.getPoint(), controlPanel.getPenColor(), controlPanel.getStroke(), controlPanel.getReflectionMode());
            // If the eraser is selected make the color of the point the background color
            if(controlPanel.getEraserMode()){
               point.setColor(Color.WHITE);
            }
            // Add the point to the temporary list
            myPoints.add(point);
            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            // If it is a dragging event keep adding points to the temporary list (
            MyPoints point = new MyPoints(e.getPoint(), controlPanel.getPenColor(), controlPanel.getStroke(), controlPanel.getReflectionMode());
            if(controlPanel.getEraserMode()){
                point.setColor(Color.WHITE);
            }
            myPoints.add(point);
            repaint();
        }
    }
}
