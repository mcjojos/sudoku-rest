package com.jojos.challenge.sudoku.service;

import com.jojos.challenge.sudoku.control.ApplicationException;
import com.jojos.challenge.sudoku.domain.Point;
import com.jojos.challenge.sudoku.domain.SudokuMove;
import com.jojos.challenge.sudoku.domain.ValidationResult;
import com.jojos.challenge.sudoku.utils.SudokuUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.jojos.challenge.sudoku.domain.ValidationResult.ValidationStatus;
import static com.jojos.challenge.sudoku.domain.ValidationResult.State;

/**
 * Implementation of the validation interface in regards to a sudoku board and a move
 * It's necessary to define a proxy mode for this implementation which is either
 * {@link ScopedProxyMode#TARGET_CLASS} or {@link ScopedProxyMode#INTERFACES} the reason being
 * that we want to inject this class with a HTTP request scope into another longer-lived scope
 * Since our class is implementing an interface we choose to use the second option
 * for the sake of using standard APIs vs external libraries (JDK dynamic proxy vs CGLIB).
 * This is also the recommended way to go based on the spring framework reference please see
 * <a href="http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#aop-proxying">
 *     http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#aop-proxying</a>
 *
 *
 * @implNote Three secondary boolean arrays are created one for rows columns and squares.
 * Their indexes represent the values that got placed into that row, column or square.
 *
 * Created by karanikasg@gmail.com.
 */
@Component
@Scope(value="request", proxyMode = ScopedProxyMode.INTERFACES)
public class DefaultValidator implements Validator {

    private static final Logger log = LoggerFactory.getLogger(DefaultValidator.class);

    /*
    The following three arrays are classified as "secondary" or "helper" arrays,
    in the sense that they should help us with the validations.
    Create an array of booleans for every row, column and square.
    Their indexes represent the values that got placed into that row, column or square.
    For example if we have a 4 to fifth row, second column, we should set rows[5][4] , columns[2][4] and squares[3][4] to true,
    to mark that the row, column, and square have a 4 value. Check also the {@link #SQUARE_INDEXES} array.
    That way it becomes an O(1) operation to determine for any given value if it's a duplicate on a row, column or square level
     */
    private final boolean[][] rows = new boolean[9][9];
    private final boolean[][] columns = new boolean[9][9];
    private final boolean[][] squares = new boolean[9][9];

    private short totalValidNumbers = 0;

    private ValidationResult boardValidationResult;

    @Override
    public ValidationResult validateSudokuMove(SudokuMove sudokuMove) throws ApplicationException {

        // set the boards
        boardValidationResult = resolveAndValidateSecondaryArrays(sudokuMove.getBoard());

        // this is the status BEFORE the move. It's entirely possible that the board in question is
        // already finished or invalid. Don't validate the move in that case.
        ValidationStatus status = boardValidationResult.getValidationStatus();

        // Don't validate any move on a board that is already finished or invalid
        if (!isFinished() && status != ValidationStatus.INVALID) {
            log.debug("Proceed with movement validation");
            short[][] board = sudokuMove.getBoard();
            short number = sudokuMove.getNumber();
            Point point = sudokuMove.getPoint();
            int row = point.getRow();
            int column = point.getColumn();

            // any reported state after this point is a {@link State#AFTERMOVE}
            State state = State.AFTER_MOVE;
            // first check if the valid board contains any element at this point
            if (board[row][column] != 0) {
                String warnMsg = String.format("Board already contains number %d at %s", board[row][column], point);
                log.warn(warnMsg);
                boardValidationResult = ValidationResult.of(board, state, Collections.singletonList(warnMsg));
            } else {
                // make our number value a zero-based index for convenience accessing our tables
                short indexedNumber = (short) (number - 1);

                List<String> invalidityWarnings = new ArrayList<>();
                if (rows[row][indexedNumber]) {
                    String warnMsg = String.format("Board already contains the same number %d at row %d", number, row);
                    log.warn(warnMsg);
                    invalidityWarnings.add(warnMsg);
                }
                if (columns[column][indexedNumber]) {
                    String warnMsg = String.format("Board already contains the same number %d at column %d", number, column);
                    log.warn(warnMsg);
                    invalidityWarnings.add(warnMsg);
                }

                // identify the square that our rows and columns map to our sudoku board
                short squareIndex = SudokuUtils.SQUARE_INDEXES[row][column];

                if (squares[squareIndex][indexedNumber]) {
                    String warnMsg = String.format("Board already contains the same number %d at square %d", number, squareIndex);
                    log.warn(warnMsg);
                    invalidityWarnings.add(warnMsg);
                }

                if (invalidityWarnings.isEmpty()) {
                    board[row][column] = number;
                    rows[row][indexedNumber] = true;
                    columns[column][indexedNumber] = true;
                    squares[squareIndex][indexedNumber] = true;
                    ++totalValidNumbers;
                }

                if (isFinished()) {
                    boardValidationResult = ValidationResult.finished(board, state);
                } else {
                    boardValidationResult = ValidationResult.of(board, state, invalidityWarnings);
                }
            }
        }

        return boardValidationResult;
    }

	/**
     * Validate the board for validity and resolve the secondary arrays before any move validation.
     * @param board the sudoku board in question
     * @return a validation result having always a state of {State#BEFORE_MOVE}. The status can be FINISHED, VALID or INVALID
     */
    private ValidationResult resolveAndValidateSecondaryArrays(short[][] board) {
        log.debug("Setting secondary arrays");

        List<String> invalidityReasons = new ArrayList<>();

        for (short i = 0; i < board.length; i++) {
            for (short j = 0; j < board[i].length; j++) {
                // at this point permitted values for the indexValue are considered 0-9
                short value = board[i][j];
                if (!isPermittedValue(value)) {
                    String errorMsg = String.format("Value %d on board is not between permitted values", value);
                    log.error(errorMsg);
                    throw new ApplicationException(errorMsg);
                } else if (value != 0) {   // skip 0 values - they are permitted but indicate no-value

                    // set the indexValue to be zero-based for convenience
                    short indexValue = (short)(value - 1);

                    if (rows[i][indexValue] == false) {
                        rows[i][indexValue] = true;
                    } else {
                        String warnMsg = String.format("Duplicate value %d found in %d row", value, i);
                        log.warn(warnMsg);
                        invalidityReasons.add(warnMsg);
                    }

                    if (columns[j][indexValue] == false) {
                        columns[j][indexValue] = true;
                    } else {
                        String warnMsg = String.format("Duplicate value %d found in %d column", value, j);
                        log.warn(warnMsg);
                        invalidityReasons.add(warnMsg);
                    }

                    // this line over here maps (i, j) points to squares in our sudoku board
                    short squareIndex = SudokuUtils.SQUARE_INDEXES[i][j];

                    if (squares[squareIndex][indexValue] == false) {
                        squares[squareIndex][indexValue] = true;
                        ++totalValidNumbers;
                    } else {
                        String warnMsg = String.format("Duplicate value %d found in %d square", value, squareIndex);
                        log.warn(warnMsg);
                        invalidityReasons.add(warnMsg);
                    }
                }
            }
        }

        if (isFinished()) {
            return ValidationResult.finished(board, State.BEFORE_MOVE);
        } else {
            return ValidationResult.of(board, State.BEFORE_MOVE, invalidityReasons);
        }
    }

    /**
     * We are finished if we have exactly 81 valid numbers is our board
     * or we have marked the status explicitly to finished.
     * @return true if the board is considered finished, false otherwise.
     */
    private boolean isFinished() {
        return (totalValidNumbers == 81) ||
                (boardValidationResult != null &&
                        boardValidationResult.getValidationStatus() == ValidationStatus.FINISHED);
    }

    /**
     * Helper class to check if some value belonging to the board is permitted or not
     * @param value to check
     * @return true if the value is between 0-9 (both inclusive)
     */
    private boolean isPermittedValue(short value) {
        return value >= 0 && value <= 9;
    }

}
