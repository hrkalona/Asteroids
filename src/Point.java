import java.io.Serializable;

/**
 * This class defines a point.
 * @author hrkalona
 */
class Point implements Serializable {
  private double x; // The point's x.
  private double y; // The point's y.

    /**
     * The constractor, Point, creates a point.
     * @param x The given x.
     * @param x The given y.
     */
    public Point(double x, double y) {

        this.x = x;
	this.y = y;

    }

    /**
     * The method, clone, creates a copy of the current point to another instance of a point.
     * @return The copied point.
     */
    @Override
    public Point clone() {

        return new Point(x, y);

    }

    /**
     * The method, getX, returns this point's x.
     * @return The point's x.
     */
    public double getX() {

        return x;

    }

    /**
     * The method, getY, returns this point's y.
     * @return The point's y.
     */
    public double getY() {

        return y;

    }

    /**
     * The method, setX, sets the point's x into a given one.
     * @param x The given x.
     */
    public void setX(double x) {
        
        this.x = x;
        
    }

    /**
     * The method, setY, sets the point's y into a given one.
     * @param y The given y.
     */
    public void setY(double y) {
        
        this.y = y;
        
    }

}
