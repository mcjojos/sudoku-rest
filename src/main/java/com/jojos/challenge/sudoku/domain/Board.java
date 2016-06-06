package com.jojos.challenge.sudoku.domain;

import java.util.Arrays;

/**
 * A wrapper class for the sudoku 2D 9x9 board of short numbers.
 *
 * Created by karanikasg@gmail.com.
 */
public class Board {
    private final short[][] values;

    public Board(short[][] values) {
        this.values = values;
    }

    public short[][] getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "Board{" +
                "values=" + Arrays.deepToString(values) +
                '}';
    }
}
