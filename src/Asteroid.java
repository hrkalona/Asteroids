import java.awt.Color;
import java.io.Serializable;
import java.util.Random;

/**
 * This class defines an asteroid.
 * @author hrkalona
 */
public class Asteroid extends Polygon implements Serializable {
  private int exploded; // How many times was this asteroid exploded.
  private int rotation_direction; // Which way it rotates.

    /**
     * The constractor, Asteroid, creates an asteroid.
     * @param shape Its shape.
     * @param position Its position.
     * @param rotation Its rotation.
     * @param exploded How many times was exploded.
     * @param acceleration Its acceleration.
     */
    public Asteroid(Point[] shape, Point position, double rotation, int exploded, double acceleration) {

        super(shape, position, rotation, Color.WHITE, acceleration, false);
        this.exploded = exploded;
        pull.setX(Math.cos(Math.toRadians(rotation)) * acceleration); // compute its movement, this will be the same forever!
        pull.setY(Math.sin(Math.toRadians(rotation)) * acceleration);

        Random generator = new Random(System.currentTimeMillis());
        rotation_direction = 1 + generator.nextInt(2);  // find the rotation speed and the rotation direction, those will be the same forever!
        if(generator.nextBoolean()) {
            rotation_direction *= -1;
        }

    }

    /**
     * How many times was the asteroid exploded.
     * @return The number of times that the asteroid was exploded.
     */
    public int getExplosionTimes() {

        return exploded;

    }


    /**
     * The method, move, moves an asteroid depending on its computed direction.
     */
    public void move() {

        rotate(rotation_direction);
        position.setX(position.getX() + pull.getX());
        position.setY(position.getY() + pull.getY());

        if(position.getY() < -40) {
            position.setY(position.getY() + 645);
        }

        if(position.getY() > 645) {
            position.setY(position.getY() - 685);
        }

        if(position.getX() < -45) {
            position.setX(position.getX() + 845);
        }

        if(position.getX() > 845) {
            position.setX(position.getX() - 885);
        }

    }


}
