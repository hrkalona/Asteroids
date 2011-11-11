import java.awt.Color;
import java.io.Serializable;

/**
 * This class defines a ship.
 * @author hrkalona
 */
public class Ship extends Polygon implements Serializable {

    /**
     * The constractor, Ship, creates a ship.
     * @param shape Its shape.
     * @param position Its position.
     * @param rotation Its rotation.
     */
    public Ship(Point[] shape, Point position, double rotation) {

        super(shape, position, rotation, Color.MAGENTA, 0, true);

    }


    /**
     * The method, move, moves a ship depending on the given direction.
     * @param direction What type of movement.
     */
    public void move(Direction direction) {

        switch(direction) {
	    case UP:
                position.setX(position.getX() + pull.getX()); // move.
                position.setY(position.getY() + pull.getY());

                if(position.getY() < -15) {   // check if out of bounds and reset.
                    position.setY(position.getY() + 625);
                }

                if(position.getY() > 620) {
                    position.setY(position.getY() - 632);
                }

                if(position.getX() < -25) {
                    position.setX(position.getX() + 845);
                }

                if(position.getX() > 825) {
                    position.setX(position.getX() - 845);
                }
		break;
	    case RIGHT:
                rotate(15);  // rotate
		break;
	    case LEFT:
                rotate(-15); // rotate
                break;
        }

    }

}
