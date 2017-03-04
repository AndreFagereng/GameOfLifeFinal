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
    Board boardWithNormalValues;
    Board boardDifferent;

    @Test
    public void fillCellBoard() throws Exception {
        boardWithNormalValues = new Board();
        boardDifferent = new Board();

        int gridWidthNormalValues = boardWithNormalValues.gridWidth = 80;
        int gridHeightNormalValues = boardWithNormalValues.gridHeight = 46;
        int gridWidthDifferent = boardDifferent.gridWidth = 50;
        int gridHeightDifferent = boardDifferent.gridHeight = 50;


        boardWithNormalValues.cellGrid = new Cell[gridWidthNormalValues][gridHeightNormalValues];
        boardWithNormalValues.copyGrid = new Cell[gridWidthNormalValues][gridHeightNormalValues];
        boardDifferent.cellGrid = new Cell[gridWidthDifferent][gridHeightDifferent];
        boardDifferent.copyGrid = new Cell[gridWidthDifferent][gridHeightDifferent];

        // The statement below must run for the assert to pass.
        // boardWithNormalValues.fillCellBoard();
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
        assertTrue(true);
    }

    @Test
    public void getCopyGrid() throws Exception {

    }

    @Test
    public void checkCellAlive() throws Exception {

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