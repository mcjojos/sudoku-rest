package com.jojos.challenge.sudoku.service;


import com.jojos.challenge.sudoku.domain.SudokuMove;
import com.jojos.challenge.sudoku.domain.ValidationResult;

/**
 * Offer sudoku validation services in regards to a sudoku board and a move.
 *
 * Created by karanikasg@gmail.com.
 */
public interface Validator {

    ValidationResult validateSudokuMove(SudokuMove sudokuMove);

}
