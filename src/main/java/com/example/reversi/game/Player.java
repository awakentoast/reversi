package com.example.reversi.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player {
    public static final int BLUE = 0;
    public static final int RED = 1;

    private int valueOfPlayer;
    private FieldContent correspondingFieldContent;

    public Player(int valueOfPlayer) {
        this.valueOfPlayer = valueOfPlayer;
        this.correspondingFieldContent = matchingFieldValue(valueOfPlayer);
    }

    private FieldContent matchingFieldValue(int fieldContent) {
        return switch (fieldContent) {
            case BLUE -> FieldContent.BLUE;
            case RED -> FieldContent.RED;
            default -> throw new IllegalStateException("Unexpected value: " + fieldContent);
        };
    }

    public Player otherPlayer(Players players) {
        if (valueOfPlayer == BLUE) {
            return players.get(Player.RED);
        } else {
            return players.get(Player.BLUE);
        }
    }
}