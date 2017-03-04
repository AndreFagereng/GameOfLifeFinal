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

    @Test
    public void fillCellBoard() throws Exception {
        board = new Board();
        board.gridWidth = 80;
        board.gridHeight = 46;

        board.cellGrid = new Cell[board.gridWidth][board.gridHeight];
        board.copyGrid = new Cell[board.gridWidth][board.gridHeight];

        // The statement below must run for the assert to pass.
        board.fillCellBoard();

        // If more than one cell is alive, the board is "filled"
        for (int i = 0; i < board.gridWidth; i++) {
            for (int j = 0; j < board.gridHeight; j++) {
                if (board.cellGrid[i][j].getState()) {
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