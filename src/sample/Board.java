package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andre on 10.02.2017.
 */
public class Board {


    protected Cell copyGrid[][];
    protected Cell cellGrid[][];
    protected int cellHeight;
    protected int cellWidth;
    protected int cellSize = cellHeight = cellWidth = 15;
    protected int gridWidth;
    protected int gridHeight;
    protected IntegerProperty generation = new SimpleIntegerProperty(this, "generation");

    public Board(){

    }

    public Board(Canvas canvas) {
        gridWidth = (int) canvas.getWidth()/cellWidth;
        gridHeight = (int) canvas.getHeight()/cellHeight;


        cellGrid = new Cell[gridWidth][gridHeight];
        copyGrid = new Cell[gridWidth][gridHeight];


        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                cellGrid[i][j] = new Cell(cellHeight, cellWidth, false);
                copyGrid[i][j] = new Cell(cellHeight, cellWidth, false);

                if(Math.random() > 0.90){
                    cellGrid[i][j].setState(true);

                }

            }
        }
    }

    public Cell[][] getCellGrid(){
        return cellGrid;
    }

    public Cell[][] getCopyGrid(){
        return copyGrid;
    }

    public boolean checkCellAlive(int x, int y) {
        return cellGrid[x][y].getState();
    }

    public int countNeighbours(int x, int y) {
        int count = 0;

        if (x != 0 && y != 0) {
            if (checkCellAlive(x - 1, y - 1)) {
                count++;
            }
        }
        if (x != 0) {
            if (checkCellAlive(x - 1, y)) {
                count++;
            }
        }
        if (y != 0) {
            if (checkCellAlive(x, y - 1)) {
                count++;
            }
        }
        if (x != gridWidth - 1 && y != 0) {
            if (checkCellAlive(x + 1, y - 1)) {
                count++;
            }
        }
        if (x != 0 && y != gridHeight - 1) {
            if (checkCellAlive(x - 1, y + 1))
                count++;
        }
        if (x != gridWidth - 1 && y != gridHeight - 1) {
            if (checkCellAlive(x + 1, y + 1))
                count++;
        }
        if (y != gridHeight - 1) {
            if (checkCellAlive(x, y + 1))
                count++;

        }
        if (x != gridWidth - 1) {
            if (checkCellAlive(x + 1, y))
                count++;
        }
        return count;
    }

    protected void checkNeighbours() {

        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {

                int neighbour = countNeighbours(x, y);

                if (neighbour < 2) {
                    copyGrid[x][y].setNewState(false);
                } else if (neighbour > 3) {
                    copyGrid[x][y].setNewState(false);
                } else if (neighbour == 2) {
                    copyGrid[x][y].setNewState(cellGrid[x][y].getState());
                } else if (neighbour == 3) {
                    copyGrid[x][y].setNewState(true);
                }
            }
        }
        clearCellState();
    }


    public void updateCell() {
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                cellGrid[x][y].setState(copyGrid[x][y].getNewState());
            }
        }

    }

    public void clearCellState(){
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                cellGrid[x][y].setState(false);
            }
        }
    }

}

