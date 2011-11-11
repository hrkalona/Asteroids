import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;


/**
 * This class defines a shape.
 * @author hrkalona
 */
public abstract class Shape implements Serializable {
  protected Point position; // Its position.
  protected Color sColor; // Its color.

    /**
     * The constractor, Shape, creates a shape.
     * @param position Its position.
     * @param sColor Its color.
     */
    public Shape(Point position, Color sColor) {

        this.position = position;
        this.sColor = sColor;

    }

    /**
     * The method, draw, draws a shape.
     * @param brush The graphic that we are using to paint.
     */
    public abstract void draw(Graphics brush);

    /**
     * The method, getPosition, returns this shape's position.
     * @return The shape's position.
     */
    public Point getPosition() {

        return position;

    }

    /**
     * The method, setColor, changes this shape's color.
     * @param sColor The given color.
     */
    public void setColor(Color sColor) {

        this.sColor = sColor;
        
    }

    /**
     * The method, getColor, returns this shape's color.
     * @return The shape's color.
     */
    public Color getColor() {

        return sColor;
        
    }

}
