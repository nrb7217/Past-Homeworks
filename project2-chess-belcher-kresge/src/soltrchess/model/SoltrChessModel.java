package soltrchess.model;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import soltrchess.SoltrChess;
import soltrchess.backtracking.Backtracker;
import soltrchess.backtracking.Configuration;
import soltrchess.backtracking.SoltrChessConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


/**
 * Model class for the SoltrChess program. Contains all
 * the methods and logic needed for the user to play a game of
 * chess in either a PTUI or GUI fashion.
 * Authors: Nathan Belcher, Keegan Kresge
 */


public class SoltrChessModel {
    /** the number of rows */
    public static  int ROWS = 4;
    /** the number of columns */
    public static  int COLS = 4;
    /** the status of the game */
    private Status status;
    /** number of pieces on board*/
    private int boardPieces;
    /** states for the position you are trying to move from*/
    private int aRow = -1;
    private int aCol = -1;
    /** states for the position you are trying to move to*/
    private int bRow;
    private int bCol;
    /** states for the position you are trying to move from*/
    private int aRowCopy = -1;
    private int aColCopy = -1;
    /** Filename of starting board */
    public String filename;
    /** the game board */
    private PIECE[][] board;
    /** the observers of this model */
    private List<Observer<SoltrChessModel, String>> observers;
    /** the game status */
    public enum Status {
        NOT_OVER,
        WON,
        STALE_MATE,
    }

    /**
     * all of the types of chess pieces that can be on the board
     */
    public enum PIECE{
        NONE,
        PAWN,
        ROOK,
        KNIGHT,
        BISHOP,
        KING,
        QUEEN
    }

    /**
     * Constructor method for SoltrChess. Takes in both a filename if creating a new
     * board as well as a reference to a copy of the given board.
     * @param filename the name of the file the board is constructed from
     * @param copy a copy of the file's board contents in a soltrchessmodel type
     * @throws FileNotFoundException
     */
    public SoltrChessModel(String filename, SoltrChessModel copy) throws FileNotFoundException{
        if(copy!=null){
            status= copy.getGameStatus();
        }
        else {
            Scanner f = new Scanner(new File(filename));
            //Assign states
            status=Status.NOT_OVER;
            this.filename=filename;
            boardPieces=0;
            board=new PIECE[ROWS][COLS];
                for (int row=0; row<ROWS; ++row){
                    String line = f.nextLine();
                    String[] lst = line.split(" ");
                    for (int col=0; col<COLS; ++col) {
                        if (lst[col].equals("-")) {
                            board[row][col] = PIECE.NONE;
                            boardPieces--;
                        }
                        else if (lst[col].equals("P")) {
                            board[row][col] = PIECE.PAWN;
                        }
                        else if(lst[col].equals("R")) {
                            board[row][col] = PIECE.ROOK;
                        }
                        else if(lst[col].equals("N")) {
                            board[row][col] = PIECE.KNIGHT;
                        }
                        else if(lst[col].equals("Q")) {
                            board[row][col] = PIECE.QUEEN;
                        }
                        else if(lst[col].equals("K")) {
                            board[row][col] = PIECE.KING;
                        }
                        else if(lst[col].equals("B")) {
                            board[row][col] = PIECE.BISHOP;
                        }
                        boardPieces++;
                    }
                }


            //Add Observers to model
            this.observers = new LinkedList<>();
            f.close();
        }
    }

    /**
     * The view calls this method to add themselves as an observer of the model.
     *
     * @param observer the observer
     */
    public void addObserver(Observer<SoltrChessModel, String> observer) {
        this.observers.add(observer);
    }

    /** When the model changes, the observers are notified via their update() method */
    private void notifyObservers(String message) {
        for (Observer<SoltrChessModel, String> obs: this.observers ) {
            obs.update(this,message);
        }
    }

    /**
     * Get the status of the game.
     * @return game status
     */
    public Status getGameStatus() {
        return this.status;
    }

    /**
     * gets what piece if on that section of the board grid, if any
     * @param row current row
     * @param col current column
     * @return contents of board at given row and col
     */
    public PIECE getContents(int row, int col) {
        System.out.println(this.board[row][col]);
        return this.board[row][col];
    }

    /**
     * boolean cases for board movements. For each type of soltrchess
     * piece, it checks if the chosen movement follows the established
     * rules for that piece and returns either true or false as to whether
     * that move is legal.
     * @param aRow the row of the piece being moved
     * @param aCol the column of the piece being moved
     * @param bRow the row the selected piece is being moved to
     * @param bCol the column the selected piece is being moved to
     * @return either true if the move is valid or false if the move is invald
     */
    public boolean isValidMove(int aRow, int aCol, int bRow, int bCol) {
        this.aRow = aRow;
        this.aCol = aCol;
        this.bCol = bCol;
        this.bRow = bRow;
        PIECE curr_piece = getContents(aRow, aCol);
        PIECE nxt_piece = getContents(bRow, bCol);
        //checks if both coordinates have a valid piece
        if (curr_piece == PIECE.NONE || nxt_piece == PIECE.NONE) {
            return false;
        }
        //checks to make sure square you're going to isn't same as square you're in
        if (aRow==bRow &&aCol==bCol) {
            return false;
        }
        /*check if move being made is in bounds; used for PTUI*/
        if (aRow > ROWS || aCol > COLS || aRow < 0 || aCol < 0) {
            return false;
        }
        switch (curr_piece) {
            case PAWN:
                return (aRow-1==bRow && (aCol-1==bCol||aCol+1==bCol));
            case BISHOP:
                return (Math.abs((bCol - aCol)) == Math.abs((bRow - aRow)));
            case ROOK:
                //case if moving vertically
                if (bCol == aCol) {
                    return (Math.abs(bRow - aRow) > 0);
                }
                //case if moving horizontally
                else if (bRow == aRow) {
                    return (Math.abs(bCol - aCol) > 0);
                } else {
                    return false;
                }
            case KING:
                if (bRow == aRow) {
                    return (Math.abs(bCol - aCol) == 1);
                }
                else if (bCol == aCol) {
                    return (Math.abs(bRow - aRow) == 1);
                }
                else {
                    return (Math.abs(bRow - aRow) == 1 && Math.abs(bCol - aCol) == 1);
                }
            case QUEEN:
                //uses combination of rook and bishop cases
                if (bCol == aCol) {
                    return (Math.abs(bRow - aRow) > 0);
                }
                //case if moving horizontally
                else if (bRow == aRow) {
                    return (Math.abs(bCol - aCol) > 0);
                } else {
                    return (Math.abs((bCol - aCol)) == Math.abs((bRow - aRow)));
                }
            case KNIGHT:
                //checks if knight's total movement is equivalent to three spaces
                return ((Math.abs(bRow - aRow) + Math.abs(bCol - aCol) == 3) && (Math.abs(bRow - aRow) != 3) && Math.abs(bCol - aCol) != 3);

        }
        return false;
    }

    /**
     * Assuming the move is valid, set the piece at B to be the piece at A and set the piece at A to be NONE
     * precondition: isvalid method returned true
     */
    public void claimPiece() {
        board[bRow][bCol]=getContents(aRow,aCol);
        board[aRow][aCol]=PIECE.NONE;
    }

    /**
     * Select a new game
     * @param stage
     */
    public void newGame(Stage stage) throws FileNotFoundException {
        FileChooser fileChooser=new FileChooser();
        File selectedFile= fileChooser.showOpenDialog(stage);
        this.filename = String.valueOf(selectedFile);
        SoltrChessModel newGame= new SoltrChessModel(filename, null);
        this.board=newGame.board;
        notifyObservers("New Game");
    }

    public void newGamePTUI(String filename) throws FileNotFoundException{
        this.filename = filename;
        SoltrChessModel newGame = new SoltrChessModel(filename, null);
        this.board = newGame.board;
        notifyObservers("New Game");
    }

    /**
     * Set board to original layout
     */
    public void restart(){
        try {
            this.board=new SoltrChessModel(filename,null).getBoard();
            notifyObservers("Restarting Game!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void hint(){}

    public void solve(){
        Backtracker backtracker = new Backtracker();
        Configuration config= new SoltrChessConfig( modelToConfigBoard(),-1,-1,"-");
            Optional<Configuration> solvedBoard= backtracker.solve(config);
            if(solvedBoard.isPresent()){
                ConfigToModelBoard( solvedBoard.get().getBoard());
            }
            notifyObservers("Solved");

    }

    public SoltrChessConfig modelToConfigBoard(){
        String letter="-";
        String[][] newBoard=new String[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                PIECE piece= this.board[i][j];
                switch (piece) {
                    case PAWN:
                        letter="P";
                    case BISHOP:
                        letter="B";
                    case ROOK:
                        letter="R";
                    case KING:
                        letter="K";
                    case QUEEN:
                        letter="Q";
                    case KNIGHT:
                        letter="N";
                    case NONE:
                        letter="-";
                }
                newBoard[i][j]=letter;
            }
        }
        return null;
    }

    public void ConfigToModelBoard(String[][] board){

    }

    /**
     * moves the piece to a valid space
     *
     * notifies the observers of changes
     */
    public void makeMove(int newRow, int newCol) {
        if (aCol == -1 && aRow == -1) {
            aCol = newRow;
            aRow = newCol;
            System.out.println("source selected: " + getContents(aRow, aCol));
            notifyObservers("Source selected: ("+aRow+","+aCol+")" + getContents(aRow, aCol));
        } else {
            System.out.println("target selected");
            bCol=newRow;
            bRow=newCol;
            // check if the game has been won, tied, or is still going on
            if (hasWonGame()) {
                this.status = Status.WON;
                //add a notify observers
                notifyObservers("Game has been won!");
            } else if (boardPieces == 2 ) { //have a better check for invalid move

            notifyObservers("Stale Mate!");
             }
            else {
                this.status = Status.NOT_OVER;
                if (isValidMove(aRow, aCol, bRow, bCol)) {
                    claimPiece();
                    notifyObservers(getContents(bRow,bCol)+" to ("+bRow+","+bCol+")");
                    if (boardPieces == 1) {
                        notifyObservers("Game has been won!");
                    }
                } else {
                    notifyObservers("Move not allowed.");//case for invalid move
                }
            }
            aRowCopy=aRow;
            aColCopy=aCol;
            aRow=-1;
            aCol=-1;
        }
    }

    /**
     * checks if there is only once piece left on the board
     * @return boolean value of whether board has one piece left
     */
    public boolean hasWonGame() {
        return boardPieces<=1;
    }

    /**
     * string representation of the model
     * @return string representation
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int row = 0; row < ROWS; ++row) {
            builder.append(row);
            for (int col = 0; col < COLS; ++col) {
                if (this.board[row][col] == PIECE.NONE) {
                    builder.append("- ");
                } else if (this.board[row][col] == PIECE.BISHOP) {
                    builder.append("B ");
                } else if (this.board[row][col] == PIECE.PAWN) {
                    builder.append("P ");
                } else if (this.board[row][col] == PIECE.ROOK) {
                    builder.append("R ");
                } else if (this.board[row][col] == PIECE.KING) {
                    builder.append("K ");
                } else if (this.board[row][col] == PIECE.QUEEN) {
                    builder.append("Q ");
                } else if (this.board[row][col] == PIECE.KNIGHT) {
                    builder.append("K ");
                }
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    /**
     * getter function for the soltrchess board model
     * @return instance of the 2D board array
     */
    public PIECE[][] getBoard() {
        return board;
    }

    public int[] getB() {
        int[]temp =new int[2];
        temp[0]=bRow;
        temp[1]=bCol;
        return temp;
    }
    public int[] getA() {
        int[]temp =new int[2];
        temp[0]=aRowCopy;
        temp[1]=aColCopy;
        return temp;
    }
}