package com.jojos.challenge.sudoku.utils;

/**
 * Static helper methods and fields
 * <p>
 * Created by karanikasg@gmail.com.
 */
public class SudokuUtils {
    private static final short[][] DEFAULT_SUDOKU = {
            {7, 0, 0,    0, 4, 0,    5, 3, 0},
            {0, 0, 5,    0, 0, 8,    0, 1, 0},
            {0, 0, 8,    5, 0, 9,    0, 4, 0},

            {5, 3, 9,    0, 6, 0,    0, 0, 1},
            {0, 0, 0,    0, 1, 0,    0, 0, 5},
            {8, 0, 0,    7, 2, 0,    9, 0, 0},

            {9, 0, 7,    4, 0, 0,    0, 0, 0},
            {0, 0, 0,    0, 5, 7,    0, 0, 0},
            {6, 0, 0,    0, 0, 0,    0, 5, 0}};

    // that will help us having a static access for "square indexes" rather than calculating them each time
    public static final short[][] SQUARE_INDEXES = {
            {0, 0, 0,    1, 1, 1,    2, 2, 2},
            {0, 0, 0,    1, 1, 1,    2, 2, 2},
            {0, 0, 0,    1, 1, 1,    2, 2, 2},

            {3, 3, 3,    4, 4, 4,    5, 5, 5},
            {3, 3, 3,    4, 4, 4,    5, 5, 5},
            {3, 3, 3,    4, 4, 4,    5, 5, 5},

            {6, 6, 6,    7, 7, 7,    8, 8, 8},
            {6, 6, 6,    7, 7, 7,    8, 8, 8},
            {6, 6, 6,    7, 7, 7,    8, 8, 8}};

    public static short[][] getSudokuTable() {
        return DEFAULT_SUDOKU;
    }

}
