import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

/**
 * This class defines a polygon.
 * @author hrkalona
 */
class Polygon  extends Shape implements Serializable {
  protected Point[] shape;   // An array of points.
  protected double rotation; // Zero degrees is due east.
  protected Point pull; // How much it moves per repaint.
  protected double acceleration; // The acceleration.
  protected Boolean filled; // Is that polygon needed to be filled in draw?


    /**
     * The constractor, Polygon, creates a polygon.
     * @param shape Its shape.
     * @param position Its position.
     * @param rotation Its rotation.
     * @param sColor Its color.
     * @param acceleration Its acceleration.
     * @param filled Is needed to be filled?
     */
    public Polygon(Point[] shape, Point position, double rotation, Color sColor, double acceleration, Boolean filled) {

        super(position, sColor);
        this.shape = shape;
        this.rotation = rotation;
        pull = new Point(0, 0);
        this.acceleration = acceleration;
        this.filled = filled;
    
        // First, we find the shape's top-most left-most boundary, its origin.
        Point origin = shape[0].clone();
        for (Point p : shape) {
            if (p.getX() < origin.getX()) origin.setX(p.getX());
            if (p.getY() < origin.getY()) origin.setY(p.getY());
        }
    
        // Then, we orient all of its points relative to the real origin.
        for (Point p : shape) {
            p.setX(p.getX() - origin.getX());
            p.setY(p.getY() - origin.getY());
        }

    }

    /**
     * The method, getPoints, applies the rotation and offset to the shape of the polygon.
     * @return An array of points.
     */
    public Point[] getPoints() {

        Point center = findCenter();
        Point[] points = new Point[shape.length];
	int i=0;
        for (Point p : shape) {
        double x = ((p.getX()-center.getX()) * Math.cos(Math.toRadians(rotation)))
                 - ((p.getY()-center.getY()) * Math.sin(Math.toRadians(rotation)))
                 + center.getX()/2 + position.getX();
        double y = ((p.getX()-center.getX()) * Math.sin(Math.toRadians(rotation)))
                 + ((p.getY()-center.getY()) * Math.cos(Math.toRadians(rotation)))
                 + center.getY()/2 + position.getY();
        points[i++] = new Point(x,y);
        }
        
        return points;

    }

    /**
     * The method, contains, checks if a point is inside another polygon.
     * @param point The point that needs to be checked
     * @return True if that point is contained, else false.
     */
    public boolean contains(Point point) {

        Point[] points = getPoints();
        double crossingNumber = 0;
        for (int i = 0, j = 1; i < shape.length; i++, j=(j+1)%shape.length) {
            if ((((points[i].getX() < point.getX()) && (point.getX() <= points[j].getX())) ||
            ((points[j].getX() < point.getX()) && (point.getX() <= points[i].getX()))) &&
            (point.getY() > points[i].getY() + (points[j].getY()-points[i].getY())/
            (points[j].getX() - points[i].getX()) * (point.getX() - points[i].getX()))) {
                crossingNumber++;
            }
        }

        return crossingNumber%2 == 1;

    }


    /**
     * The method, rotate, rotates a polygon.
     * @param degrees How many degrees to be rotated.
     */
    public void rotate(int degrees) {rotation = (rotation+degrees)%360;}
  
  
    /*
     * The method, findArea, implements some more magic math.
     */
    private double findArea() {
      double sum = 0;

        for (int i = 0, j = 1; i < shape.length; i++, j=(j+1)%shape.length) {
            sum += shape[i].getX()*shape[j].getY()-shape[j].getX()*shape[i].getY();
        }

        return Math.abs(sum/2);

    }

    /*
     * The method, findCenter, implements another bit of math.
     */
    private Point findCenter() {

        Point sum = new Point(0,0);
        for (int i = 0, j = 1; i < shape.length; i++, j=(j+1)%shape.length) {
            sum.setX(sum.getX() + (shape[i].getX() + shape[j].getX()) * (shape[i].getX() * shape[j].getY() - shape[j].getX() * shape[i].getY()));
            sum.setY(sum.getY() + (shape[i].getY() + shape[j].getY()) * (shape[i].getX() * shape[j].getY() - shape[j].getX() * shape[i].getY()));
                  
        }
        double area = findArea();

      return new Point(Math.abs(sum.getX()/(6*area)),Math.abs(sum.getY()/(6*area)));

    }

    /**
     * The method, getRotation, returns the polygon's rotation.
     * @return The polygon's rotation.
     */
    public double getRotation() {

        return rotation;

    }

    /**
     * The method, getAcceleration, returns the polygon's acceleration.
     * @return the polygon's acceleration.
     */
    public double getAcceleration() {

        return acceleration;

    }

    /**
     * The method, setAcceleration, sets a new acceleration to the current one.
     * @param acceleration The given acceleration.
     */
    public void setAcceleration(double acceleration) {

        this.acceleration = acceleration;

    }

    /**
     * The method, accelerate, computes the new shift of position.
     */
    public void accelerate() {

        pull.setX(pull.getX() + (acceleration * Math.cos(Math.toRadians(rotation))));
        pull.setY(pull.getY() + (acceleration * Math.sin(Math.toRadians(rotation))));

    }

    /**
     * The method, draw, draws a polygon depending if its filled or not.
     * @param brush The graphic that we are using to paint.
     */
    public void draw(Graphics brush) {
      int temp_length;

        brush.setColor(sColor);

        Point[] polygon = getPoints();
        temp_length = polygon.length;
        int[] xPoints = new int[temp_length];
        int[] yPoints = new int[temp_length];

        for(int i = 0; i < temp_length; i++) {
            xPoints[i] = (int)Math.round(polygon[i].getX());
            yPoints[i] = (int)Math.round(polygon[i].getY());
        }

        if(filled) {
            brush.fillPolygon(xPoints, yPoints, temp_length);
        }
        else {
            brush.drawPolygon(xPoints, yPoints, temp_length);
        }
        
    }

}
