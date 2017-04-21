package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by Andre on 21.04.2017.
 */
public class DynamicGameBoard {

    ArrayList<ArrayList<Cell>> cellArrayList;
    ArrayList<ArrayList<Cell>> copyArrayList = new ArrayList<>();

    ArrayList<Cell> tempArray;
    Cell cell;


    public DynamicGameBoard(int width, int height, boolean state) {

        cellArrayList = new ArrayList<>(height);
        tempArray = new ArrayList<>();
        cell = new Cell(state);


        //IntStream.range(0,width).forEach(p-> tempArray.add(new Cell(state)));
        IntStream.range(0, height).forEach(p -> {
            tempArray.clear();
            IntStream.range(0, width).forEach(e -> tempArray.add(new Cell(state)));
            cellArrayList.add(new ArrayList<>(tempArray));
        });


    }

    public void randomizeStates(ArrayList<ArrayList<Cell>> cellArrayList) {
       /* cellArrayList.forEach(array -> {

            for(int i = 0; i < tempArray.size(); i++){
                double p = Math.random();
                if(p < 0.5){
                    tempArray
                }
            }
        });*/
        boolean state = !cellArrayList.get(0).get(0).getArrayState();
        for (ArrayList<Cell> array : cellArrayList) {
            for (Cell anArray : array) {
                if (Math.random() < 0.5) {
                    anArray.setArrayState(state);
                }
            }
        }

    }


    public void testGameBoard(GraphicsContext gc) {

        randomizeStates(cellArrayList);
        System.out.println(tempArray.size());
        System.out.println(cellArrayList.size());

        for (int i = 0; i < cellArrayList.size(); i++) {
            for (int y = 0; y < tempArray.size(); y++) {
                if (checkCellAlive(i, y)) {
                    gc.fillRect(10 * y, 10 * i, 9, 9);
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

    public void testMethod() {
        checkNeighbours(cellArrayList, tempArray);
    }

    protected void checkNeighbours(ArrayList<ArrayList<Cell>> cellArrayList, ArrayList<Cell> tempArray) {

        int neighbour = 0;
        for (int x = 0; x < cellArrayList.size(); x++) {
            for (int y = 0; y < tempArray.size(); y++) {

                neighbour = countNeighbours(x, y);

                // dies if < 2 neighbours
                if (neighbour < 2) {
                    cellArrayList.get(y).get(x).setArrayState(false);
                    // dies if > 3 neighbours
                } else if (neighbour > 3) {
                    cellArrayList.get(y).get(x).setArrayState(false);
                    // survives if  2 neighbours
                } else if (neighbour == 2) {
                    cellArrayList.get(y).get(x).getArrayState();
                    // lives if 3 neighbours
                } else if (neighbour == 3) {
                    cellArrayList.get(y).get(x).setArrayState(true);
                }

                System.out.print(neighbour + " ");
            }
            System.out.println("\n");
        }

    }

    public boolean checkCellAlive(int x, int y) {
        if (x == -1 || y == -1 || x == tempArray.size() || y == cellArrayList.size()) {
            return false;
        }
        return cellArrayList.get(x).get(y).getArrayState();
    }


}
