import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

/**
 * This class defines a circle.
 * @author hrkalona
 */
public class Circle extends Shape implements Serializable {
  protected int radius; // Its radius.

    /**
     * The constractor, Circle, creates a circle.
     * @param position Its position.
     * @param radius Its radius.
     * @param sColor Its color.
     */
    public Circle(Point position, int radius, Color sColor) {

        super(position, sColor);
        this.radius = radius;

    }

    /**
     * The method, draw, draws a circle.
     * @param brush The graphic that we are using to paint.
     */
    public void draw(Graphics brush) {

        brush.setColor(sColor);
        brush.fillOval((int)Math.round(position.getX()), (int)Math.round(position.getY()), radius, radius);

    }

}
