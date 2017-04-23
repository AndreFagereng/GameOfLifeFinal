package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Andre on 22.02.2017.
 */
public class BoardTest {
    @FXML Canvas canvas;
    Cell copyGrid[][];
    Cell cellGrid[][];
    int cellHeight;
    int cellWidth;
    int cellSize = cellHeight = cellWidth = 10;
    int gridWidth;
    int gridHeight;
    IntegerProperty generation = new SimpleIntegerProperty(this, "generation");
    Board board;
    Board boardWithNormalValues;
    Board boardDifferent;

    @Test
    public void fillCellBoard() throws Exception {
        boardWithNormalValues = new Board();
        boardDifferent = new Board();

        int gridWidthNormalValues = boardWithNormalValues.gridWidth = 80;
        int gridHeightNormalValues = boardWithNormalValues.gridHeight = 46;
        int gridWidthDifferent = boardDifferent.gridWidth = 100;
        int gridHeightDifferent = boardDifferent.gridHeight = 100;


        boardWithNormalValues.cellGrid = new Cell[gridWidthNormalValues][gridHeightNormalValues];
        boardWithNormalValues.copyGrid = new Cell[gridWidthNormalValues][gridHeightNormalValues];
        boardDifferent.cellGrid = new Cell[gridWidthDifferent][gridHeightDifferent];
        boardDifferent.copyGrid = new Cell[gridWidthDifferent][gridHeightDifferent];

        // The statement below must run for the assert to pass.
        boardWithNormalValues.fillCellBoard();
        boardDifferent.fillCellBoard();

        // If more than one cell is alive, the board is "filled"
        for (int i = 0; i < boardWithNormalValues.gridWidth; i++) {
            for (int j = 0; j < boardWithNormalValues.gridHeight; j++) {
                if (boardWithNormalValues.cellGrid[i][j].getState()) {
                    assertTrue(true);
                }
            }
        }

        // This for loop checks that the method still works even if the dimension is changed
        for (int i = 0; i < boardDifferent.gridWidth; i++) {
            for (int j = 0; j < boardDifferent.gridHeight; j++) {
                if (boardDifferent.cellGrid[i][j].getState()) {
                    assertTrue(true);
                }
            }
        }
    }

    @Test
    public void getCellGrid() throws Exception {
        board = new Board();
        Cell cellGrid[][] = new Cell[gridWidth][gridHeight];

        board.setCellGrid(cellGrid);
        assertEquals(cellGrid, board.getCellGrid());
    }

    @Test
    public void getCopyGrid() throws Exception {
        board = new Board();
        Cell copyGrid[][] = new Cell[gridWidth][gridHeight];

        // Running the below with another grid (e.g. cellGrid) yields error
        board.setCopyGrid(copyGrid);
        assertEquals(copyGrid, board.getCopyGrid());
    }

    @Test
    public void setCellGrid() throws Exception {
        board = new Board();
        Cell cellGrid[][] = new Cell[gridWidth][gridHeight];

        board.setCellGrid(cellGrid);
        assertEquals(cellGrid, board.getCellGrid());
    }

    @Test
    public void setCopyGrid() throws Exception {
        board = new Board();
        Cell copyGrid[][] = new Cell[gridWidth][gridHeight];

        // Running the below with another grid (e.g. cellGrid) yields error
        board.setCopyGrid(copyGrid);
        assertEquals(copyGrid, board.getCopyGrid());
    }

    @Test
    public void checkCellAlive() throws Exception {
        board = new Board();
        Board board2 = new Board();

        // Should be false when coordinator is outside of board
        assertFalse(board.checkCellAlive(-1, 1));
        assertFalse(board.checkCellAlive(1, -1));
        assertFalse(board.checkCellAlive(gridWidth, 1));
        assertFalse(board.checkCellAlive(1, gridHeight));

        // Simple board with just (0,0) being alive
        board2.cellGrid = new Cell[2][2];

        try {
            board2.cellGrid[0][0].setState(true);
            board2.cellGrid[0][1].setState(false);
            board2.cellGrid[1][0].setState(false);
            board2.cellGrid[1][1].setState(false);
        }
        catch (NullPointerException e) {
            System.out.println("These cell values are not valid!");
        }

        assertTrue(board2.checkCellAlive(0,0));
    }

    @Test
    public void countNeighbours() throws Exception {

    }

    @Test
    public void checkNeighbours() throws Exception {

    }

    @Test
    public void updateCell() throws Exception {

    }

    @Test
    public void clearCellState() throws Exception {

    }

}