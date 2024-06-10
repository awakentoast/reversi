package com.example.reversi.game;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Board {
    private int amountOfHorizontalFields;
    private int amountOfVerticalFields;

    private final List<Field> fields;

    public Board(int amountOfHorizontalFields, int amountOfVerticalFields) {
        this.amountOfHorizontalFields = amountOfHorizontalFields;
        this.amountOfVerticalFields = amountOfVerticalFields;
        this.fields = createEmptyBoard();
        this.addStartingStones(amountOfHorizontalFields, amountOfVerticalFields);
    }

    private void addStartingStones(int horizontals, int verticals) {
        int x = horizontals / 2 - 1;
        int y = verticals / 2 - 1;
        BoardPoint point = new BoardPoint(x, y);
        this.getField(point).setFieldContent(FieldContent.BLUE);

        x = horizontals / 2;
        y = verticals / 2 - 1;
        point = new BoardPoint(x, y);
        this.getField(point).setFieldContent(FieldContent.RED);

        x = horizontals / 2 - 1;
        y = verticals / 2;
        point = new BoardPoint(x, y);
        this.getField(point).setFieldContent(FieldContent.RED);

        x = horizontals / 2;
        y =  verticals / 2;
        point = new BoardPoint(x, y);
        this.getField(point).setFieldContent(FieldContent.BLUE);
    }

    private List<Field> createEmptyBoard() {
        List<Field> tempBoard = new ArrayList<>();
        for (int y = 0; y < amountOfVerticalFields; y++) {
            for (int x = 0; x < amountOfVerticalFields; x++) {
                tempBoard.add(new Field(FieldContent.EMPTY, new BoardPoint(x,y)));
            }
        }

        return tempBoard;
    }

    public Field getField(BoardPoint boardPoint) {
        return fields.get(boardPoint.y() * this.amountOfVerticalFields + boardPoint.x());
    }

    public void removeHelpFields() {
        fields.forEach(field -> {
            if (field.getFieldContent() == FieldContent.HELP) {
                field.setFieldContent(FieldContent.EMPTY);
            }
        });
    }

    public void flipStoneInDirection(Board board, Field field, Player currentPlayer, int amountOfStonesToBeTurned, int xOffset, int yOffset) {
        BoardPoint point = field.getBoardPoint();
        field.setFieldContent(currentPlayer.getCorrespondingFieldContent());
        for (int i = 0; i < amountOfStonesToBeTurned; i++) {
            point = new BoardPoint(point.x() + xOffset, point.y() + yOffset);
            field = board.getField(point);
            field.setFieldContent(currentPlayer.getCorrespondingFieldContent());
        }
    }

    public int countPieces(Player player) {
        if (player.getValueOfPlayer() == Player.RED) {
            return (int) this.fields.stream().filter(field -> field.getFieldContent() == FieldContent.RED).count();
        } else {
            return (int) this.fields.stream().filter(field -> field.getFieldContent() == FieldContent.BLUE).count();
        }
    }
}
