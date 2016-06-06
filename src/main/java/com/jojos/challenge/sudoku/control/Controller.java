package com.jojos.challenge.sudoku.control;

import com.jojos.challenge.sudoku.domain.Board;
import com.jojos.challenge.sudoku.domain.ErrorInfo;
import com.jojos.challenge.sudoku.domain.SudokuMove;
import com.jojos.challenge.sudoku.domain.ValidationResult;
import com.jojos.challenge.sudoku.service.Validator;
import com.jojos.challenge.sudoku.utils.SudokuUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Out main and only controller which shall be used by Spring MVC to handle web request to the following addresses
 * HTTP GET /sudoku/table to request a new table
 * HTTP POST /sudoku/validate -d {sudoku table, number, point}
 *
 * Created by karanikasg@gmail.com.
 */
@RestController
@RequestMapping("/sudoku")
public class Controller {

    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    @Autowired
    Validator sudokuValidator;

    @RequestMapping(value = "/table", method = RequestMethod.GET)
    public Board getNewTable() {
        log.info("GET request on /table");
        Board board = new Board(SudokuUtils.getSudokuTable());
        log.info("Returning {}", board);
        return board;
    }

    @RequestMapping(value = "/validate", method = RequestMethod.PUT)
    public ValidationResult validate(@RequestBody SudokuMove sudokuMove) throws ApplicationException {
        log.info("PUT Request {}", sudokuMove.toString());
        ValidationResult validationResult = sudokuValidator.validateSudokuMove(sudokuMove);
        log.info("Returning {}", validationResult);
        return validationResult;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    ErrorInfo handleBadRequest(HttpServletRequest req, Exception ex) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST, req.getRequestURL().toString(), ex);
        log.error("Handling exception and returning error {}", errorInfo.toString());
        return errorInfo;
    }

}
