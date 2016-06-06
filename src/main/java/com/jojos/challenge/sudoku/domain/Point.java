package com.jojos.challenge.sudoku.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is not a geometric point horizontal (X) axis and then vertical (Y) axis.
 * This is a point in a 2D array rows comes first and columns second.
 *
 * Created by karanikasg@gmail.com.
 */
public class Point {
    private final short row;
    private final short column;

    @JsonCreator
    public Point(@JsonProperty("row") short row,
                 @JsonProperty("column") short column) {
        this.row = row;
        this.column = column;
    }

    public short getRow() {
        return row;
    }

    public short getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "Point(" + row + ", " + column + '}';
    }
}
