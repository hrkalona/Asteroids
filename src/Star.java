import java.awt.Color;
import java.io.Serializable;


/**
 * This class defines a star.
 * @author hrkalona
 */
public class Star extends Circle implements Serializable {

    /**
     * The constractor, Star, creates a star.
     * @param position Its position.
     * @param radius Its radius.
     */
    public Star(Point position, int radius) {

        super(position, radius, Color.WHITE);

    }

}
