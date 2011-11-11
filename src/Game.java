import java.awt.*;
import java.awt.event.*;

enum Direction {UP, RIGHT, LEFT}; // The moving directions.
enum StateOfGame {IDLE, START, PLAY, GAMEOVER, NEW_GAME, PAUSE, SAVE, LOAD}; // The game states.

/**
 * This class defines some of the window's mechanics.
 * @author hrkalona
 */
abstract class Game extends Canvas {
  protected static final int NUMBER_OF_HIGH_SCORES = 10; // The number of max high scores.
  protected StateOfGame state; // The current state of game.
  protected int width, height; // The main window's width and height.
  protected Image buffer;  // The image that was created.
  protected Frame frame;  // The main window.
  protected Frame high_scores_frame; // The high scores window.
  protected Frame enter_your_name; // The register your high score window.
  protected MenuItem start; // The menu item start.
  protected MenuItem pause_resume; // The menu item pause/resume.
  protected MenuItem save; // The menu item save;
  protected MenuItem high_scores; // The menu item high scores.
  protected TextField player; // The textfield that the user can write.
  protected static final int HIGH_SCORE_FRAME_WIDTH = 400; // Both high score's and register your high score's window width and height;
  protected static final int HIGH_SCORE_FRAME_HEIGHT = 400;

    public Game(String name, int inWidth, int inHeight) {
        width = inWidth;
	height = inHeight;

        state = StateOfGame.IDLE; // IDLE state.

        MenuBar menubar = new MenuBar();
        Menu menu;
        
        MenuItem new_game;
        MenuItem load;
        MenuItem exit;
        

        menu = new Menu("Game");
        
        start = new MenuItem("Start");
        new_game = new MenuItem("New Game");
        save = new MenuItem("Save");
        load = new MenuItem("Load");
        pause_resume = new MenuItem("Pause/Resume");
        high_scores = new MenuItem("High Scores");
        exit = new MenuItem("Quit");
        
        
        


        start.setShortcut(new MenuShortcut(KeyEvent.VK_F1)); // add shortcuts.
        new_game.setShortcut(new MenuShortcut(KeyEvent.VK_N));
        save.setShortcut(new MenuShortcut(KeyEvent.VK_S));
        load.setShortcut(new MenuShortcut(KeyEvent.VK_L));
        pause_resume.setShortcut(new MenuShortcut(KeyEvent.VK_P));
        high_scores.setShortcut(new MenuShortcut(KeyEvent.VK_H));
        exit.setShortcut(new MenuShortcut(KeyEvent.VK_Q));
        
        
        

        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                state = StateOfGame.START;
                repaint();
                start.setEnabled(false);
            }    

        } );

        

        new_game.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetNewGame();
                state = StateOfGame.NEW_GAME;
                repaint();
                start.setEnabled(false);
            }

        } );

        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if(Save()) {
                    if(state != StateOfGame.PAUSE) {
                        state = StateOfGame.SAVE;
                    }
                }

            }
        } );

        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                if(Load()) {
                    if(state != StateOfGame.PAUSE) {
                        state = StateOfGame.LOAD;
                    }
                    start.setEnabled(false);
                    repaint();
                }

            }
        } );

        

        pause_resume.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(state == StateOfGame.PLAY) {
                    state = StateOfGame.PAUSE;
                }
                else {
                    if(state == StateOfGame.PAUSE) {
                        state = StateOfGame.PLAY;
                    }
                }
            }

        } );
        
        high_scores.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                showHighScores();
                
            }
        });

        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }

        } );

        menu.add(start);
        menu.addSeparator();

        menu.add(new_game);
        menu.addSeparator();

        menu.add(save);
        menu.addSeparator();

        menu.add(load);
        menu.addSeparator();

        menu.add(pause_resume);
        menu.addSeparator();

        menu.add(high_scores);
        menu.addSeparator();

        menu.add(exit);
        
        menubar.add(menu);

        
     

	// Frame can be read as 'window' here.
        frame = new Frame(name);
        frame.add(this);
        frame.setSize(width, height);
        frame.setVisible(true);
        frame.setResizable(false);

        frame.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {}
        });

        
        frame.setMenuBar(menubar);

        buffer = createImage(width, height);

    }
  
    /**
     * The method, paint, is the method that actual draws everything! It is called every tenth of a second.
     * @param brush The Graphics parameter, also known as the brush that draws our canvas!
     */
    @Override
    abstract public void paint(Graphics brush);
  

    /**
     * The method, update, paints to a buffer then to the screen, then waits a tenth of
     * a second before repeating itself, assuming the game is on. This is done
     * to avoid a choppy painting experience if repainted in pieces.
     * @param brush The Graphics parameter, also known as the brush that draws our canvas!
     */
    @Override
    public void update(Graphics brush) {

        if(state == StateOfGame.PLAY || state == StateOfGame.START || state == StateOfGame.NEW_GAME ||
                state == StateOfGame.GAMEOVER || state == StateOfGame.PAUSE ||
                state == StateOfGame.SAVE || state == StateOfGame.LOAD) {
            paint(buffer.getGraphics());
            brush.drawImage(buffer,0,0,this);
        }
        
        if (state == StateOfGame.PLAY || state == StateOfGame.START || state == StateOfGame.NEW_GAME ||
                state == StateOfGame.GAMEOVER || state == StateOfGame.PAUSE ||
                state == StateOfGame.SAVE || state == StateOfGame.LOAD) {sleep(10); repaint();}

    }
  
    /*
     * The method, sleep, is a simple helper function used in update.
     */
    private void sleep(int time) {

        try {Thread.sleep(time);} catch(Exception exc){};

    }

    /**
     * The method, resetNewGame, basically does the same as the game constractor, but it only re-initializes all the game parameters.
     */
    public abstract void resetNewGame();

    /**
     * The method, displayHighScores, is returning the high score that was asked in a string.
     * @param whichScore The score that was asked.
     * @return The position of that highscore and the actuall high score if it exists.
     */
    public abstract String displayHighScores(int whichScore);

    /**
     * The method, Save, is exporting all the necessary information in order the game to be saved and continued.
     * @return True if there was not a problem while saving, else false.
     */
    public abstract Boolean Save();

    /**
     * The method, Load, is importing all the necessary information in order the game to be continued from the last save.
     * @return True if there was not a problem while loading, else false.
     */
    public abstract Boolean Load();

    /**
     * The method, showHighScores, is creating the window that displays the high scores.
     */
    public abstract void showHighScores();


}