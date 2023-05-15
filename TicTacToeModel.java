import java.util.Observable;
/**
 * The MainMenuModel models the TicTacToe game. This class re-uses code from TicTacToe lab10 with some changes to make it work with a GUI.
 *
 * @author George Papoutsis
 * @version April 4 2023
 */
public class TicTacToeModel extends Observable
{
   public static final String PLAYER_X = "X"; // player using "X"
   public static final String PLAYER_O = "O"; // player using "O"
   public static final String EMPTY = " ";  // empty cell
   public static final String TIE = "T"; // game ended in a tie
 
   private String player;   // current player (PLAYER_X or PLAYER_O)

   private String winner;   // winner: PLAYER_X, PLAYER_O, TIE, EMPTY = in progress

   private int numFreeSquares; // number of squares still free
   
   private String board[][]; /* 3x3 array representing the board (NOTE: the model has it's own version of the board used for checking for a winner. 
   This board is updated along side the JButton board in the TicTacToeController class. */
   
   /* Player scores */
   private int xScore;
   private int oScore;
   
   /* Coordinates of the 3 squares that won the game */
   private int[][] threeInARow;
   
   /** 
    * Constructor for the TicTacToeModel class used to initialize fields for the class. 
    */
   public TicTacToeModel()
   {
      board = new String[3][3]; // initialize the model's version of the board
      xScore = 0;
      oScore = 0;
      threeInARow = new int[3][2]; // initialize the array of winning squares
      
      clearBoard(false); // clear the board (no need to update score)
   }

   /**
    * clearBoard method for TicTacToeModel class.This method was re-used from lab10 with some changes. The method still resets the model's verion of the
    * board, winner, and player, but now also checks if we want to reset the player scores.
    * 
    * @param resetScore - true if we want to reset the player scores, otherwise false.
    */
   public void clearBoard(boolean resetScore)
   {
      for (int i = 0; i < 3; i++) {
         for (int j = 0; j < 3; j++) {
            board[i][j] = EMPTY; // reset model's board
         }
      }
      setWinner(EMPTY); // reset winner
      numFreeSquares = 9;
      player = PLAYER_X;     // Player X always has the first turn.
      
      setChanged();
      notifyObservers(player); // notify the view that the view has possibly chaged from O to X
      clearChanged();
      
      /* If we want to reste Score*/
      if(resetScore){
          xScore = 0;
          oScore = 0;
          
          /* Pack the player scores into an array of Integers */
          Integer[] scores = new Integer[2];
          scores[0] = xScore;
          scores[1] = oScore;
          
          setChanged();
          notifyObservers(scores); // notify the view that the scores have changed
          clearChanged();
      }
   }

   /**
    * Returns true if filling the given square gives us a winner, and false
    * otherwise. 
    * 
    * NOTE: This method has been re-used from lab10 with some changes. It now also checks for a Tie, decrements the number of free squares, and 
    * if there was a winner, it will also update the threeInARow array with the winning squares.
    *
    * @param int row of square just set
    * @param int col of square just set
    * 
    * @return true if we have a winner or Tie, false otherwise
    */
   public boolean haveWinner(int row, int col) 
   {
       // unless at least 5 squares have been filled, we don't need to go any further
       // (the earliest we can have a winner is after player X's 3rd move).
         
       if(numFreeSquares >= 1){
           numFreeSquares --;
       }
       
       if (numFreeSquares>4) return false;

       // Note: We don't need to check all rows, columns, and diagonals, only those
       // that contain the latest filled square.  We know that we have a winner 
       // if all 3 squares are the same, as they can't all be blank (as the latest
       // filled square is one of them).

       // check row "row"
       if ( board[row][0].equals(board[row][1]) &&
            board[row][0].equals(board[row][2]) ){
                /* pack coordinates of winning squares into threeInARow array */
                threeInARow[0][0] = row;
                threeInARow[0][1] = 0;
                threeInARow[1][0] = row;
                threeInARow[1][1] = 1;
                threeInARow[2][0] = row;
                threeInARow[2][1] = 2;
                return true;
            }
       
       // check column "col"
       if ( board[0][col].equals(board[1][col]) &&
            board[0][col].equals(board[2][col]) ){ 
                threeInARow[0][0] = 0;
                threeInARow[0][1] = col;
                threeInARow[1][0] = 1;
                threeInARow[1][1] = col;
                threeInARow[2][0] = 2;
                threeInARow[2][1] = col;
                return true;
            }

       // if row=col check one diagonal
       if (row==col)
          if ( board[0][0].equals(board[1][1]) &&
               board[0][0].equals(board[2][2]) ) {
                threeInARow[0][0] = 0;
                threeInARow[0][1] = 0;
                threeInARow[1][0] = 1;
                threeInARow[1][1] = 1;
                threeInARow[2][0] = 2;
                threeInARow[2][1] = 2;
                return true;
                }

       // if row=2-col check other diagonal
       if (row==2-col)
          if ( board[0][2].equals(board[1][1]) &&
               board[0][2].equals(board[2][0]) ) {
                threeInARow[0][0] = 0;
                threeInARow[0][1] = 2;
                threeInARow[1][0] = 1;
                threeInARow[1][1] = 1;
                threeInARow[2][0] = 2;
                threeInARow[2][1] = 0;
                return true;
                }
       
       /* If there was a tie */
       if(numFreeSquares == 0){
           player = TIE;
           return true;
       }
       // no winner yet
       return false;
   }
   
   /**
    * getThreeInARow method for TicTacToeModel class used for returning the coordinates of the winning squares.
    * 
    * @return threeInARow
    */
   public int[][] getThreeInARow(){
       return threeInARow;
   }
   
   /**
    * swapTurn method for TicTacToeModel class used to swap the player's turn.
    */
   public void swapTurn(){
       // change to other player (this won't do anything if game has ended)
       if (player==PLAYER_X) 
           player=PLAYER_O;
       else 
           player=PLAYER_X;
        
       setChanged();
       notifyObservers(player); // notify the view that the player turn has changed
       clearChanged();
   }
   
   /**
    * getWinner method for TicTacToeModel class used to retrieve the winner.
    * 
    * @return the winner
    */
   public String getWinner(){
       return winner;
   }
   
   /**
    * setWinner method for TicTacToeModel class used to update the winner
    * 
    * @param w - the new winner
    */
   public void setWinner(String w){
       winner = w; // update winner
       setChanged();
       /* notify view of the change */
       if(winner.equals(PLAYER_X)){
           notifyObservers(0);
       }
       else if(winner.equals(PLAYER_O)){
           notifyObservers(1);
       }
       else if(winner.equals(TIE)){
           notifyObservers(2);
       }
       else{
           notifyObservers(3);
       }
       clearChanged();
   }
   
   /**
    * getPlayer method for TicTacToeModel class used to return the current player
    * 
    * @return the current player.
    */
   public String getPlayer(){
       return player;
   }
   
   /**
    * incrementScore method for TicTacToeModel class used to update the player scores.
    */
   public void incrementScore(){
       /* update the correct score based on the winner variable */
       if(winner == PLAYER_X){
           xScore ++;
       }
       else if(winner == PLAYER_O){
           oScore ++;
       }
       /* pack scores into an array of Integers */
       Integer[] scores = new Integer[2];
       scores[0] = xScore;
       scores[1] = oScore;
          
       setChanged();
       notifyObservers(scores); // notify the view of the changes
       clearChanged();
   }
   
   /**
    * getBoardAtIndex method for TicTacToeModel class used to get the value of the model's version of the board at a specific index.
    * 
    * @param i - the row
    * @param j - the column
    * 
    * @return the value at (i, j)
    */
   public String getBoardAtIndex(int i, int j){
       return board[i][j];
   }
   
   /**
    * setBoardAtIndex method for TicTacToeModel class used to set the value of the model's version of the board at a specific coordinate.
    * 
    * @param i - the row
    * @param j - the column
    */
   public void setBoardAtIndex(int i, int j){
       board[i][j] = player; // update with the current player
   }
   
   /**
    * getNumFreeSquares method for TicTacToeModel class used to get the number of free squares on the baord.
    * 
    * @return numFreeSquares
    */
   public int getNumFreeSquares(){
       return numFreeSquares;
   }
}
