import java.io.Serializable;


/**
 * This class defines a  High Score.
 * @author hrkalona
 */
public class HighScore implements Serializable {
  private int level; // The level.
  private int score; // The score.
  private String name; // The name.

    /**
     * The constractor, HighScore, creates a high score.
     * @param level The given level.
     * @param score The given score.
     * @param name The given name.
     */
    public HighScore(int level, int score, String name) {

        this.level = level;
        this.score = score;
        this.name = name;

    }

    /**
     * The method, getScore, returns the score of that instance.
     * @return The score.
     */
    public int getScore() {

        return score;

    }

    /**
     * The method, toString, creates a string of the level, the score, and the name.
     * @return A string.
     */
    @Override
    public String toString() {

        return "    " + String.format("%03d", level) + "    " + String.format("%06d", score) + "    " + name;


    }

}
