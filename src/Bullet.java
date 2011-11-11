import java.awt.Color;
import java.io.Serializable;

/**
 * This class defines a bullet.
 * @author hrkalona
 */
public class Bullet extends Circle implements Serializable {
  private double rotation; // Its rotation.
  private Boolean active = false; // Is it inside the window?

    /**
     * The constractor, Bullet, creates a bullet.
     * @param position Its position.
     * @param rotation Its rotation.
     */
    public Bullet(Point position, double rotation) {

        super(position, 4, Color.RED);
        this.rotation = rotation;
        active = true;

    }

    /**
     * The method, move, moves a bullet depending on its fired direction.
     */
    public void move() {

        position.setX(position.getX() + Math.cos(Math.toRadians(rotation)) * 6);
        position.setY(position.getY() + Math.sin(Math.toRadians(rotation)) * 6);

        if(position.getX() < -6 || position.getX() > 806 || position.getY() < -6 || position.getY() > 606) {

            active = false;
            
        }

    }

    /**
     * The method, getActivity, returns the status of the bullet.
     * @return True if the bullet is still in the screen, else false.
     */
    public Boolean getActivity() {

        return active;

    } 

}
