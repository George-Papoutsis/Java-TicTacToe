import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * The controller for TicTacToe game used to crate and manage anything to do with user input (Buttons, Menus).
 *
 * @author George Papoutsis
 * @version April 4 2023
 */
public class TicTacToeController extends JFrame implements ActionListener
{
    /* Menu and Menu Items */
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem newGameItem;
    private JMenuItem quitGameItem;
    
    /* Play Again button */
    private JButton playAgainButton;
    
    /* The Gaame Board */
    private JButton[][] board; // array of the buttons
    private JPanel gameBoard; // Panel for the buttons to go on
    
    /* The model */
    private TicTacToeModel model;
    
    /* Colors */
    private final Color RED = Color.red;
    private final Color DEFAULTCOLOR = UIManager.getColor("button.background");
    
    /* Sounds */
    private final String WINSOUND = ".//sounds//winSound.wav";
    private final String PLAYERXSOUND = ".//sounds//playerXSound.wav";
    private final String PLAYEROSOUND = ".//sounds//playerOSound.wav";
    private final String TIESOUND = ".//sounds//tieSound.wav";
    private final String CLICKSOUND = ".//sounds//clickSound.wav";
    
    /**
     * Constructor for the TicTacToeController class used to create the controller.
     * 
     * @param view - the TicTacToeView we want to add to our JFrame
     * @param model - the TicTacToeModel we want to call methods from.
     */
    public TicTacToeController(TicTacToeView view, TicTacToeModel model){
        super("TicTacToe"); //Create the JFrame using super constructor
        
        this.model = model;
        populateFrame(view); //populate the frame
        
        registerListener(); // register all listeners
    }
    
    /**
     * registerListener method for TicTacToeController class used to add ActionListeners to all input elements.
     */
    private void registerListener(){
        newGameItem.addActionListener(this);
        quitGameItem.addActionListener(this);
        /* Loop through buttons and add listener to each */
        for(int i=0; i < board.length; i++){
            for(int j=0; j < board.length; j++){
                board[i][j].addActionListener(this);
            }
        }
        playAgainButton.addActionListener(this);
        
        // this allows us to use shortcuts (e.g. Ctrl-N and Ctrl-Q)
        final int SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx(); // to save typing
        quitGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));
        newGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, SHORTCUT_MASK));
    }
    
    /**
     * populateFrame method for TicTacToeController class used to add all elements to the JFrame.
     * 
     * @param view - the TicTacToeView we want to add to the JFrame.
     */
    private void populateFrame(TicTacToeView view){
        super.setLayout(new BorderLayout()); // set layout to BorderLayout to make organization of elements easier
        
        menuBar = new JMenuBar();
        
        menu = new JMenu("Menu");
        
        newGameItem = new JMenuItem("New Game");
        menu.add(newGameItem);
        
        quitGameItem = new JMenuItem("Quit");
        menu.add(quitGameItem);
        
        menuBar.add(menu);
        super.setJMenuBar(menuBar);
        
        playAgainButton = new JButton("PlayAgain");
        playAgainButton.setVisible(false);
        super.add(playAgainButton, BorderLayout.PAGE_START);
        
        /* Initialize game board */
        gameBoard = new JPanel();
        gameBoard.setLayout(new GridLayout(3, 3));
        board = new JButton[3][3];
        for(int i=0; i < board.length; i++){
            for(int j=0; j < board.length; j++){
                board[i][j] = new JButton();
                board[i][j].setEnabled(false);
                board[i][j].setFont(new Font("SansSerif", Font.BOLD, 25));
                gameBoard.add(board[i][j]);
            }
        }
        gameBoard.setPreferredSize(new Dimension(300, 300));
        view.add(gameBoard, BorderLayout.PAGE_START);
        
        super.add(view);
    }
    
    /**
     * actionPreformed method for TicTacToeController class used to setup logic for every input element in the game.
     * 
     * @param e - the ActionEvent that just happened.
     */
    public void actionPerformed(ActionEvent e){
        Object o = e.getSource();
        boolean done = false;
        /* If the Object was of type JMenu Item (newGameItem, quitGameItem) */
        if(o instanceof JMenuItem){
            JMenuItem item = (JMenuItem)o; // cast o into a JMenuItem
            SoundEffect sound = new SoundEffect(CLICKSOUND); // create a SoundEffect
            sound.play(); // play sound
            /* newGameItem */
            if(item == newGameItem){
                model.clearBoard(true); // clear board and score
                playAgainButton.setVisible(false);
                for(int i=0; i < board.length; i++){
                    for(int j = 0; j < board.length; j++){
                        board[i][j].setEnabled(true);
                        board[i][j].setBackground(DEFAULTCOLOR); // reset all button colors
                        board[i][j].setText(model.getBoardAtIndex(i, j));
                    }
                }
            }
            /* quitGameItem */
            else{
                System.exit(0); // close program
            }
        }
        /* JButton */
        else{
            JButton button = (JButton)o; // cast o into a JButton
            /* playAgainButton */
            if(button == playAgainButton){
                SoundEffect sound = new SoundEffect(CLICKSOUND);
                sound.play();
                model.clearBoard(false);
                playAgainButton.setVisible(false);
                if(model.getPlayer() == "O"){ // if the previous game ended on player O, swap back to player X
                    model.swapTurn();
                }
                /* Reset all the buttons in the game board */
                for(int i=0; i < board.length; i++){
                    for(int j = 0; j < board.length; j++){
                        board[i][j].setEnabled(true);
                        board[i][j].setBackground(DEFAULTCOLOR);
                        board[i][j].setText(model.getBoardAtIndex(i, j));
                    }
                }
            }
            /* Button in the game board */
            else{
                /* loop through board to check what button was pressed */
                for(int i=0; i < board.length; i++){
                    for(int j=0; j < board.length; j++){
                        if(button == board[i][j]){
                            model.setBoardAtIndex(i, j); // update the models version of the board
                            board[i][j].setEnabled(false); // disable the button
                            board[i][j].setText(model.getPlayer());
                            /* If someone has won the game or tie */
                            if(model.haveWinner(i, j)){
                                model.setWinner(model.getPlayer()); // update the winner
                                /* If it was not a tie */
                                if(model.getWinner() != "T"){
                                    int[][] tempList = model.getThreeInARow(); // get the coordinates of the winning squares
                                    SoundEffect sound = new SoundEffect(WINSOUND);
                                    sound.play();
                                    /* show the players the winning 3 squares */
                                    board[tempList[0][0]][tempList[0][1]].setBackground(RED);
                                    board[tempList[1][0]][tempList[1][1]].setBackground(RED);
                                    board[tempList[2][0]][tempList[2][1]].setBackground(RED);
                                }
                                /* If it was a tie */
                                else{
                                    SoundEffect sound = new SoundEffect(TIESOUND);
                                    sound.play();
                                }
                                
                                model.incrementScore(); // update the scores
                                done = true;
                                playAgainButton.setVisible(true); // reveal the playAgain button to the user
                            }
                            /* If no one has won yet */
                            else{
                                if(model.getPlayer() == "X"){
                                    SoundEffect sound = new SoundEffect(PLAYERXSOUND);
                                    sound.play();
                                }
                                else if(model.getPlayer() == "O"){
                                    SoundEffect sound = new SoundEffect(PLAYEROSOUND);
                                    sound.play();
                                }
                                model.swapTurn(); // switch the turn to the next player
                            }
                        }
                    }
                }
                /* If the game has finished */
                if(done){
                    for(int i=0; i < board.length; i++){
                        for(int j=0; j < board.length; j++){
                            board[i][j].setEnabled(false); // re-enable all buttons
                        }
                    }
                }
            }
        }
    }
}
