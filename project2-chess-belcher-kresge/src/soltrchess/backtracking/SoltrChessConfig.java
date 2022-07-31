package soltrchess.backtracking;

import soltrchess.SoltrChess;
import soltrchess.model.SoltrChessModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SoltrChessConfig implements Configuration{
    String[][] board=new String[4][4];
    int numPieces=0;
    int aRow;
    int aCol;
    int bRow;
    int bCol;

    SoltrChessConfig(String filename) throws FileNotFoundException {
        Scanner f = new Scanner(new File(filename));
        for (int i = 0; i < 4; i++) {
            String[] line=f.nextLine().split(" ");
            for (int j = 0; j < 4; j++) {
                if(!line[j].equals("-")){numPieces++;}
                board[i][j]=line[j];
            }
        }

    }
    /**
     * Copy constructor
     *
     * @param copy SkyscraperConfig instance
     */
    public SoltrChessConfig(SoltrChessConfig copy, int row, int col, String value) {
        // copy over dimension
        this.aRow = row;
        this.aCol = col;
        // create the new board
        this.board = new String[4][4];
        // copy over the rows from the old board to the new one
        for (int r = 0; r < 4; r++) {
            System.arraycopy(copy.board[r], 0, this.board[r], 0, 4);
        }
        if(row!=-1){this.board[row][col] = value;}

    }

/**
 * The main program.
 * @param args command line arguments
 * @throws FileNotFoundException if file not found
 */

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 2) {
            System.err.println("Usage: java Skyscraper file debug");
        } else {
            // pass scanner object to constructor to read initial board
            String fileName = args[0];
            SoltrChessConfig initConfig = new SoltrChessConfig(fileName);

            boolean debug = args[1].equals("true");
            System.out.println("File: " + fileName);
            System.out.println("Debug: " + debug);
            System.out.println("Initial config:");
            System.out.println(initConfig);

            // create the backtracker
            Backtracker bt = new Backtracker();

            // start the clock
            double start = System.currentTimeMillis();

            // solve the puzzle
            Optional<Configuration> solution = bt.solve(initConfig);

            // compute the elapsed time
            double elapsed = (System.currentTimeMillis() - start) / 1000.0;

            // display the solution, if one exists
            if (solution.isPresent()) {
                System.out.println("Solution:\n" + solution.get());
            } else {
                System.out.println("No solution");
            }

            System.out.println("Elapsed time: " + elapsed + " seconds.");
        }
    }

    public Collection<Configuration> getSuccessorMoves(SoltrChessConfig config, String piece){
        Collection<Configuration> successors = new ArrayList<>();
        String testPiece;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                testPiece = config.board[i][j];
                if (!testPiece.equals("-")) {
                    successors.add(new SoltrChessConfig(config,i,j,piece));
                }
            }
        }
        return successors;
    }

    @Override
    public Collection<Configuration> getSuccessors() {
        Collection<Configuration> successors = new ArrayList<>();
        String piece;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                piece = this.board[i][j];
                if (!piece.equals("-")) {
                    SoltrChessConfig iMissMyPiece= new SoltrChessConfig(this,i,j,"-");
                    successors.addAll(getSuccessorMoves(iMissMyPiece,piece));
                }
            }
        }
        return successors;
    }

    public String[][] getBoard() {
        return board;
    }

    @Override
    public boolean isValid(Configuration config) {
        String piece;
        String aPiece="-";
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                piece = config.getBoard()[i][j];
                if(!piece.equals("-")){
                    if(this.board[i][j].equals("-")){
                        aPiece=piece;
                        aRow=i;
                        aCol=j;
                    }else if(!this.board[i][j].equals(piece)){
                        bCol=j;
                        bRow=i;
                    }
                }
            }
        }
        //checks to make sure square you're going to isn't same as square you're in
        if (aRow==bRow &&aCol==bCol) {
            return false;
        }
        switch (aPiece) {
            case "P":
                return (aRow-1==bRow && (aCol-1==bCol||aCol+1==bCol));
            case "B":
                return (Math.abs((bCol - aCol)) == Math.abs((bRow - aRow)));
            case "R":
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
            case "K":
                if (bRow == aRow) {
                    return (Math.abs(bCol - aCol) == 1);
                }
                else if (bCol == aCol) {
                    return (Math.abs(bRow - aRow) == 1);
                }
                else {
                    return (Math.abs(bRow - aRow) == 1 && Math.abs(bCol - aCol) == 1);
                }
            case "Q":
                //edit so it checks rook and bishop cases
                if (bCol == aCol) {
                    return (Math.abs(bRow - aRow) > 0);
                }
                //case if moving horizontally
                else if (bRow == aRow) {
                    return (Math.abs(bCol - aCol) > 0);
                } else {
                    return (Math.abs((bCol - aCol)) == Math.abs((bRow - aRow)));
                }
            case "N":
                //checks if knight's total movement is equivalent to three spaces
                return ((Math.abs(bRow - aRow) + Math.abs(bCol - aCol) == 3) && (Math.abs(bRow - aRow) != 3) && Math.abs(bCol - aCol) != 3);

        }
        return false;
    }

    @Override
    public boolean isGoal() {
        numPieces = 0;
        for (int row = 0; row < 4; ++row) {
            for (int col = 0; col < 4; ++col) {
                if (!board[row][col].equals("-")) {
                    numPieces++;
                }
            }
        }
        return numPieces<=1;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < 4; ++row) {
            for (int col = 0; col < 4; ++col) {
                builder.append(board[row][col]).append(" ");
            }
            builder.append('\n');
            }
        return builder.toString();
    }
}