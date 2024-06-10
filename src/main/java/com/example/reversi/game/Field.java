package com.example.reversi.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Field {
    private FieldContent fieldContent;
    private BoardPoint boardPoint;
}
