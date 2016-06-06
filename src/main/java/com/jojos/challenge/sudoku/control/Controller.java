package com.jojos.challenge.sudoku.control;

import com.jojos.challenge.sudoku.domain.Board;
import com.jojos.challenge.sudoku.domain.ErrorInfo;
import com.jojos.challenge.sudoku.domain.SudokuMove;
import com.jojos.challenge.sudoku.domain.ValidationResult;
import com.jojos.challenge.sudoku.service.Validator;
import com.jojos.challenge.sudoku.utils.SudokuUtils;
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

    @Autowired
    Validator sudokuValidator;

    @RequestMapping(value = "/table", method = RequestMethod.GET)
    public Board getNewTable() {
        return new Board(SudokuUtils.getSudokuTable());
    }

    @RequestMapping(value = "/validate", method = RequestMethod.PUT)
    public ValidationResult validate(@RequestBody SudokuMove sudokuMove) throws ApplicationException {
//        System.out.println(sudokuMove);
        return sudokuValidator.validateSudokuMove(sudokuMove);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    ErrorInfo handleBadRequest(HttpServletRequest req, Exception ex) {
        return new ErrorInfo(HttpStatus.BAD_REQUEST, req.getRequestURL().toString(), ex);
    }

}
