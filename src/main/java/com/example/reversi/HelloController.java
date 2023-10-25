package com.example.reversi;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;


public class HelloController {


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
    private Pane panePopup;

    private GraphicsContext gc;
    private int playersTurnInt;


    private int[][] boardState;

    private boolean displayPossibleMoves;

    private int sizeHorizontalField;
    private int sizeVerticalField;

    private int horizontalFields;
    private int verticalFields;
    private int diameterStone;
    private static final int BLUE_STONE = 1; //player1
    private static final int RED_STONE = 2; //player2
    private static final int HELP_FIELD = 3; //this is a possible move

    private final int[] xDirections = {0, 1, 1, 1, 0, -1, -1, -1};
    private final int[] yDirections = {-1, -1, 0, 1, 1, 1, 0, -1};


    private int otherPlayer() {
        return playersTurnInt == 1 ? 2 : 1;
    }

    private void changeTextPlayerTurn() {
        if (playersTurnInt == BLUE_STONE) {
            textPlayerTurn.setText("Blue's turn");
        } else {
            textPlayerTurn.setText("Red's turn");
        }
    }

    private void drawBoard() {
        for (int y = 0; y < verticalFields; y++) {
            for (int x = 0; x < horizontalFields; x++) {
                drawCircle(x, y, boardState[y][x]);
            }
        }
    }

    @FXML
    public void placeStone(MouseEvent mouseEvent) {
        //gets the upper left corner of the given square
        int x = (int) Math.floor(mouseEvent.getX());
        x -= x % (sizeHorizontalField);
        x = x / (sizeHorizontalField);

        int y = (int) Math.floor(mouseEvent.getY());
        y -= y % (sizeVerticalField);
        y = y / (sizeVerticalField);

        boolean validMove = false;
        if (boardState[y][x] != BLUE_STONE && boardState[y][x] != RED_STONE) {
            for (int direction = 0; direction < 8; direction++) {
                boolean validDirection = checkDirection(x, y, direction);
                if (validDirection) {
                    flipStones(x, y, direction);
                    validMove = true;
                }
            }
        }

        if (validMove) {
            boardState[y][x] = playersTurnInt;
            clearBoardOfHelpFields();
            drawBoard();

            playersTurnInt = otherPlayer();
            checkForEndGame();
        }
    }


    private void flipStones(int x, int y, int direction) {
        int xDirection = xDirections[direction];
        int yDirection = yDirections[direction];
        x += xDirection;
        y += yDirection;
        while (boardState[y][x] != playersTurnInt) {
            boardState[y][x] = playersTurnInt;
            x += xDirection;
            y += yDirection;
        }
    }


    private boolean checkDirection(int x, int y, int direction) {
        int xDirection = xDirections[direction];
        int yDirection = yDirections[direction];
        x += xDirection;
        y += yDirection;
        boolean stoneCanBeTurned = false;
        int lastStone = 0;

        while ((x >= 0 && x < horizontalFields) && (y >= 0 && y < verticalFields) &&
               boardState[y][x] == otherPlayer()) {
            stoneCanBeTurned = true;

            x += xDirection;
            y += yDirection;
        }

        if (x >= 0 && x < horizontalFields && y >= 0 && y < verticalFields) {
            lastStone = boardState[y][x];
        }

        return lastStone == playersTurnInt && stoneCanBeTurned;
    }

    @FXML
    private void helpButtonPressed() {
        displayPossibleMoves = !displayPossibleMoves;

        if (displayPossibleMoves) {
            drawPossibleMoves(getValidMoves());
        } else {
            clearBoardOfHelpFields();
        }
    }

    private void clearBoardOfHelpFields() {
        for (int y = 0; y < verticalFields; y++) {
            for (int x = 0; x < horizontalFields; x++) {
                if (boardState[y][x] == HELP_FIELD) {
                    boardState[y][x] = 0;
                    drawCircle(x, y, 0);
                }
            }
        }
    }


    private ArrayList<int[]> getValidMoves() {
        ArrayList<int[]> validMoves = new ArrayList<>();

        for (int y = 0; y < verticalFields; y++) {
            for (int x = 0; x < horizontalFields; x++) {
                if (boardState[y][x] != BLUE_STONE && boardState[y][x] != RED_STONE) {
                    for (int direction = 0; direction < 8; direction++) {
                        if (checkDirection(x, y, direction)) {
                            validMoves.add(new int[]{x, y});
                            break;
                        }
                    }
                }
            }
        }

        return validMoves;
    }

    private void drawPossibleMoves(ArrayList<int[]> possibleMoves) {
        if (displayPossibleMoves) {
            for (int[] possibleMove : possibleMoves) {
                boardState[possibleMove[1]][possibleMove[0]] = HELP_FIELD;
                drawCircle(possibleMove[0], possibleMove[1], 3);
            }
        }
    }


    private void drawCircle(int x, int y, int stateField) {
        switch (stateField) {
            case 0 -> {
                gc.setFill(Color.WHITE);
                gc.setStroke(Color.WHITE);
            }
            case BLUE_STONE -> {
                gc.setFill(Color.BLUE);
                gc.setStroke(Color.BLUE);
            }
            case RED_STONE -> {
                gc.setFill(Color.RED);
                gc.setStroke(Color.RED);
            }
            case HELP_FIELD -> {
                gc.setStroke(Color.BLACK);
                gc.setFill(Color.WHITE);
            }
        }

        if (stateField == 0) {
            gc.fillRect(x * sizeHorizontalField + 2.0, y * sizeVerticalField + 2.0, sizeHorizontalField - 4.0, sizeVerticalField - 4.0);
        } else if (stateField == 1 || stateField == 2) {
            gc.fillOval(x * sizeHorizontalField + ((double) (sizeHorizontalField - diameterStone) / 2), y * sizeVerticalField + ((double) (sizeVerticalField - diameterStone) / 2), diameterStone, diameterStone);
        } else {
            gc.strokeOval(x * sizeHorizontalField + ((double) (sizeHorizontalField - diameterStone) / 2), y * sizeVerticalField + ((double) (sizeVerticalField - diameterStone) / 2), diameterStone, diameterStone);
        }
    }

    private void checkForEndGame() {
        ArrayList<int[]> validMoves = getValidMoves();
        System.out.println(validMoves.size());
        if (!validMoves.isEmpty()) {
            changeTextPlayerTurn();
            clearBoardOfHelpFields();
            drawPossibleMoves(getValidMoves());
        } else {

            playersTurnInt = otherPlayer();
            ArrayList<int[]> validMoves2 = getValidMoves();
            if (!validMoves2.isEmpty()) {
                changeTextPlayerTurn();
                clearBoardOfHelpFields();
                drawPossibleMoves(getValidMoves());
            } else {
                //game has ended count pieces
                int blueCount = 0;
                int redCount = 0;

                for (int y = 0; y < verticalFields; y++) {
                    for (int x = 0; x < horizontalFields; x++) {
                        if (boardState[y][x] == BLUE_STONE) {
                            blueCount += 1;
                        } else if (boardState[y][x] == RED_STONE) {
                            redCount += 1;
                        }
                    }
                }
                int winner;
                if (blueCount > redCount) {
                    winner = BLUE_STONE;
                } else if (redCount > blueCount) {
                    winner = RED_STONE;
                } else {
                    winner = -1;
                }
                playerHasWon(winner, blueCount, redCount);
            }
        }
    }

    private void playerHasWon(int winner, int blueCount, int redCount) {
        panePopup.setLayoutX(200);
        panePopup.setLayoutY(200);
        if (winner == BLUE_STONE) {
            textWinner.setText("Player with the blue stones has won" + " blue stone count: " + blueCount + " red stone count: " + redCount);
        } else if (winner == RED_STONE) {
            textWinner.setText("Player with the red stones has won" + " blue stone count: " + blueCount + " red stone count: " + redCount);
        } else {
            textWinner.setText("It's a draw");
        }
    }

    @FXML
    private void newGame() {
        panePopup.setLayoutX(-400);
        panePopup.setLayoutX(-200);
        for (int y = 0; y < verticalFields; y++) {
            for (int x = 0; x < horizontalFields; x++) {
                drawCircle(x, y, 0);
                boardState[y][x] = 0;
            }
        }

        playersTurnInt = BLUE_STONE;

        boardState[verticalFields / 2 - 1][horizontalFields / 2 - 1] = BLUE_STONE;
        boardState[verticalFields / 2 - 1][horizontalFields / 2] = RED_STONE;
        boardState[verticalFields / 2][horizontalFields / 2 - 1] = RED_STONE;
        boardState[verticalFields / 2][horizontalFields / 2] = BLUE_STONE;
        displayPossibleMoves = false;

        changeTextPlayerTurn();
        drawBoard();
    }

    @FXML
    public void initialize() {
        horizontalFields = 4;
        verticalFields = 4;

        int boardWidth = (int) canvas.getWidth();
        int boardHeight = (int) canvas.getHeight();

        sizeHorizontalField = boardWidth / horizontalFields;
        sizeVerticalField = boardHeight / verticalFields;

        gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);


        boardState = new int[verticalFields][horizontalFields];
        diameterStone = Math.min(sizeVerticalField, sizeHorizontalField);
        diameterStone -= diameterStone / 10;

        for (double x = 0; x <= boardWidth; x += sizeHorizontalField) {
            gc.strokeLine(x, 0, x, boardHeight);
        }
        for (double y = 0; y <= boardHeight; y += sizeVerticalField) {
            gc.strokeLine(0, y, boardWidth, y);
        }

        newGame();
    }

}