import java.awt.*;
import javax.swing.*;
import java.util.Observer;
import java.util.Observable;
/**
 * TicTacToeView class used to setup any non-input elements that the user sees on screen. This class is an observer of the TicTacToe model class and
 * updates itself whenever the model changes.
 *
 * @author George Papoutsis
 * @version April 4 2023
 */
public class TicTacToeView extends JPanel implements Observer
{
    /* fields part of the game Score Board */
    private JPanel scoreBoard;
    private JLabel status;
    private JPanel x;
    private JPanel o;
    private JLabel xScore;
    private JLabel oScore;
    
    /* Colors */
    private final Color DEFAULTCOLOR = UIManager.getColor("panel.background");
    private final Color GRAY = Color.lightGray;
    
    /**
     * TicTacToeView constructor used to create a TicTacToeView object.
     */
    public TicTacToeView(){
        super.setLayout(new BorderLayout()); // set the JPanel to Borderlayout for easier organization
        
        JLabel title = new JLabel("Score Board", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        scoreBoard = new JPanel();
        scoreBoard.setPreferredSize(new Dimension(300, 200));
        scoreBoard.setLayout(new BorderLayout()); // set the ScoreBoard to borderlayout for easier organization
        scoreBoard.add(title, BorderLayout.PAGE_START);
        status = new JLabel("Game in Progress", SwingConstants.CENTER);
        status.setFont(new Font("SansSerif", Font.BOLD, 16));
        scoreBoard.add(status, BorderLayout.PAGE_END);
        
        x = new JPanel();
        x.setPreferredSize(new Dimension(100, 20));
        x.setBackground(GRAY); // set x background to gray to show that it is player x's turn
        x.setLayout(new GridLayout(2, 1)); // make grid layout to easily layout the player name and their score
        
        JLabel xTitle = new JLabel("Player X", SwingConstants.CENTER);
        xTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        x.add(xTitle);
        
        xScore = new JLabel("0", SwingConstants.CENTER);
        xScore.setFont(new Font("SansSerif", Font.BOLD, 30));
        x.add(xScore);
        
        scoreBoard.add(x, BorderLayout.LINE_START);
        
        o = new JPanel();
        o.setLayout(new GridLayout(2, 1));
        o.setPreferredSize(new Dimension(100, 20));
        
        JLabel oTitle = new JLabel("Player O", SwingConstants.CENTER);
        oTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        o.add(oTitle);
        
        oScore = new JLabel("0", SwingConstants.CENTER);
        oScore.setFont(new Font("SansSerif", Font.BOLD, 30));
        o.add(oScore);
        
        scoreBoard.add(o, BorderLayout.LINE_END);
        
        super.add(scoreBoard, BorderLayout.PAGE_END);
    }
    
    /**
     * update method for TicTacToeView class used to update the view whenever the model updates.
     * 
     * @param obs - the Observable object that the TicTacToeView views
     * @param arg - an optional argument
     */
    public void update(Observable obs, Object arg){
        TicTacToeModel model = (TicTacToeModel)obs; // cast obs to TicTacToeModel
        /* If arg came in as a String (player turn was updated) */
        if(arg instanceof String){
            String p = (String)arg; // cast arg to String
            /* Change the background colors of the player name and scores to show whos turn it is */
            if(p.equals("X")){
                x.setBackground(GRAY);
                o.setBackground(DEFAULTCOLOR);
            }
            else{
                x.setBackground(DEFAULTCOLOR);
                o.setBackground(GRAY);
            }
        }
        /* If arg waas of type Integer[] (player scores were updated)*/
        else if(arg instanceof Integer[]){
            Integer[] scores = (Integer[])arg; // cast arg to Integer[]
            /* update the player scores */
            xScore.setText(scores[0].toString());
            oScore.setText(scores[1].toString());
        }
        /* If arg waas of type Integer (winner was updated)*/
        else if(arg instanceof Integer){
            Integer i = (Integer)arg; // cast arg to Integer
            /* If i was 0 (player X won) */
            if(i == 0){
                status.setText("Player X Wins");
            }
            /* If i was 1 (player O won) */
            else if(i == 1){
                status.setText("Player O Wins");
            }
            /* If i was 2 (tie) */
            else if(i == 2){
                status.setText("Tie");
            }
            /* anything else means that the game is still in progress */
            else{
                status.setText("Game in Progress");
            }
        }
    }
}
