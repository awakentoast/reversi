package com.example.reversi.game;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Reversi {
    private Board board;
    private Players players;
    private Player currentPlayer;

    public static final int AMOUNT_OF_HORIZONTAL_FIELDS = 8;
    public static final int AMOUNT_OF_VERTICAL_FIELDS = 8;

    private void changeCurrentPlayerToOtherPlayer() {
        this.currentPlayer = this.currentPlayer.otherPlayer(this.players);
    }

    public Reversi(int amountOfHorizontalFields, int amountOfVerticalFields) {
        this.board = new Board(amountOfHorizontalFields, amountOfVerticalFields);
        this.players = new Players();
        players.addPlayer(new Player(Player.BLUE));
        players.addPlayer(new Player(Player.RED));
        this.currentPlayer = players.get(Player.BLUE);
    }

    public boolean isValidMove(Field field) {
        int[] xDirections = {0, 1, 1, 1, 0, -1, -1, -1};
        int[] yDirections = {-1, -1, 0, 1, 1, 1, 0, -1};

        List<Boolean> validDirections = new ArrayList<>(8);
        for (int direction = 0; direction < 8; direction++) {
            var amountOfStonesThatCanBeTurnedInThisDirection = amountOfStonesThatCanBeTurnedInGivenDirection(field, xDirections[direction], yDirections[direction]);
            validDirections.add(amountOfStonesThatCanBeTurnedInThisDirection != 0);
        }

        return validDirections.stream().anyMatch(bool -> bool);
    }

    public List<Field> getValidMoves() {
        List<Field> validMoves = new ArrayList<>();

        for (int y = 0; y < this.board.getAmountOfVerticalFields(); y++) {
            for (int x = 0; x < this.board.getAmountOfHorizontalFields(); x++) {
                Field field = this.board.getField(new BoardPoint(x, y));
                if (isValidMove(field)) {
                    validMoves.add(field);
                }
            }
        }

        return validMoves;
    }

    public void markHelpMoves() {
        var fieldsThatNeedToBeMarkedHelp = this.board.getFields().stream().filter(field -> {
            FieldContent fieldContent = field.getFieldContent();
            if (fieldContent == FieldContent.BLUE || fieldContent == FieldContent.RED) {
                return false;
            }

            return this.isValidMove(field);
        });

        fieldsThatNeedToBeMarkedHelp.forEach(field -> field.setFieldContent(FieldContent.HELP));
    }

    public int amountOfStonesThatCanBeTurnedInGivenDirection(Field field, int xOffset, int yOffset) {
        if (field.getFieldContent() == FieldContent.BLUE || field.getFieldContent() == FieldContent.RED) {
            return 0;
        }

        var fieldContentOtherPlayer = this.currentPlayer.otherPlayer(players).getCorrespondingFieldContent();
        var x = field.getBoardPoint().x();
        var y = field.getBoardPoint().y();
        var stonesToBeTurned = 0;
        var endedOnOwnColour = false;
        while (true) {
            x += xOffset;
            y += yOffset;
            BoardPoint currentBoardPoint = new BoardPoint(x, y);

            if (!isInBounds(currentBoardPoint)) {
                break;
            }
            var currentFieldContent = board.getField(currentBoardPoint).getFieldContent();

            if (currentFieldContent == FieldContent.EMPTY || currentFieldContent == FieldContent.HELP) {
                break;
            }

            if (currentFieldContent == currentPlayer.getCorrespondingFieldContent()) {
                endedOnOwnColour = true;
            }

            if (currentFieldContent != fieldContentOtherPlayer) {
                break;
            }

            stonesToBeTurned++;
        }
        return endedOnOwnColour ? stonesToBeTurned : 0;
    }

    private boolean isInBounds(BoardPoint boardPoint) {
        var horizontalBound = boardPoint.x() >= 0 && boardPoint.x() < this.board.getAmountOfHorizontalFields();
        var verticalBound = boardPoint.y() >= 0 && boardPoint.y() < this.board.getAmountOfVerticalFields();

        return (horizontalBound && verticalBound);
    }



    public boolean isThereStillAPossibleMove() {
        var validMoves = this.getValidMoves();
        return !validMoves.isEmpty();
    }

    public boolean validTurn(BoardPoint boardPoint) {
        var field = board.getField(boardPoint);

        if (field.getFieldContent() == (FieldContent.BLUE) ||
                field.getFieldContent() == (FieldContent.RED)) {
            return false;
        }

        var validTurn = false;
        int[] xDirections = {0, 1, 1, 1, 0, -1, -1, -1};
        int[] yDirections = {-1, -1, 0, 1, 1, 1, 0, -1};

        for (int direction = 0; direction < 8; direction++) {
            int xOffset = xDirections[direction];
            int yOffset = yDirections[direction];
            var amount = this.amountOfStonesThatCanBeTurnedInGivenDirection(field, xOffset, yOffset);
            if (amount > 0) {
                validTurn = true;
                board.flipStoneInDirection(this.board, field, this.currentPlayer, amount, xOffset, yOffset);
            }
        }

        return validTurn;
    }

    public boolean hasGameEnded() {
        if (this.isThereStillAPossibleMove()) {
            return false;
        }

        this.changeCurrentPlayerToOtherPlayer();

        if (this.isThereStillAPossibleMove()) {
            return false;
        }

        return true;
    }



    public void removeHelpFields() {
        this.board.getFields().stream().filter(field -> field.getFieldContent() == FieldContent.HELP).forEach(field -> field.setFieldContent(FieldContent.EMPTY));
    }
}
