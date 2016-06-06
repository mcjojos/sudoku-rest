package com.jojos.challenge.sudoku.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jojos.challenge.sudoku.control.ApplicationException;

import java.util.Arrays;

/**
 * The input object that contains a 9x9 array of (short) numbers and a move which is represented by the number
 * and the point in the board
 * The number can take values from 0 to 9 (inclusive) with 0 representing the empty value
 * The Point includes two numbers which are zero based and thus they must be between 0-8 (inclusive).
 * Note that the {@link Point} object represents in a 2D array where rows comes first and columns second.
 *
 * Created by karanikasg@gmail.com.
 */
public class SudokuMove {
    private final short[][] board;
    private final short number;
    private final Point point;

    @JsonCreator
    public SudokuMove(@JsonProperty("board") short[][] board,
                      @JsonProperty("number") short number,
                      @JsonProperty("point") Point point) throws ApplicationException {
        checkValidValuesOrThrow(number, point);
        this.board = board;
        this.number = number;
        this.point = point;
    }

    public short[][] getBoard() {
        return board;
    }

    public short getNumber() {
        return number;
    }

    public Point getPoint() {
        return point;
    }

    /**
     * Validation for numbers and points ranges
     * @param number must be between 0-9
     * @param point each x, y of point must be between 1-9
     * @throws ApplicationException if any of the two arguments are out of the permitted values
     */
    private void checkValidValuesOrThrow(int number, Point point) throws ApplicationException {
        if (number < 0 || number > 9) {
            throw new ApplicationException(String.format("Number %d is not between 0-9", number));
        }
        if (point.getRow() < 0 || point.getRow() > 8 ||
                point.getColumn() < 0 || point.getColumn() > 8) {
            throw new ApplicationException(String.format("Point values %s are not between 0-8", point));
        }
    }

    @Override
    public String toString() {
        return "SudokuMove{" +
                "board=" + Arrays.deepToString(board) +
                ", number=" + number +
                ", point=" + point +
                '}';
    }
}
