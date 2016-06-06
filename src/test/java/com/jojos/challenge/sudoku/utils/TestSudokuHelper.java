package com.jojos.challenge.sudoku.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import java.nio.charset.Charset;

/**
 * helper static test fields and methods.
 *
 * Created by karanikasg@gmail.com.
 */
public class TestSudokuHelper {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
                                                                        MediaType.APPLICATION_JSON.getSubtype(),
                                                                        Charset.forName("utf8")
    );

    // invalid sudoku board - check the first two elements
    public static final short[][] INVALID_SUDOKU = {
            {7, 7, 0,    0, 4, 0,    5, 3, 0},
            {0, 0, 5,    0, 0, 8,    0, 1, 0},
            {0, 0, 8,    5, 0, 9,    0, 4, 0},

            {5, 3, 9,    0, 6, 0,    0, 0, 1},
            {0, 0, 0,    0, 1, 0,    0, 0, 5},
            {8, 0, 0,    7, 2, 0,    9, 0, 0},

            {9, 0, 7,    4, 0, 0,    0, 0, 0},
            {0, 0, 0,    0, 5, 7,    0, 0, 0},
            {6, 0, 0,    0, 0, 0,    0, 5, 0}};

    public static final short[][] VALID_INITIAL_SUDOKU = {
            {7, 0, 0,    0, 4, 0,    5, 3, 0},
            {0, 0, 5,    0, 0, 8,    0, 1, 0},
            {0, 0, 8,    5, 0, 9,    0, 4, 0},

            {5, 3, 9,    0, 6, 0,    0, 0, 1},
            {0, 0, 0,    0, 1, 0,    0, 0, 5},
            {8, 0, 0,    7, 2, 0,    9, 0, 0},

            {9, 0, 7,    4, 0, 0,    0, 0, 0},
            {0, 0, 0,    0, 5, 7,    0, 0, 0},
            {6, 0, 0,    0, 0, 0,    0, 5, 0}};


    // finished and valid sudoku board
    public static final short[][] FINISHED_SUDOKU = {
            {7, 9, 2,    1, 4, 6,    5, 3, 8},
            {4, 6, 5,    2, 3, 8,    7, 1, 9},
            {3, 1, 8,    5, 7, 9,    6, 4, 2},

            {5, 3, 9,    8, 6, 4,    2, 7, 1},
            {2, 7, 6,    9, 1, 5,    4, 8, 3},
            {8, 4, 1,    7, 2, 3,    9, 6, 5},

            {9, 5, 7,    4, 8, 1,    3, 2, 6},
            {1, 2, 3,    6, 5, 7,    8, 9, 4},
            {6, 8, 4,    3, 9, 2,    1, 5, 7}};

    // a valid sudoku board that is almost completed apart from it's last element
    public static final short[][] ALMOST_FINISHED_SUDOKU = {
            {7, 9, 2,    1, 4, 6,    5, 3, 8},
            {4, 6, 5,    2, 3, 8,    7, 1, 9},
            {3, 1, 8,    5, 7, 9,    6, 4, 2},

            {5, 3, 9,    8, 6, 4,    2, 7, 1},
            {2, 7, 6,    9, 1, 5,    4, 8, 3},
            {8, 4, 1,    7, 2, 3,    9, 6, 5},

            {9, 5, 7,    4, 8, 1,    3, 2, 6},
            {1, 2, 3,    6, 5, 7,    8, 9, 4},
            {6, 8, 4,    3, 9, 2,    1, 5, 0}};

    public static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
