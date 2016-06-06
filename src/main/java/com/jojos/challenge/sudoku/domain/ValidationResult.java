package com.jojos.challenge.sudoku.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jojos.challenge.sudoku.utils.SudokuUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The object that
 * <p>
 * Created by karanikasg@gmail.com.
 */
public class ValidationResult {
    private final short[][] board;
    private final ValidationStatus validationStatus;
    private final State state;
    private final List<String> invalidityWarnings;

    @JsonCreator
    private ValidationResult(@JsonProperty("board") short[][] board,
                             @JsonProperty("validationStatus") ValidationStatus validationStatus,
                             @JsonProperty("state") State state,
                             @JsonProperty("invalidityWarnings") List<String> invalidityWarnings) {
        this.board = board;
        this.validationStatus = validationStatus;
        this.state = state;
        this.invalidityWarnings = invalidityWarnings;
    }

    private ValidationResult(ValidationResultBuilder builder) {
        this.board = builder.board;
        this.validationStatus = builder.validationStatus;
        this.state = builder.state;
        this.invalidityWarnings = builder.invalidityWarnings;
    }

    public static ValidationResult finished(short[][] board, State state) {
        return new ValidationResult(board, ValidationStatus.FINISHED, state, Collections.emptyList());
    }

    public static ValidationResult of(short[][] board, State state, List<String> invalidityWarnings) {
        if (invalidityWarnings.isEmpty()) {
            return new ValidationResult(board, ValidationStatus.VALID, state, Collections.emptyList());
        } else {
            return new ValidationResult(board, ValidationStatus.INVALID, state, invalidityWarnings);
        }
    }

    public short[][] getBoard() {
        return board;
    }

    public ValidationStatus getValidationStatus() {
        return validationStatus;
    }

    public State getState() {
        return state;
    }

    public List<String> getInvalidityWarnings() {
        return invalidityWarnings;
    }

    @Override
    public String toString() {
        return "ValidationResult{" +
                "board=" + Arrays.deepToString(board) +
                ", validationStatus=" + validationStatus +
                ", state=" + state +
                ", invalidityWarnings=" + invalidityWarnings +
                '}';
    }

    public enum ValidationStatus {
        VALID,
        INVALID,
        FINISHED
    }

    public enum State {
        BEFORE_MOVE,
        AFTER_MOVE
    }

    public static class ValidationResultBuilder {
        private short[][] board = SudokuUtils.getSudokuTable();
        private ValidationStatus validationStatus = ValidationStatus.VALID;
        private State state = State.BEFORE_MOVE;
        private List<String> invalidityWarnings = Collections.emptyList();

        public ValidationResultBuilder board(short[][] board) {
            this.board = board;
            return this;
        }

        public ValidationResultBuilder validationStatus(ValidationStatus validationStatus) {
            this.validationStatus = validationStatus;
            return this;
        }

        public ValidationResultBuilder state(State state) {
            this.state = state;
            return this;
        }

        public ValidationResultBuilder invalidityWarnings(List<String> invalidityWarnings) {
            this.invalidityWarnings = invalidityWarnings;
            return this;
        }

        public ValidationResult build() {
            return new ValidationResult(this);
        }
    }

}
