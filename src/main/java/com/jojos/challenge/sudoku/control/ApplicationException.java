package com.jojos.challenge.sudoku.control;

/**
 * General purpose application specific exception
 *
 * Created by karanikasg@gmail.com.
 */
public class ApplicationException extends RuntimeException {

    public ApplicationException(String message) {
        super(message);
    }
}
