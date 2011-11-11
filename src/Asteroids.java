import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import javax.swing.BoxLayout;

/**
 * Creates the classic arcade game, Asteroids, based on the idea of Dan Leyzberg and Art Simon
 * as described at Nifty Assignments session of 2008
 * @author hrkalona
 */
class Asteroids extends Game implements KeyListener {
  private static final int NUMBER_OF_STARS = 300; // The number of the stars.
  private static final int NUMBER_OF_ASTEROIDS = 14; // The max number of the asteroids.
  private static final int SHIP_POINTS = 4;  // The number of ship's points.
  private static final int ASTEROID_POINTS = 6; // The number of asteroid's points.
  private ArrayList<Asteroid> Asteroid_objects = new ArrayList<Asteroid>(); // The list of the active asteroids.
  private Star[] Star_objects; // The array of the stars.
  private ArrayList<Bullet> Bullets = new ArrayList<Bullet>(); // The list of the active bullets.
  private ArrayList<HighScore> High_scores = new ArrayList<HighScore>(NUMBER_OF_HIGH_SCORES); // The list of the high scores.
  private Ship ship; // The ship.
  private int Score; // The score.
  private int Lives; // The Lives.
  private int Level; // The Level.
  private long firing_time; // The time between two shots.
  private long drawing_time; // The time that GET READY, 5 , 4 , 3 , 2, 1, GO! will be painted.
  private long drawing_plus_score_time; // The time that the +score on the screen will be painted.
  private int plus_score; // The last score added to the score.
  private Point plus_score_position; // The position of the +score on the window.
  private long drawing_ship_color_time; // The time that the ship changes colors on a crash.
  private long drawing_critical_condition_time; // The time that critical condition is painted.
  private long drawing_save_time; // The time that saving! is painted.
  private long drawing_load_time; // The time that loading! is painted.
  private long drawing_gameover_time; // The time is needed to be waited in gameover state so the enter your name window pops up.
  private Boolean displayed_high_scores_registration; // The enter your name window was poped?
  private HighScore highscore; // The new highscore to be imported.
  private int result; // The result of the check on the high scores list.
  

    /**
     * The constractor, Asteroids, initializes all the game, creating the game's window, initialiazes parameters, such as the ship,
     * the first asteroids (according to level 1), the stars, the score, the lives,
     * some time parameters that are used for drawing, and loads the high scores, if any.
     */
    public Asteroids() {

        super("Asteroids!", 1024, 600);
        addKeyListener(this);

        firing_time = System.currentTimeMillis();
        Random generator = new Random(firing_time);

        Point[] star_ship = new Point[SHIP_POINTS]; // Create the ship's shape.
        star_ship[0] = new Point(0, 0);
        star_ship[1] = new Point(30, 15);
        star_ship[2] = new Point(0, 30);
        star_ship[3] = new Point(15, 15);

        ship = new Ship(star_ship, new Point(395, 265), 0.0);  // Where to appear.

        
        Point[][] Asteroid = new Point[NUMBER_OF_ASTEROIDS][ASTEROID_POINTS];  // Create the 14's asteroids shape.
        for(int i = 0; i < Asteroid.length; i++) {
            Asteroid[i][0] = new Point(10 + generator.nextInt(12), 50 + generator.nextInt(12));
            Asteroid[i][1] = new Point(60 + generator.nextInt(12), 60 + generator.nextInt(12));
            Asteroid[i][2] = new Point(60 + generator.nextInt(12), 10 + generator.nextInt(12));
            Asteroid[i][3] = new Point(30 + generator.nextInt(12), 35 + generator.nextInt(12));
            Asteroid[i][4] = new Point(10 + generator.nextInt(12), 10 + generator.nextInt(12));
            Asteroid[i][5] = new Point(-5 + generator.nextInt(12), 30 + generator.nextInt(12));
        }

        Point[] Position = new Point[NUMBER_OF_ASTEROIDS];  // Where those asteroids will appear.
        Position[0] = new Point(124, 312);
        Position[1] = new Point(402, 366);
        Position[2] = new Point(202, 67);
        Position[3] = new Point(456, 45);
        Position[4] = new Point(712, 237);
        Position[5] = new Point(74, 134);
        Position[6] = new Point(78, 423);
        Position[7] = new Point(501, 452);
        Position[8] = new Point(267, 167);
        Position[9] = new Point(297, 442);
        Position[10] = new Point(644, 87);
        Position[11] = new Point(533, 313);
        Position[12] = new Point(656, 411);
        Position[13] = new Point(487, 177);

        Level = 1;

        for(int i = 0; i < Level + 4; i++) {  // Add some of the asteroids to the list, depending the level.
            Asteroid Asteroid_structure = new Asteroid(Asteroid[i], Position[i], generator.nextInt(360), 0, (0.5 + generator.nextDouble())/2);
            Asteroid_objects.add(Asteroid_structure);
        }

        Star_objects = new Star[NUMBER_OF_STARS];
        for(int i = 0; i < Star_objects.length; i++) { // Add stars to the array.
            Star_objects[i] = new Star(new Point(generator.nextInt(800), generator.nextInt(600)), 1 + generator.nextInt(4));
        }

        Score = 0;
        Lives = 5;

        drawing_time = 0;   // Initialize some time parameters and some other stuff!

        drawing_plus_score_time = 0;
        plus_score = 0;
        plus_score_position = new Point(0, 0);

        drawing_ship_color_time = 0;

        drawing_critical_condition_time = 0;

        drawing_save_time = 0;
        drawing_load_time = 0;

        drawing_gameover_time = 0;

        displayed_high_scores_registration = false;

        result = NUMBER_OF_HIGH_SCORES;



        ObjectInputStream file = null;  // Load the High Scores list, if any.
        try {
            file = new ObjectInputStream(new FileInputStream("Highscores.dat"));

            High_scores = (ArrayList<HighScore>) file.readObject();


        }
        catch(IOException ex) {}
        catch(ClassNotFoundException ex) {}

        try {
            file.close();
        }
        catch(Exception ex) {}

        requestFocus();

    }

    /**
     * The method, paint, is the method that actual draws everything! It is called every tenth of a second.
     * @param brush The graphic that we are using to paint.
     */
    public void paint(Graphics brush) {

        brush.setColor(Color.black);
        brush.fillRect(0, 0, width, height);


        if(state == StateOfGame.PLAY || state == StateOfGame.PAUSE ) {
            pause_resume.setEnabled(true);
        }
        else {
            pause_resume.setEnabled(false);
        }

        if(state == StateOfGame.PLAY || state == StateOfGame.PAUSE) {
            save.setEnabled(true);
        }
        else {
            save.setEnabled(false);
        }

        try {
            for(int i = 0; i < Star_objects.length; i++) {  // Draw the ship.
                Star_objects[i].draw(brush);
            }

            for(int i = 0; i < Asteroid_objects.size(); i++) {  // Draw and move the Asteroids.
                Asteroid_objects.get(i).draw(brush);
                if(state == StateOfGame.PLAY) {
                    Asteroid_objects.get(i).move();
                }
            }

            for(int i = 0; i < Bullets.size(); i++) {  // Draw the bullets if they are still inside our window or else remove them.
                if(Bullets.get(i).getActivity()) {
                    Bullets.get(i).draw(brush);
                    if(state == StateOfGame.PLAY) {
                        Bullets.get(i).move();
                    }
                }
                if(!Bullets.get(i).getActivity()) {
                    Bullets.remove(i);
                }
            }

            Point[] ship_shape = ship.getPoints();
            for(int i = 0; i < Asteroid_objects.size(); i++) { // Check if the ship collided to an asteroid and if so remove the asteroid
                for(int k = 0; k < ship_shape.length; k++) {  // reduce the number of lives by one and blink the ship's color
                    if(Asteroid_objects.get(i).contains(ship_shape[k])) {
                        Asteroid_objects.remove(i);
                        ship.setColor(Color.RED);
                        drawing_ship_color_time = System.currentTimeMillis();
                        if(Lives > 0) {
                            Lives--;
                        }
                        break;
                    }
                    if(ship.getColor() == Color.RED && (System.currentTimeMillis() - drawing_ship_color_time > 150)) {
                        ship.setColor(Color.MAGENTA);
                        drawing_ship_color_time = 0;
                    }
                }
            }

            for(int i = 0; i < Asteroid_objects.size(); i++) { // Check if a bullet hit an Asteroid and if so remove the bullet
                for(int j = 0; j < Bullets.size(); j++) {  // try to create asteroid kids and remove the father - asteroid.
                    if(Asteroid_objects.get(i).contains(Bullets.get(j).getPosition())) {
                        Bullets.remove(j);
                        createKid(Asteroid_objects.get(i));
                        Asteroid_objects.remove(i);
                        break;
                    }
                }
            }
        }
        catch(NullPointerException ex) {}

        if(plus_score != 0 && (System.currentTimeMillis() - drawing_plus_score_time < 500)) { // Draw +score for some time.
            brush.setColor(Color.yellow);
            brush.setFont(new Font("BOLD", Font.BOLD, 16));
            brush.drawString("+" + plus_score, (int)Math.round(plus_score_position.getX()), (int)Math.round(plus_score_position.getY()));
        }

        try { // Draw the ship.
            ship.draw(brush);
        }
        catch(NullPointerException ex) {}


        if(state == StateOfGame.PLAY) {    // Decrease the ship's accelaration.
            try {
                if(ship.getAcceleration() < 0.1) {
                    ship.setAcceleration(0);
                }

                if(ship.getAcceleration() > 0.1) {
                    ship.setAcceleration(ship.getAcceleration() - 0.1);
                }

                ship.accelerate();
                ship.move(Direction.UP);
            }
            catch(NullPointerException ex) {}
        }

        try {
            drawRightPanel(brush);
        }
        catch(NullPointerException ex) {}

        if(Lives == 0) {  // If lives are 0, its gameover.
            Gameover(brush);
        }

        if(state == StateOfGame.PAUSE) {  // If pause was pressed, its paused.
            Pause(brush);
        }

        try {
            if((Asteroid_objects.isEmpty() && state == StateOfGame.PLAY)  ||   // Draw get ready message.
                    (Asteroid_objects.isEmpty() && state == StateOfGame.START) ||
                    (state == StateOfGame.START) || (state == StateOfGame.NEW_GAME)) {
                drawGetReady(brush);
            }
        }
        catch(NullPointerException ex) {}

       
    }

    /**
     * The method, KeyPressed, is listening which key is pressed while the game is running.
     * @param e The key that was pressed.
     */
    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT :
       	    case KeyEvent.VK_KP_LEFT:
                if(state == StateOfGame.PLAY) {
                    ship.move(Direction.LEFT);  // Rotate left.
                }
	        break;
	    case KeyEvent.VK_RIGHT:
	    case KeyEvent.VK_KP_RIGHT:
                if(state == StateOfGame.PLAY) {
                    ship.move(Direction.RIGHT);  // Rotate right.
                }
	        break;
 	    case KeyEvent.VK_UP :
            case KeyEvent.VK_KP_UP:
                if(state == StateOfGame.PLAY) {
                    ship.setAcceleration(0.4);  // Accelarate.
                }
	        break;
            case KeyEvent.VK_SPACE:
                if(state == StateOfGame.PLAY && ((System.currentTimeMillis() - firing_time) > 400)) {  // Fire a bullet.
                    Point position = new Point(ship.getPosition().getX() + 7, ship.getPosition().getY() + 5);
                    double rotation = ship.getRotation();
                    Bullet bullet = new Bullet(position, rotation);
                    Bullets.add(bullet);
                    firing_time = System.currentTimeMillis();
                }
                break;
	    }
          
    }

    /**
     * The method, KeyReleased, is listening which key is released while the game is running.
     * @param e The key that was released.
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * The method, KeyTyped, is listening which key is typed while the game is running.
     * @param e The key that was typed.
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /*
     * The method, createKid, is creating 3 smaller asteroids, on their fathers old position, depending of how many
     * times the asteroid was exploded before.
     */
    private void createKid(Asteroid asteroid) {

        int explosion = asteroid.getExplosionTimes();
        Random generator = new Random(System.currentTimeMillis());

        if(explosion == 2) {  // The father was a small asteroid.
            Score += 15;
            plus_score = 15;
            plus_score_position = asteroid.getPosition().clone();
        }

        if(explosion == 1) { // The father was a middle asteroid.
            Score += 10;
            plus_score = 10;
            Point[] kid_asteroid = new Point[ASTEROID_POINTS];
            kid_asteroid[0] = new Point(10 + generator.nextInt(12) + 10 * 1.4, 50 + generator.nextInt(12) - 10 * 1.4);
            kid_asteroid[1] = new Point(60 + generator.nextInt(12) - 15 * 1.4, 60 + generator.nextInt(12) - 15 * 1.4);
            kid_asteroid[2] = new Point(60 + generator.nextInt(12) - 15 * 1.4, 10 + generator.nextInt(12) + 10 * 1.4);
            kid_asteroid[3] = new Point(30 + generator.nextInt(12), 35 + generator.nextInt(12) - 5 * 1.4);
            kid_asteroid[4] = new Point(10 + generator.nextInt(12) + 10 * 1.4, 10 + generator.nextInt(12) + 10 * 1.4);
            kid_asteroid[5] = new Point(-5 + generator.nextInt(12) + 15 * 1.4, 30 + generator.nextInt(12));
            Point kid_position1 = asteroid.getPosition().clone();
            Point kid_position2 = asteroid.getPosition().clone();
            Point kid_position3 = asteroid.getPosition().clone();
            Asteroid kid4 = new Asteroid(kid_asteroid, kid_position1, generator.nextInt(360), 2, (0.5 + generator.nextDouble())/2);
            Asteroid kid5 = new Asteroid(kid_asteroid, kid_position2, generator.nextInt(360), 2, (0.5 + generator.nextDouble())/2);
            Asteroid kid6 = new Asteroid(kid_asteroid, kid_position3, generator.nextInt(360), 2, (0.5 + generator.nextDouble())/2);
            Asteroid_objects.add(kid4);
            Asteroid_objects.add(kid5);
            Asteroid_objects.add(kid6);
            plus_score_position = asteroid.getPosition().clone();
        }
        if(explosion == 0) { // The father was a big asteroid.
            Score += 5;
            plus_score = 5;
            Point[] kid_asteroid = new Point[ASTEROID_POINTS];
            kid_asteroid[0] = new Point(10 + generator.nextInt(12) + 10, 50 + generator.nextInt(12) - 10);
            kid_asteroid[1] = new Point(60 + generator.nextInt(12) - 15, 60 + generator.nextInt(12) - 15);
            kid_asteroid[2] = new Point(60 + generator.nextInt(12) - 15, 10 + generator.nextInt(12) + 10);
            kid_asteroid[3] = new Point(30 + generator.nextInt(12), 35 + generator.nextInt(12) - 5);
            kid_asteroid[4] = new Point(10 + generator.nextInt(12) + 10, 10 + generator.nextInt(12) + 10);
            kid_asteroid[5] = new Point(-5 + generator.nextInt(12) + 15, 30 + generator.nextInt(12));
            Point kid_position1 = asteroid.getPosition().clone();
            Point kid_position2 = asteroid.getPosition().clone();
            Point kid_position3 = asteroid.getPosition().clone();
            Asteroid kid1 = new Asteroid(kid_asteroid, kid_position1, generator.nextInt(360), 1, (0.5 + generator.nextDouble())/2);
            Asteroid kid2 = new Asteroid(kid_asteroid, kid_position2, generator.nextInt(360), 1, (0.5 + generator.nextDouble())/2);
            Asteroid kid3 = new Asteroid(kid_asteroid, kid_position3, generator.nextInt(360), 1, (0.5 + generator.nextDouble())/2);
            Asteroid_objects.add(kid1);
            Asteroid_objects.add(kid2);
            Asteroid_objects.add(kid3);
            plus_score_position = asteroid.getPosition().clone();
        }
        
        drawing_plus_score_time = System.currentTimeMillis();


    }

    /*
     * The method, resetGameOnLevelUp, is reseting the game parameters are need to be reseted when all the asteroids are exploded,
     * also known as level up.
     */
    private void resetGameOnLevelUp() {
        // Reset some of the game parameters on level up.

        firing_time = System.currentTimeMillis();
        Random generator = new Random(firing_time);

        Point[] star_ship = new Point[SHIP_POINTS];
        star_ship[0] = new Point(0, 0);
        star_ship[1] = new Point(30, 15);
        star_ship[2] = new Point(0, 30);
        star_ship[3] = new Point(15, 15);

        ship = new Ship(star_ship, new Point(395, 265), 0.0);

        Point[][] Asteroid = new Point[NUMBER_OF_ASTEROIDS][ASTEROID_POINTS];
        for(int i = 0; i < Asteroid.length; i++) {
            Asteroid[i][0] = new Point(10 + generator.nextInt(12), 50 + generator.nextInt(12));
            Asteroid[i][1] = new Point(60 + generator.nextInt(12), 60 + generator.nextInt(12));
            Asteroid[i][2] = new Point(60 + generator.nextInt(12), 10 + generator.nextInt(12));
            Asteroid[i][3] = new Point(30 + generator.nextInt(12), 35 + generator.nextInt(12));
            Asteroid[i][4] = new Point(10 + generator.nextInt(12), 10 + generator.nextInt(12));
            Asteroid[i][5] = new Point(-5 + generator.nextInt(12), 30 + generator.nextInt(12));
        }

        Point[] Position = new Point[NUMBER_OF_ASTEROIDS];
        Position[0] = new Point(124, 312);
        Position[1] = new Point(402, 366);
        Position[2] = new Point(202, 67);
        Position[3] = new Point(456, 45);
        Position[4] = new Point(712, 237);
        Position[5] = new Point(74, 134);
        Position[6] = new Point(78, 423);
        Position[7] = new Point(501, 452);
        Position[8] = new Point(267, 167);
        Position[9] = new Point(297, 442);
        Position[10] = new Point(644, 87);
        Position[11] = new Point(533, 313);
        Position[12] = new Point(656, 411);
        Position[13] = new Point(487, 177);

        if(Level <= 10) {
            for(int i = 0; i < Level + 4; i++) {
                Asteroid Asteroid_structure = new Asteroid(Asteroid[i], Position[i], generator.nextInt(360), 0, (0.5 + generator.nextDouble())/2);
                Asteroid_objects.add(Asteroid_structure);
            }
        }
        else {
            for(int i = 0; i < Position.length; i++) {
                Asteroid Asteroid_structure = new Asteroid(Asteroid[i], Position[i], generator.nextInt(360), 0, (0.5 + generator.nextDouble())/2);
                Asteroid_objects.add(Asteroid_structure);
            }
        }


        
        Bullets.removeAll(Bullets);

        Lives = 5;

        drawing_time = 0;

        drawing_plus_score_time = 0;
        plus_score = 0;
        plus_score_position = new Point(0, 0);

        drawing_ship_color_time = 0;

        drawing_critical_condition_time = 0;

        drawing_save_time = 0;
        drawing_load_time = 0;

        drawing_gameover_time = 0;


    }

    /**
     * The method, resetNewGame, basically does the same as the game constractor, but it only re-initializes all the game parameters.
     */
    public void resetNewGame() {
        // Reset all the game parameters.
        
        Asteroid_objects.removeAll(Asteroid_objects);

        Level = 1;

        resetGameOnLevelUp();

        Random generator = new Random(firing_time);
        
        Star_objects = new Star[NUMBER_OF_STARS];
        for(int i = 0; i < Star_objects.length; i++) {
            Star_objects[i] = new Star(new Point(generator.nextInt(800), generator.nextInt(600)), 1 + generator.nextInt(4));
        }
        
        Score = 0;

        displayed_high_scores_registration = false;

        result = NUMBER_OF_HIGH_SCORES;
        
    }

    /*
     * The method, Pause, draws a paused notation on the window.
     * Parameters: brush <- The graphic that we are using to paint.
     */
    private void Pause(Graphics brush) {

         brush.setColor(Color.RED);
         brush.setFont(new Font("BOLD", Font.BOLD, 90));
         brush.drawString("PAUSED!", 195, 295);

    }

    /*
     * The method, Gameover, draws a game over notation on the window and checks for new high scores.
     * If there is a high score to be imported on the list, it also starts the interaction with the user.
     * Parameters: brush <- The graphic that we are using to paint.
     */
    private void Gameover(Graphics brush) {

        ship.setColor(Color.MAGENTA);
        brush.setColor(Color.RED);
        brush.setFont(new Font("BOLD", Font.BOLD, 90));
        brush.drawString("GAME OVER!", 110, 295);

        state = StateOfGame.GAMEOVER;
        if(drawing_gameover_time == 0) {
            drawing_gameover_time = System.currentTimeMillis();
        }

        if(Score > 0) {
            result = checkScore();  // Can the score be imported?
            if(result < NUMBER_OF_HIGH_SCORES && displayed_high_scores_registration == false && (System.currentTimeMillis() - drawing_gameover_time) > 2500) {
                createScoreRegistrationFrame();
            }
        }

    }

    /**
     * The method, displayHighScores, is returning the high score that was asked in a string.
     * @param whichScore The score that was asked.
     * @return The position of that highscore and the actuall high score if it exists.
     */
    public String displayHighScores(int whichScore) {
      String temp = "";

        if(whichScore < High_scores.size()) {
            temp = High_scores.get(whichScore).toString();
        }

        if(whichScore == NUMBER_OF_HIGH_SCORES - 1) {
            return whichScore + 1 + temp;
        }
        return String.format("%3d",whichScore + 1) + temp;

    }

    /**
     * The method, Save, is exporting all the necessary information in order the game to be saved and continued.
     * @return True if there was not a problem while saving, else false.
     */
    public Boolean Save() {
      ObjectOutputStream file = null;

        try {
            file = new ObjectOutputStream(new FileOutputStream("Gamesave.dat"));

            file.writeObject(Asteroid_objects); // Saving the active asteroids.
            file.writeObject(Star_objects); // Saving the stars.
            file.writeObject(Bullets); // Saving the active bullets.
            file.writeObject(ship); // Saving the ship.
            file.writeInt(Score); // Saving the Score.
            file.writeInt(Lives); // Saving the Lives.
            file.writeInt(Level); // Saving the Level.

            drawing_time = 0; // Reseting some parameters.
            
            drawing_plus_score_time = 0;
            plus_score = 0;
            plus_score_position = new Point(0, 0);

            drawing_ship_color_time = 0;

            drawing_critical_condition_time = 0;

            drawing_save_time = 0;
            drawing_load_time = 0;

            drawing_gameover_time = 0;

            displayed_high_scores_registration = false;
            
            result = NUMBER_OF_HIGH_SCORES;

            file.flush();
        }
        catch(IOException ex) {
            return false;
        }

        try {
            file.close();
        }
        catch(Exception ex) {
            return false;
        }

        return true;

    }

    /**
     * The method, Load, is importing all the necessary information in order the game to be continued from the last save.
     * @return True if there was not a problem while loading, else false.
     */
    public Boolean Load() {
      ObjectInputStream file = null;

        try {
            file = new ObjectInputStream(new FileInputStream("Gamesave.dat"));

            Asteroid_objects = (ArrayList<Asteroid>) file.readObject(); // Loading the asteroids.
            Star_objects = (Star[]) file.readObject(); // Loading the stars.
            Bullets = (ArrayList<Bullet>) file.readObject(); // Loading the bullets.
            ship = (Ship) file.readObject(); // Loading the ship.
            Score = file.readInt(); // Loading the Score.
            Lives = file.readInt(); // Loading the Lives.
            Level = file.readInt(); // Loading the Level.

            drawing_time = 0;  // Reseting some parameters.

            drawing_plus_score_time = 0;
            plus_score = 0;
            plus_score_position = new Point(0, 0);

            drawing_ship_color_time = 0;

            drawing_critical_condition_time = 0;

            drawing_save_time = 0;
            drawing_load_time = 0;

            drawing_gameover_time = 0;

            displayed_high_scores_registration = false;
            
            result = NUMBER_OF_HIGH_SCORES;
        }
        catch(IOException ex) {
            return false;
        }
        catch(ClassNotFoundException ex) {
            return false;
        }

        try {
            file.close();
        }
        catch(Exception ex) {
            return false;
        }
        
        return true;

    }

    /*
     * The method, drawGetReady, is drawing a notation to inform the user that the game is starting.
     * Parameters: brush <- The graphic that we are using to paint.
     */
    private void drawGetReady(Graphics brush) {

        ship.setColor(Color.MAGENTA);
        if(drawing_time == 0 && Asteroid_objects.isEmpty() && state == StateOfGame.PLAY) {
            drawing_time = System.currentTimeMillis();
            state = StateOfGame.START;
        }
        else {
            if(drawing_time == 0) {
                drawing_time = System.currentTimeMillis();
            }
        }

        if(System.currentTimeMillis() - drawing_time < 4000) {
            brush.setColor(Color.RED);
            brush.setFont(new Font("BOLD", Font.BOLD, 90));
            brush.drawString("GET READY!", 115, 300);
        }

        if((System.currentTimeMillis() - drawing_time > 4000) && System.currentTimeMillis() - drawing_time < 5000) {
            brush.setColor(Color.RED);
            brush.setFont(new Font("BOLD", Font.BOLD, 90));
            brush.drawString("5", 380, 300);
        }

        if((System.currentTimeMillis() - drawing_time > 5000) && System.currentTimeMillis() - drawing_time < 6000) {
            brush.setColor(Color.RED);
            brush.setFont(new Font("BOLD", Font.BOLD, 90));
            brush.drawString("4", 380, 300);
        }

        if((System.currentTimeMillis() - drawing_time > 6000) && System.currentTimeMillis() - drawing_time < 7000) {
            brush.setColor(Color.RED);
            brush.setFont(new Font("BOLD", Font.BOLD, 90));
            brush.drawString("3", 380, 300);
        }

        if((System.currentTimeMillis() - drawing_time > 7000) && System.currentTimeMillis() - drawing_time < 8000) {
            brush.setColor(Color.RED);
            brush.setFont(new Font("BOLD", Font.BOLD, 90));
            brush.drawString("2", 380, 300);
        }

        if((System.currentTimeMillis() - drawing_time > 8000) && System.currentTimeMillis() - drawing_time < 9000) {
            brush.setColor(Color.RED);
            brush.setFont(new Font("BOLD", Font.BOLD, 90));
            brush.drawString("1", 380, 300);
        }

        if((System.currentTimeMillis() - drawing_time > 9000) && System.currentTimeMillis() - drawing_time < 10000) {
            brush.setColor(Color.RED);
            brush.setFont(new Font("BOLD", Font.BOLD, 90));
            brush.drawString("GO!", 335, 300);
        }

        if(Asteroid_objects.isEmpty() && state == StateOfGame.START && (System.currentTimeMillis() - drawing_time > 10000)) {
            Level++;
            resetGameOnLevelUp();
            state = StateOfGame.PLAY;
        }

        if(state == StateOfGame.NEW_GAME && (System.currentTimeMillis() - drawing_time > 10000)) {
            state = StateOfGame.PLAY;
            drawing_time = 0;
        }

        if(state == StateOfGame.START && (System.currentTimeMillis() - drawing_time > 10000)) {
            state = StateOfGame.PLAY;
            drawing_time = 0;
        }

    }

    /*
     * The method, drawRightPanel, draws the level, the score, the number of lives, and the number of the asteroids.
     * Depending on the status it also draws a saving or loading or critical status notation.
     * Parameters: brush <- The graphic that we are using to paint.
     */
    private void drawRightPanel(Graphics brush) {

        brush.setColor(Color.GRAY);
        brush.fillRect(801, 0, 220, 599);
        brush.setColor(Color.BLUE);
        brush.fillRect(817, 78, 190, 30);
        brush.fillRect(817, 128, 190, 30);
        brush.fillRect(817, 178, 190, 30);
        brush.fillRect(817, 228, 190, 30);
        brush.setColor(Color.ORANGE);
        brush.drawRect(820, 81, 183, 23);
        brush.drawRect(820, 131, 183, 23);
        brush.drawRect(820, 181, 183, 23);
        brush.drawRect(820, 231, 183, 23);
        brush.setFont(new Font("BOLD", Font.BOLD, 20));
        brush.drawString("Level:", 860, 100);
        brush.drawString("" + String.format("%03d", Level), 945, 100);
        brush.drawString("Score:", 850, 150);
        brush.drawString("" + String.format("%06d", Score), 925, 150);
        brush.drawString("Lives:", 860, 200);
        brush.drawString("" + String.format("%03d", Lives), 945, 200);
        brush.drawString("Asteroids:", 840, 250);
        brush.drawString("" + String.format("%03d", Asteroid_objects.size()), 945, 250);


        if(Lives == 1 && drawing_critical_condition_time == 0) {
            brush.setColor(Color.RED);
            brush.setFont(new Font("BOLD", Font.BOLD, 24));
            brush.drawString("Critical Status!", 821, 440);
            drawing_critical_condition_time = System.currentTimeMillis();

        }
        else {
            if(Lives == 1 && (System.currentTimeMillis() - drawing_critical_condition_time < 400)) {
                brush.setColor(Color.RED);
                brush.setFont(new Font("BOLD", Font.BOLD, 24));
                brush.drawString("Critical Status!", 821, 440);
            }
            else {
                if((System.currentTimeMillis() - drawing_critical_condition_time > 800)) {
                     drawing_critical_condition_time = 0;
                }
            }
        }

        if(state == StateOfGame.SAVE) {
            if(drawing_save_time == 0) {
                drawing_save_time = System.currentTimeMillis();
            }

            if(System.currentTimeMillis() - drawing_save_time < 2000) {
                brush.setColor(Color.RED);
                brush.setFont(new Font("BOLD", Font.BOLD, 24));
                brush.drawString("Saving!", 860, 440);
            }
            else {
                drawing_save_time = 0;
                state = StateOfGame.START;
            }
        }

        if(state == StateOfGame.LOAD) {
            if(drawing_load_time == 0) {
                drawing_load_time = System.currentTimeMillis();
            }

            if(System.currentTimeMillis() - drawing_load_time < 2000) {
                brush.setColor(Color.RED);
                brush.setFont(new Font("BOLD", Font.BOLD, 24));
                brush.drawString("Loading!", 854, 440);
            }
            else {
                drawing_load_time = 0;
                state = StateOfGame.START;
            }
        }

    }

    /*
     * The method, checkScore, is checking if the user's score can be imported to the highscore's list.
     */
    private int checkScore() {
      int i = 0;

        while(i < High_scores.size()) {
            if(Score > High_scores.get(i).getScore()) { // find the spot
                return i;
            }
            i++;
        }

        return i;

    }

    /*
     * The method, listInsert, is importing the high score in the given position.
     * Parameters: i <- In which spot of the list, entry <- The high score.
     */
    private void listInsert(int i, HighScore entry) {

        if(High_scores.size() < NUMBER_OF_HIGH_SCORES) { // The list is not full, just import the element.
            High_scores.add(i, entry);
        }
        else {                                          // The list was full, import the element and the remove the last item of the list.
            High_scores.add(i, entry);
            High_scores.remove(NUMBER_OF_HIGH_SCORES);
            High_scores.trimToSize();
        }

    }

    /*
     * The method, createScoreRegistrationFrame, is creating the window, so the user can add his name.
     */
    private void createScoreRegistrationFrame() {

        enter_your_name = new Frame("New High Score!");
        enter_your_name.setSize(HIGH_SCORE_FRAME_WIDTH, HIGH_SCORE_FRAME_HEIGHT);
        enter_your_name.setResizable(false);
        enter_your_name.setLocation((int)frame.getLocation().getX() + (width / 4) + 53, (int)frame.getLocation().getY() + (height / 4) - 40);

        Label labelLevelScoreString = new Label("                    Level                  Score                 ");
        
        Label labelLevelScore = new Label("                     " + String.format("%03d", Level) + "                   " + String.format("%06d", Score) + "               ");
        
        Panel level_score = new Panel();
        level_score.setLayout(new FlowLayout());
       
        level_score.add(labelLevelScoreString);
        level_score.add(labelLevelScore);
       
        level_score.setBackground(Color.GRAY);
        level_score.setFont(new Font("BOLD", Font.BOLD, 14));
        
        Panel centerPanel = new Panel();
        centerPanel.setLayout(new GridLayout(3,1));
        centerPanel.setBackground(Color.GRAY);

        Panel textfield = new Panel();
        textfield.setLayout(new FlowLayout());
        textfield.setBackground(Color.GRAY);
        textfield.setFont(new Font("BOLD", Font.BOLD, 14));
       
        player = new TextField(40);
       
        player.setBackground(Color.RED);
        
        Label enter_name = new Label("   Enter your name!");
       
        textfield.add(enter_name);
        textfield.add(player);

        centerPanel.add(level_score);
        centerPanel.add(textfield);
        centerPanel.add(new Panel());

        Panel button = new Panel();
        button.setBackground(Color.GRAY);
        Button next = new Button("Next");
        next.setBackground(Color.LIGHT_GRAY);
        
        button.add(next);
        button.setBackground(Color.RED);

        enter_your_name.setLayout(new BorderLayout());
        enter_your_name.add(centerPanel, BorderLayout.CENTER);
        enter_your_name.add(button, BorderLayout.SOUTH);


        displayed_high_scores_registration = true;
        enter_your_name.setVisible(true);

        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {
                    highscore = new HighScore(Level, Score, player.getText().substring(0, 19));
                    listInsert(result, highscore);
                }
                catch(StringIndexOutOfBoundsException ex) {
                    highscore = new HighScore(Level, Score, player.getText());
                    listInsert(result, highscore);
                }

                ObjectOutputStream file = null;

                try {
                    file = new ObjectOutputStream(new FileOutputStream("Highscores.dat"));

                    file.writeObject(High_scores);
                }
                catch(IOException ex) {}

                try {
                    file.close();
                }
                catch (IOException ex) {}

                enter_your_name.dispose();
                
                showHighScores();
            }
        });

    }

    /**
     * The method, showHighScores, is creating the window that displays the high scores.
     */
    public void showHighScores() {

        high_scores.setEnabled(false);

        high_scores_frame = new Frame("High Scores!");
        high_scores_frame.setSize(HIGH_SCORE_FRAME_WIDTH, HIGH_SCORE_FRAME_HEIGHT);
        high_scores_frame.setResizable(false);
        high_scores_frame.setBackground(Color.GRAY);
        high_scores_frame.setLocation((int)frame.getLocation().getX() + (width / 4) + 53, (int)frame.getLocation().getY() + (height / 4) - 40);

        high_scores_frame.setLayout(new BorderLayout());
        Panel scoresPanel=new Panel();
        Panel buttonPanel=new Panel();
        Panel title = new Panel();
        title.setFont(new Font("BOLD", Font.BOLD, 14));


        Label topGuns = new Label("Top Guns!");
        title.add(topGuns);
        title.setBackground(Color.RED);
        high_scores_frame.add(title,BorderLayout.NORTH);
        high_scores_frame.add(scoresPanel,BorderLayout.CENTER);
        high_scores_frame.add(buttonPanel, BorderLayout.SOUTH);

        scoresPanel.setLayout(new BoxLayout(scoresPanel, BoxLayout.Y_AXIS));
        scoresPanel.setFont(new Font("BOLD", Font.BOLD, 14));
        scoresPanel.setForeground(Color.BLACK);
        scoresPanel.add(new Label("      Level   Score                              Name"));

        for(int i = NUMBER_OF_HIGH_SCORES - 1; i > -1; i--) {
            scoresPanel.add(new Label(displayHighScores(i)), BoxLayout.Y_AXIS);

        }

        Button Close = new Button("Close");
        Close.setBackground(Color.LIGHT_GRAY);
        buttonPanel.add(Close);
        buttonPanel.setBackground(Color.RED);

        Close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                high_scores.setEnabled(true);
                high_scores_frame.dispose();
                if(state == StateOfGame.PAUSE) {
                    state = StateOfGame.PLAY;
                }
            }
        });

        if(state == StateOfGame.PLAY) {
            state = StateOfGame.PAUSE;
        }

        high_scores_frame.setVisible(true);

    }


    /**
     * Creates a new instance of the game.
     * @param args Command-line parameters (Not used).
     */
    public static void main (String[] args) {

        new Asteroids();
    
    }
    
}