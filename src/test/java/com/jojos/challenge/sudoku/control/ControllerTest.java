package com.jojos.challenge.sudoku.control;

import com.jojos.challenge.sudoku.Application;
import com.jojos.challenge.sudoku.domain.Point;
import com.jojos.challenge.sudoku.domain.SudokuMove;
import com.jojos.challenge.sudoku.domain.ValidationResult;
import com.jojos.challenge.sudoku.utils.TestSudokuHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static com.jojos.challenge.sudoku.domain.ValidationResult.ValidationResultBuilder;
import static com.jojos.challenge.sudoku.domain.ValidationResult.ValidationStatus;
import static com.jojos.challenge.sudoku.utils.TestSudokuHelper.APPLICATION_JSON_UTF8;
import static com.jojos.challenge.sudoku.utils.TestSudokuHelper.asJsonString;

/**
 * todo: create javadoc
 * <p>
 * Created by karanikasg@gmail.com.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
        // use this setup with mock objects
        // this.mockMvc = MockMvcBuilders.standaloneSetup(new Controller()).build();
    }

	/**
     * Test requesting a new table
     * curl http://localhost:8080/sudoku/table
     * @throws Exception
     */
    @Test
    public void getTable() throws Exception {
        this.mockMvc.perform(get("/sudoku/table"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.values").isArray());
    }

	/**
     * Test with an invalid sudoku board. It's invalid because number 7 is contained two times in the first row and thus in the first square as well
     * curl -H "Content-Type:application/json" -X PUT http://localhost:8080/sudoku/validate -d "{ \"board\": [[7,7,0,0,4,0,5,3,0],[0,0,5,0,0,8,0,1,0],[0,0,8,5,0,9,0,4,0],[5,3,9,0,6,0,0,0,1],[0,0,0,0,1,0,0,0,3],[8,0,0,7,2,0,9,0,0],[9,0,7,4,0,0,0,0,0],[0,0,0,0,5,7,0,0,0],[6,0,0,0,0,0,0,5,0]], \"number\":3,  \"point\":{\"row\":1, \"column\":1} }"
     * @throws Exception
     */
    @Test
    public void invalidInitialBoard() throws Exception {
        ValidationResult validationResult = new ValidationResultBuilder()
                .board(TestSudokuHelper.INVALID_SUDOKU)
                .state(ValidationResult.State.BEFORE_MOVE)
                .validationStatus(ValidationStatus.INVALID)
                .invalidityWarnings(Arrays.asList("Duplicate value 7 found in 0 row", "Duplicate value 7 found in 0 square"))
                .build();

        Point point = new Point((short)1, (short)1);
        SudokuMove sudokuMove = new SudokuMove(TestSudokuHelper.INVALID_SUDOKU, (short)3, point);

        this.mockMvc.perform(
                put("/sudoku/validate")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(sudokuMove)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.board").isArray())
                .andExpect(jsonPath("$.board[0][0]").value(7))
                .andExpect(jsonPath("$.validationStatus").value("INVALID"))
                .andExpect(jsonPath("$.state").value("BEFORE_MOVE"))
                .andExpect(jsonPath("$.invalidityWarnings[0]").value(validationResult.getInvalidityWarnings().get(0)))
                .andExpect(jsonPath("$.invalidityWarnings[1]").value(validationResult.getInvalidityWarnings().get(1)));

    }

    /**
     * Test with a valid sudoku board but an invalid move of number 5 to row 1 and column 3 (remember rows and columns are 0-based)
     * Putting a number 5 in that position is invalid from every possible aspect:
     * Number five is contained in the board in its equivalent row, column and square
     * curl -H "Content-Type:application/json" -X PUT http://localhost:8080/sudoku/validate -d "{ \"board\": [[7,0,0,0,4,0,5,3,0],[0,0,5,0,0,8,0,1,0],[0,0,8,5,0,9,0,4,0],[5,3,9,0,6,0,0,0,1],[0,0,0,0,1,0,0,0,3],[8,0,0,7,2,0,9,0,0],[9,0,7,4,0,0,0,0,0],[0,0,0,0,5,7,0,0,0],[6,0,0,0,0,0,0,5,0]], \"number\":5,  \"point\":{\"row\":1, \"column\":3} }"
     * @throws Exception
     */
    @Test
    public void invalidBoardAfterMove() throws Exception {
        ValidationResult validationResult = new ValidationResultBuilder()
                .board(TestSudokuHelper.VALID_INITIAL_SUDOKU)
                .state(ValidationResult.State.AFTER_MOVE)
                .validationStatus(ValidationStatus.INVALID)
                .invalidityWarnings(Arrays.asList("Board already contains the same number 5 at row 1", "Board already contains the same number 5 at column 3", "Board already contains the same number 5 at square 1"))
                .build();

        Point point = new Point((short)1, (short)3);
        SudokuMove sudokuMove = new SudokuMove(TestSudokuHelper.VALID_INITIAL_SUDOKU, (short)5, point);

        this.mockMvc.perform(
                put("/sudoku/validate")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(sudokuMove)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.board").isArray())
                .andExpect(jsonPath("$.board[1][3]").value(0))
                .andExpect(jsonPath("$.validationStatus").value("INVALID"))
                .andExpect(jsonPath("$.state").value("AFTER_MOVE"))
                .andExpect(jsonPath("$.invalidityWarnings[0]").value(validationResult.getInvalidityWarnings().get(0)))
                .andExpect(jsonPath("$.invalidityWarnings[1]").value(validationResult.getInvalidityWarnings().get(1)))
                .andExpect(jsonPath("$.invalidityWarnings[2]").value(validationResult.getInvalidityWarnings().get(2)));

    }

    /**
     * Test with a finished sudoku board. The move should not matter.
     * curl -H "Content-Type:application/json" -X PUT http://localhost:8080/sudoku/validate -d "{ "board":[[7,9,2,1,4,6,5,3,8],[4,6,5,2,3,8,7,1,9],[3,1,8,5,7,9,6,4,2],[5,3,9,8,6,4,2,7,1],[2,7,6,9,1,5,4,8,3],[8,4,1,7,2,3,9,6,5],[9,5,7,4,8,1,3,2,6],[1,2,3,6,5,7,8,9,4],[6,8,4,3,9,2,1,5,7]], \"number\":5,  \"point\":{\"row\":1, \"column\":3} }"
     * @throws Exception
     */
    @Test
    public void finishedBoardBeforeMove() throws Exception {
        Point point = new Point((short)1, (short)3);
        SudokuMove sudokuMove = new SudokuMove(TestSudokuHelper.FINISHED_SUDOKU, (short)5, point);

        this.mockMvc.perform(
                put("/sudoku/validate")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(sudokuMove)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.board").isArray())
                .andExpect(jsonPath("$.validationStatus").value("FINISHED"))
                .andExpect(jsonPath("$.state").value("BEFORE_MOVE"));

    }

    /**
     * Test with an almost finished sudoku board and a move that is expected to terminate the sudoku.
     * The last move is expected to be number 7 at the last position (8, 8)
     * curl -H "Content-Type:application/json" -X PUT http://localhost:8080/sudoku/validate -d "{ "board":[[7,9,2,1,4,6,5,3,8],[4,6,5,2,3,8,7,1,9],[3,1,8,5,7,9,6,4,2],[5,3,9,8,6,4,2,7,1],[2,7,6,9,1,5,4,8,3],[8,4,1,7,2,3,9,6,5],[9,5,7,4,8,1,3,2,6],[1,2,3,6,5,7,8,9,4],[6,8,4,3,9,2,1,5,0]], \"number\":7,  \"point\":{\"row\":8, \"column\":8} }"
     */
    @Test
    public void finishedBoardAfterMove() throws Exception {
        Point point = new Point((short)8, (short)8);
        SudokuMove sudokuMove = new SudokuMove(TestSudokuHelper.ALMOST_FINISHED_SUDOKU, (short)7, point);

        this.mockMvc.perform(
                put("/sudoku/validate")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(asJsonString(sudokuMove)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.board").isArray())
                .andExpect(jsonPath("$.validationStatus").value("FINISHED"))
                .andExpect(jsonPath("$.state").value("AFTER_MOVE"));
    }

}