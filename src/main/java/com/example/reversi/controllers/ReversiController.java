package com.example.reversi.controllers;

import com.example.reversi.game.*;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;


public class ReversiController {
    @FXML
    private Button newGameButton;
    @FXML
    private Text textWinner;
    @FXML
    private Button helpButton;
    @FXML
    private Canvas canvas;
    @FXML
    private Text textPlayerTurn;
    @FXML
    private Pane winnerAnouncementPane;

    private GameCanvas gameCanvas;
    private Reversi reversi;

    private boolean displayHelpMoves;

    private void changeTextPlayerTurn() {
        if (this.reversi.getCurrentPlayer().getValueOfPlayer() == Player.BLUE) {
            this.textPlayerTurn.setText("Blue's turn");
        } else {
            this.textPlayerTurn.setText("Red's turn");
        }
    }
    private BoardPoint getArrayIndexFromMouseClick(double xMouse, double yMouse) {
        int x = (int) Math.floor(xMouse);
        x -= (int) (x % (gameCanvas.getWidthField()));
        x = (int) (x / (gameCanvas.getWidthField()));

        int y = (int) Math.floor(yMouse);
        y -= (int) (y % (gameCanvas.getHeightField()));
        y = (int) (y / (gameCanvas.getHeightField()));

        return new BoardPoint(x, y);
    }

    @FXML
    public void doTurn(MouseEvent mouseEvent) {
        BoardPoint boardPoint = getArrayIndexFromMouseClick(mouseEvent.getX(), mouseEvent.getY());


        if (!this.reversi.validTurn(boardPoint)) {
            return;
        }

        this.reversi.removeHelpFields();

        changePlayer();

        if (this.displayHelpMoves) {
            this.reversi.markHelpMoves();
        }

        this.gameCanvas.drawBoard(this.reversi.getBoard());

        if (this.reversi.hasGameEnded()) {
            this.gameHasEnded();
        }
    }

    private void gameHasEnded() {
        int blueStones = this.reversi.getBoard().countPieces(this.reversi.getPlayers().get(Player.BLUE));
        int redStones = this.reversi.getBoard().countPieces(this.reversi.getPlayers().get(Player.RED));
        Player winner = blueStones > redStones ? this.reversi.getPlayers().get(Player.BLUE) : this.reversi.getPlayers().get(Player.RED);

        this.announceWinner(winner, blueStones, redStones);
    }

    private void changePlayer() {
        var player = this.reversi.getCurrentPlayer().otherPlayer(this.reversi.getPlayers());
        this.reversi.setCurrentPlayer(player);
        this.changeTextPlayerTurn();
    }

    @FXML
    private void helpButtonPressed() {
        this.displayHelpMoves = !this.displayHelpMoves;
        if (this.displayHelpMoves) {
            this.reversi.markHelpMoves();
        } else {
            this.reversi.getBoard().removeHelpFields();
        }

        this.gameCanvas.drawBoard(this.reversi.getBoard());
    }

    private void announceWinner(Player winner, int blueCount, int redCount) {
        this.winnerAnouncementPane.setLayoutX(200);
        this.winnerAnouncementPane.setLayoutY(200);
        if (winner.getValueOfPlayer() == Player.BLUE) {
            this.textWinner.setText("Player with the blue stones has won" + " blue stone count: " + blueCount + " red stone count: " + redCount);
        } else if (winner.getValueOfPlayer() == Player.RED) {
            this.textWinner.setText("Player with the red stones has won" + " blue stone count: " + blueCount + " red stone count: " + redCount);
        } else {
            this.textWinner.setText("It's a draw");
        }
    }

    @FXML
    private void newGame() {
        this.reversi = new Reversi(Reversi.AMOUNT_OF_HORIZONTAL_FIELDS, Reversi.AMOUNT_OF_VERTICAL_FIELDS);
        this.gameCanvas.drawBoard(reversi.getBoard());
        this.displayHelpMoves = false;
        this.changeTextPlayerTurn();
    }

    @FXML
    public void initialize() {
        this.reversi = new Reversi(Reversi.AMOUNT_OF_HORIZONTAL_FIELDS, Reversi.AMOUNT_OF_VERTICAL_FIELDS);
        this.gameCanvas = new GameCanvas(canvas, canvas.getWidth(),canvas.getHeight(), reversi.getBoard());
        this.newGame();
    }
}