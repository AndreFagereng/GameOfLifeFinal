package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by Andre on 21.04.2017.
 *
 * @author Andre
 */
public class DynamicGameBoard {

    public ArrayList<ArrayList<Cell>> cellArrayList;
    ArrayList<ArrayList<Cell>> copyArrayList;

    ArrayList<Cell> tempArray;
    ArrayList<Cell> copyTempArray;
    Cell cell;
    public IntegerProperty generation = new SimpleIntegerProperty(this, "generation");
    public IntegerProperty cellsAlive = new SimpleIntegerProperty(this, "cellsAlive");


    /**
     * DynamicGameBoard constructor
     * <p>
     * Creates two 2D ArrayList height * width.
     * CellArrayList is the main ArrayList which is
     * drawn on the Canvas. CopyArrayList is a copy that
     * gets the next generation of living Cells.
     *
     * @param width  Sets width of DynamicGameBoard.
     * @param height Sets height of DynamicGameBoard.
     * @param state  Sets boolean state of each Cell.
     */

    public DynamicGameBoard(int width, int height, boolean state) {

        cellArrayList = new ArrayList<>(height);
        copyArrayList = new ArrayList<>(height);

        copyTempArray = new ArrayList<>();
        tempArray = new ArrayList<>();

        cell = new Cell(state);

        IntStream.range(0, height).forEach(p -> {
            tempArray.clear();
            IntStream.range(0, width).forEach(e -> tempArray.add(new Cell(state)));
            cellArrayList.add(new ArrayList<>(tempArray));

        });

        IntStream.range(0, height).forEach(p -> {
            copyTempArray.clear();
            IntStream.range(0, width).forEach(e -> copyTempArray.add(new Cell(state)));
            copyArrayList.add(new ArrayList<>(copyTempArray));

        });
    }


    public void onNextGen() {
        collisionChecker();
        cellsAlive.set(0);
        checkNeighbours(cellArrayList, tempArray);
        copyArrayList();


    }


    /**
     * ClearCellState method goes through the
     * cellArrayList and sets all Cells to false.
     * This is used right before the cellArrayList
     * copies the next generation from copyArrayList.
     */


    public void clearCellState() {
        for (ArrayList<Cell> subArray : cellArrayList) {
            subArray.stream().filter(Cell::getArrayState).forEach(cell -> cell.setArrayState(false));
        }
    }

    //Old code
   /* public void clearCellState() {
        for (int x = 0; x < cellArrayList.size(); x++) {
            for (int y = 0; y < tempArray.size(); y++) {
                cellArrayList.get(x).get(y).setArrayState(false);
            }
        }
    }*/


    /**
     * This method sets a specific cell in the ArrayList
     * to a different boolean state.
     *
     * @param x x value of the cell.
     * @param y y value of the cell.
     * @param state new boolean state value of that specific cell.
     */

    public void setCellState(int x, int y, boolean state) {
        cellArrayList.get(x).get(y).setArrayState(state);
    }


    /**
     * This methods checks if a "living organism" is moving
     * towards the end of the cellArrayList. If the cell
     * is near the end, the cellArrayList will increase its
     * size. One more ArrayList is created in the cellArrayList
     * and each ArrayList within increases by 1 cell.
     *
     */
    private void collisionChecker() {

        for (int i = 0; i < cellArrayList.size(); i++) {
            if (cellArrayList.get(cellArrayList.size() - 2).get(i).getArrayState()) {
                extendBoardDownRight();

            } else if (cellArrayList.get(i).get(cellArrayList.size() - 2).getArrayState()) {
                extendBoardDownRight();

            } else if (cellArrayList.get(0).get(i).getArrayState()) {
                // extendBoardUpLeft();

            } else if (cellArrayList.get(i).get(0).getArrayState()) {
                //extendBoardUpLeft();

            }
        }

    }


    public void extendBoardUpLeft() {
        tempArray = new ArrayList<>();
        copyTempArray = new ArrayList<>();

        IntStream.range(0, cellArrayList.size()).forEach(p -> {
            tempArray.add(0, new Cell(false));
        });
        cellArrayList.add(new ArrayList<>(tempArray));

        for (ArrayList<Cell> array : cellArrayList) {
            array.add(0, new Cell(false));
        }

        IntStream.range(0, cellArrayList.size()).forEach(p -> {
            copyTempArray.add(new Cell(false));
        });
        copyArrayList.add(new ArrayList<>(copyTempArray));

        for (ArrayList<Cell> array : copyArrayList) {
            array.add(0, new Cell(false));
        }
    }

    /**
     * This method extends the board and is used
     * in the collisionChecker method.
     */

    private void extendBoardDownRight() {
        tempArray = new ArrayList<>();
        copyTempArray = new ArrayList<>();

        IntStream.range(0, cellArrayList.size()).forEach(p -> {
            tempArray.add(new Cell(false));
        });
        cellArrayList.add(new ArrayList<>(tempArray));

        for (ArrayList<Cell> array : cellArrayList) {
            array.add(new Cell(false));
        }

        IntStream.range(0, cellArrayList.size()).forEach(p -> {
            copyTempArray.add(new Cell(false));
        });
        copyArrayList.add(new ArrayList<>(copyTempArray));

        for (ArrayList<Cell> array : copyArrayList) {
            array.add(new Cell(false));
        }

    }

    /**
     * This method sets a specific cell in the
     * copyArrayList to a new boolean value.
     *
     * @param x x value of the cell.
     * @param y y value of the cell.
     * @param state new boolean state value of the cell.
     *
     */

    public void setCopyState(int x, int y, boolean state) {
        copyArrayList.get(x).get(y).setCopyArrayState(state);
    }


    /**
     * This method iterates the copyArrayList and sets
     * the main cellArrayList to the exact same for each cell
     *
     */


    public void copyArrayList() {
        for (int x = 0; x < copyArrayList.size(); x++) {
            for (int y = 0; y < cellArrayList.size(); y++) {
                if (copyArrayList.get(x).get(y).getCopyArrayState()) {
                    setCellState(x, y, true);
                    cellsAlive.set(cellsAlive.get() + 1);
                } else {
                    setCellState(x, y, false);
                }
            }
        }
    }


    /**
     * This is the method that counts the number of
     * living cells to a specific cell. The methods iterates
     * through every neighbour of that cell, and stores the int
     * value in count.
     *
     * @param x x value of the cell.
     * @param y y value of the cell.
     * @return returns number of living neighbours of a specific cell.
     */


    public int countNeighbours(int x, int y) {

        int count = 0;

        for (int xoffset = -1; xoffset < 2; xoffset++) {
            for (int yoffset = -1; yoffset < 2; yoffset++) {
                if (checkCellAlive(x - xoffset, y - yoffset) && (xoffset != 0 || yoffset != 0)) {
                    count++;
                }
            }
        }

        return count;
    }

    /**
     *
     * CheckNeighbours uses countNeighbours to see if a
     * specific cell is alive to the next generation of cells.
     * Iterates the cellArrayList to check and count neighbours
     * of each cell. And stores the new boolean value of each
     * cell in the copyArrayList.
     *
     * cellArrayList contains this generation.
     * copyArrayList contains next generation.
     *
     * @param cellArrayList
     * @param tempArray
     */


    protected void checkNeighbours(ArrayList<ArrayList<Cell>> cellArrayList, ArrayList<Cell> tempArray) {

        int neighbour = 0;
        for (int x = 0; x < cellArrayList.size(); x++) {
            for (int y = 0; y < tempArray.size(); y++) {

                neighbour = countNeighbours(x, y);

                // dies if < 2 neighbours
                if (neighbour < 2) {
                    setCopyState(x, y, false);
                    // dies if > 3 neighbours
                } else if (neighbour > 3) {
                    setCopyState(x, y, false);
                    // survives if  2 neighbours
                } else if (neighbour == 2) {
                    setCopyState(x, y, cellArrayList.get(x).get(y).getArrayState());
                    // lives if 3 neighbours
                } else if (neighbour == 3) {
                    setCopyState(x, y, true);               }

                //System.out.print(neighbour + " ");
            }
            //System.out.println("\n");
        }
        clearCellState();

    }

    /**
     *
     * This method is used to see if a specific cell is alive.
     * It takes two parameters to go to the coordinates of
     * that cell, and returns the state.
     *
     * @param x x coordinate of a cell.
     * @param y y coordinate of a cell.
     * @return returns the boolean value of that cell.
     */

    public boolean checkCellAlive(int x, int y) {
        return !(x == -1 || y == -1 || x == cellArrayList.size() || y == cellArrayList.size()) && cellArrayList.get(x).get(y).getArrayState();
    }


}
