package soltrchess.ptui;

import soltrchess.model.Observer;
import soltrchess.model.SoltrChessModel;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * PTUI method for running SoltrChess game model
 *
 * Authors: Nathan Belcher and Keegan Kresge
 */

public class SoltrChessPTUI implements Observer<SoltrChessModel, String> {

    public SoltrChessModel model;
    /**
     * Construct the PTUI
     *
     * @param filename the file name
     */
    public SoltrChessPTUI(String filename) throws FileNotFoundException {
        this.model = new SoltrChessModel(filename, null);
        initializeView();
    }

    /*
    ************************** THE VIEW SECTION *****************************************
     */

    /**
     * initializes the observer for the model on the PTUI
     */
    private void initializeView() {this.model.addObserver(this); }

    @Override
    public void update(SoltrChessModel soltrChessModel, String s) {
        System.out.print(this.model);
        System.out.println("Status " + this.model.getGameStatus());
    }
    /*
    ************************* THE CONTROLLER SECTION *************************************
     */

    /**
     * run method for PTUI. Allows users to move pieces, quit game,
     * choose a new game, restart game, get a hint from backtracker, or
     * have backtracker solve board
     */
    public void run() {
        System.out.println("Welcome to SoltrChess!\n");
        update(this.model, null); //change second parameter
        try (Scanner in = new Scanner(System.in)) {
            System.out.print("\n[Move,New,Restart,Hint,Solve,Quit]\n>");
            while (this.model.getGameStatus() == SoltrChessModel.Status.NOT_OVER) {
                String choice = in.nextLine();
                switch (choice) {
                    case "Quit", "quit" -> System.out.println("Quitting Game"); //TODO: add a different solution to this case

                    case "Move", "move" -> { //TODO: see why it's not selecting pieces
                        System.out.println("starting row: ");
                        int aRow = in.nextInt();
                        System.out.println("starting column: ");
                        int aCol = in.nextInt();
                        model.makeMove(aRow, aCol);
                        System.out.println("Move to row: ");
                        int bRow = in.nextInt();
                        System.out.println("move to column: ");
                        int bCol = in.nextInt();
                        model.makeMove(bRow, bCol);
                        update(model, null); //TODO: set null to use the string outputted by notifyobservers instead
                        System.out.print("\n[Move,New,Restart,Hint,Solve,Quit]\n>");
                    }

                    case "New", "new" -> {
                        System.out.println("Select a new filename\n");
                        String filename = in.nextLine();
                        model.newGamePTUI(filename);
                        update(model, null); //TODO
                        System.out.print("\n[Move,New,Restart,Hint,Solve,Quit]\n>");
                    }

                    case "Restart", "restart" -> {
                        model.restart();
                        update(model, null); //TODO
                        System.out.print("\n[Move,New,Restart,Hint,Solve,Quit]\n>");
                    }

                    case "Hint", "hint" -> {
                        model.hint();
                        update(model, null); //TODO
                        System.out.print("\n[Move,New,Restart,Hint,Solve,Quit]\n>");
                    }

                    case "Solve", "solve" -> {
                        model.solve();
                        update(model, null); //TODO
                        System.out.print("\n[Move,New,Restart,Hint,Solve,Quit]\n>");
                    }
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * The main command loop.
     */
    public static void main(String[] args) throws FileNotFoundException {
        SoltrChessPTUI ptui = new SoltrChessPTUI(args[1]); //add filename
        ptui.run();
    }
}