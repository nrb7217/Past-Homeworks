package soltrchess.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import soltrchess.SoltrChess;
import soltrchess.model.Observer;
import soltrchess.model.SoltrChessModel;

import java.io.FileNotFoundException;
import java.util.List;

public class SoltrChessGUI extends Application implements Observer<SoltrChessModel, String> {
    /**
     * the board is the Model
     */
    public SoltrChessModel model;
    private final Image bishop = new Image(getClass().getResourceAsStream("resources/bishop.png"));
    private final Image king = new Image(getClass().getResourceAsStream("resources/king.png"));
    private final Image knight = new Image(getClass().getResourceAsStream("resources/knight.png"));
    private final Image pawn = new Image(getClass().getResourceAsStream("resources/pawn.png"));
    private final Image queen = new Image(getClass().getResourceAsStream("resources/queen.png"));
    private final Image rook = new Image(getClass().getResourceAsStream("resources/rook.png"));
    private final Image dark = new Image(getClass().getResourceAsStream("resources/dark.png"));
    private final Image light = new Image(getClass().getResourceAsStream("resources/light.png"));
    private final BackgroundImage white = new BackgroundImage(light, BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            BackgroundSize.DEFAULT);
    private final BackgroundImage blue = new BackgroundImage(dark, BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            BackgroundSize.DEFAULT);
    private final Background whiteBackground = new Background(white);
    private final Background blueBackground = new Background(blue);


    private Label top = new Label("Game File: ");
    private chessButton[][] buttons = new chessButton[10][10];


    /*
     ******************* THE VIEW SECTION ***************************************
     */
    //Initialization
    @Override
    public void init() throws FileNotFoundException {
        // create the model and add ourselves as an observer
        Parameters params = getParameters();
        String filename=params.getUnnamed().get(0);
        model = new SoltrChessModel(filename, null);
        model.addObserver(this);



    }

    //update
    @Override
    //labelUpdate is message displayed
    public void update(SoltrChessModel subject, String labelUpdate) {
        this.model = subject;
        this.top.setText(labelUpdate);
        //buttons[model.getB()[0]][model.getB()[1]].changeImage();
        //buttons[model.getA()[0]][model.getA()[1]].changeImage();
        for (int row = 0; row < SoltrChessModel.ROWS; row++) {
            for (int col = 0; col < SoltrChessModel.COLS; col++) {
                buttons[col][row].changeImage();
            }
        }
    }

    /*
     ******************* THE CONTROLLER SECTION *********************************
     */
    @Override
    public void start(Stage stage) throws Exception {
        // create the border panes that holds the grid and labels
        BorderPane borderPane = new BorderPane();
        HBox footer = new HBox(4);
        HBox header = new HBox(1);
        top = new Label("Game File: " + model.filename);

        // Set Main Boarder pane components
        borderPane.setCenter(makeGridPane());
        borderPane.setBottom(footer);
        header.getChildren().add(top);
        header.setAlignment(Pos.CENTER);
        footer.setAlignment(Pos.CENTER);
        borderPane.setTop(header);


        //Add buttons to footer
        Button NewGameButton = new Button("New Game");
        NewGameButton.setOnAction(event -> {
            try {
                model.newGame(stage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        Button RestartButton = new Button("Restart");
        RestartButton.setOnAction(event -> model.restart());
        Button SolveButton = new Button("Solve");
        RestartButton.setOnAction(event -> model.solve());
        Button HintButton = new Button("Hint");
        RestartButton.setOnAction(event -> model.hint());
        footer.getChildren().addAll(NewGameButton, RestartButton, SolveButton,HintButton);


        // store the grid into the scene and display it
        Scene scene = new Scene(borderPane);
        stage.setTitle("Solitaire Chess -- Nathan Belcher and Keegan Kresge");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Make grid of discButtons
     *
     * @return gridpane
     */
    public GridPane makeGridPane() {
        GridPane gridpane = new GridPane();
        gridpane.addColumn(SoltrChessModel.COLS);
        gridpane.addRow(SoltrChessModel.ROWS);
        for (int x = 0; x < SoltrChessModel.COLS; x++) {
            for (int y = 0; y < SoltrChessModel.ROWS; y++) {
                chessButton disc = new chessButton(x, y);
                disc.setOnAction(event -> disc.callMakeMove());
                gridpane.add(disc, y, x); //flipped x y
                buttons[y][x] = disc;
            }
        }
        for (int row = 0; row < SoltrChessModel.ROWS; row++) {
            for (int col = 0; col < SoltrChessModel.COLS; col++) {
                buttons[col][row].changeImage();
            }
        }
        return gridpane;
    }


    //public static void main(String[] args) {
    //    Application.launch(args);
    //}


    private class chessButton extends Button {

        /**
         * Position of Button
         */
        private final int col;
        private final int row;



        /**
         * Create the button with the image based on the ownership.
         **/
        chessButton(int col, int row) {
            if ((col + row) % 2 == 0) {
                this.setBackground(whiteBackground);
            } else {
                this.setBackground(blueBackground);
            }
            this.col = row;
            this.row = col;


        }

        /**
         * Change the image of the button to reflect the board layout
         */
        public void changeImage() {
            SoltrChessModel.PIECE piece = model.getContents(row, col);
            if (piece == SoltrChessModel.PIECE.BISHOP) {
                this.setButtonIcon(bishop);
            } else if (piece == SoltrChessModel.PIECE.KING) {
                this.setButtonIcon(king);
            } else if (piece == SoltrChessModel.PIECE.KNIGHT) {
                this.setButtonIcon(knight);
            } else if (piece == SoltrChessModel.PIECE.PAWN) {
                this.setButtonIcon(pawn);
            } else if (piece == SoltrChessModel.PIECE.ROOK) {
                this.setButtonIcon(rook);
            } else if (piece == SoltrChessModel.PIECE.QUEEN) {
                this.setButtonIcon(queen);
            } else{
                if ((col + row) % 2 == 0) {
                    this.setButtonIcon(light);
                } else {
                    this.setButtonIcon(dark);
                }
            }
        }
        private void setButtonIcon( Image img) {
            ImageView icon = new ImageView(img);
            icon.setFitWidth(120);
            icon.setFitHeight(120);
            this.setGraphic(icon);
        }

        public void callMakeMove() {
            model.makeMove(col, row);
        }

    }

}