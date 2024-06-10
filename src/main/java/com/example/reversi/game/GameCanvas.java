package com.example.reversi.game;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameCanvas {
    private final GraphicsContext gc;

    private final double widthGame;
    private final double heightGame;

    private final double widthField;
    private final double heightField;

    private final double heightStone;
    private final double widthStone;

    private static final double LINE_THICKNESS = 2.0;

    public GameCanvas(Canvas canvas, double widthGame, double heightGame, Board board) {
        this.widthGame = widthGame;
        this.heightGame = heightGame;

        this.widthField = widthGame / board.getAmountOfVerticalFields();
        this.heightField = heightGame / board.getAmountOfHorizontalFields();

        this.heightStone = heightField - (heightField / 10);
        this.widthStone = widthField - (widthField / 10);

        this.gc = canvas.getGraphicsContext2D();
    }

    public void drawLines(Board board) {
        this.gc.setStroke(Color.BLACK);
        this.gc.setLineWidth(2);

        for (double x = 0; x <= board.getAmountOfHorizontalFields(); x++) {
            gc.strokeLine(x * this.widthField, 0, x * this.widthField, heightGame);
        }

        for (double y = 0; y <= board.getAmountOfVerticalFields(); y++) {
            gc.strokeLine(0, y * this.heightField, widthGame, y * this.heightField);
        }
    }

    public void drawBoard(Board board) {
        drawFields(board);
        drawLines(board);
    }

    public void drawFields(Board board) {
        for (Field field : board.getFields()) {
            drawField(field);
        }
    }

    public void drawField(Field field) {
        BoardPoint boardPoint = field.getBoardPoint();
        FieldContent fieldContent = field.getFieldContent();

        switch (fieldContent) {
            case EMPTY -> {
                gc.setFill(Color.WHITE);
                gc.setStroke(Color.WHITE);
                drawEmptyField(boardPoint);
            }
            case BLUE -> {
                gc.setFill(Color.BLUE);
                gc.setStroke(Color.BLUE);
                drawFilledCircle(boardPoint);

            }
            case RED -> {
                gc.setFill(Color.RED);
                gc.setStroke(Color.RED);
                drawFilledCircle(boardPoint);

            }
            case HELP -> {
                gc.setStroke(Color.BLACK);
                gc.setFill(Color.WHITE);
                drawNonFilledCircle(boardPoint);
            }
        }
    }

    private void drawEmptyField(BoardPoint boardPoint) {
        int x = boardPoint.x();
        int y = boardPoint.y();
        gc.fillRect(x * widthField - LINE_THICKNESS,
                y * heightField - LINE_THICKNESS,
                    widthField, heightField);
    }

    public void drawFilledCircle(BoardPoint boardPoint) {
        drawCircle(boardPoint, true);
    }

    private void drawNonFilledCircle(BoardPoint boardPoint) {
        drawCircle(boardPoint, false);
    }

    private void drawCircle(BoardPoint boardPoint, boolean filledCircle) {
        int x = boardPoint.x();
        int y = boardPoint.y();
        double xOffset = (widthField - widthStone) / 2;
        double yOffset = (heightField - heightStone) / 2;
        if (filledCircle) {
            gc.fillOval(x * this.widthField + xOffset,
                    y * this.heightField + yOffset,
                        widthStone, heightStone);
        } else {
            gc.strokeOval(x * this.widthField + xOffset,
                    y * this.heightField + yOffset,
                    widthStone, heightStone);
        }
    }
}
