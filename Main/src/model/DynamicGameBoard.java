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
 */
public class DynamicGameBoard {

    ArrayList<ArrayList<Cell>> cellArrayList;
    ArrayList<ArrayList<Cell>> copyArrayList;

    ArrayList<Cell> tempArray;
    ArrayList<Cell> copyTempArray;
    Cell cell;
    GraphicsDisplayDynamicBoard graphicsDisplayDynamicBoard;
    protected IntegerProperty generation = new SimpleIntegerProperty(this, "generation");
    protected IntegerProperty cellsAlive = new SimpleIntegerProperty(this, "cellsAlive");


    public DynamicGameBoard(int width, int height, boolean state) {

        cellArrayList = new ArrayList<>(height);
        copyArrayList = new ArrayList<>(height);

        copyTempArray = new ArrayList<>();
        tempArray = new ArrayList<>();
        cell = new Cell(state);

        graphicsDisplayDynamicBoard = new GraphicsDisplayDynamicBoard();


        //IntStream.range(0,width).forEach(p-> tempArray.add(new Cell(state)));
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


    public void randomizeStates(ArrayList<ArrayList<Cell>> cellArrayList) {

        cellArrayList.get(0).get(4).setArrayState(true);
        cellArrayList.get(1).get(4).setArrayState(true);
        cellArrayList.get(2).get(4).setArrayState(true);

        cellArrayList.get(1).get(2).setArrayState(true);
        cellArrayList.get(2).get(3).setArrayState(true);

        /*boolean state = !cellArrayList.get(0).get(0).getArrayState();
        for (ArrayList<Cell> array : cellArrayList) {
            for (Cell anArray : array) {
                if (Math.random() < 0.6) {
                    anArray.setArrayState(state);
                }
            }
        }*/

    }


    public void initializeDynamicBoard() {
        randomizeStates(cellArrayList);

        System.out.println(tempArray.size());
        System.out.println(cellArrayList.size());

    }

    public void onNextGen() {
        cellsAlive.set(0);
        checkNeighbours(cellArrayList, tempArray);
        copyArrayList();




    }

    public void clearCellState() {
        for (int x = 0; x < cellArrayList.size(); x++) {
            for (int y = 0; y < tempArray.size(); y++) {
                cellArrayList.get(x).get(y).setArrayState(false);
            }
        }
    }


    public void setCellState(int x, int y, boolean state){
       cellArrayList.get(x).get(y).setArrayState(state);
        while(cellArrayList.size() < x || cellArrayList.size() < y){
            extendBoard();
        }
    }

    private void extendBoard() {

        ArrayList<Cell> newTempArray = new ArrayList<>(cellArrayList.size() + 1);
        cellArrayList.add(new ArrayList<>(newTempArray));

        IntStream.range(cellArrayList.size(), cellArrayList.size() + 1).forEach(p -> {
            tempArray.clear();
            IntStream.range(cellArrayList.size(), cellArrayList.size() + 1).forEach(e -> tempArray.add(new Cell(false)));
            cellArrayList.add(new ArrayList<>(tempArray));

        });

    }

    public void setCopyState(int x, int y, boolean state){
        copyArrayList.get(x).get(y).setCopyArrayState(state);

    }

    public void copyArrayList() {
        for (int i = 0; i < copyArrayList.size(); i++) {
            for (int y = 0; y < tempArray.size(); y++) {
                if (copyArrayList.get(i).get(y).getCopyArrayState()) {
                    setCellState(i, y, true);
                    cellsAlive.set(cellsAlive.get() + 1);
                } else {
                    setCellState(i, y, false);

                }
            }
        }
    }


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
                    setCopyState(x, y, true);;
                }

                //System.out.print(neighbour + " ");
            }
            //System.out.println("\n");
        }
        clearCellState();

    }

    protected boolean checkCellAlive(int x, int y) {
        return !(x == -1 || y == -1 || x == tempArray.size() || y == cellArrayList.size()) && cellArrayList.get(x).get(y).getArrayState();
    }


}
