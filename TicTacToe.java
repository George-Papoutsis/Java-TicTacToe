import javax.swing.*;
/**
 * TicTacToe class used to run the TicTacToe program
 *
 * @author George Papoutsis
 * @version April 4, 2023
 */
public class TicTacToe
{
    /**
     * main function for the TicTacToe game used to start the program
     */
    public static void main(String[] args){
        TicTacToeModel model = new TicTacToeModel(); // create the model
        
        TicTacToeView view = new TicTacToeView(); // create the view
        
        TicTacToeController controller = new TicTacToeController(view, model); // create the controller
        
        model.addObserver(view); // add the view as an observer of the model
        
        controller.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // setup what happens when you close the controller window
        controller.setSize(300, 590); // set size of window
        controller.setResizable(false); // disable resizability of the window to ensure everyhting looks the correct way
        controller.setVisible(true);
        
        view.update(model, null); // update the view based off the model
    }
}
