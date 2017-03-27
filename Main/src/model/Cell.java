package model;

/**
 * Created by Andre on 10.02.2017.
 */
public class Cell {

    private boolean cellState = false;
    private boolean newCellState = false;

    public Cell(int cellHeight, int cellWidth, boolean cellState){
        this.cellState = cellState;
    }

    public boolean getState(){
        return cellState;
    }
    public boolean getNewState(){
        return newCellState;
    }

    public void setState(boolean state){
       this.cellState = state;
    }

    public void setNewState(boolean newCellState){
       this.newCellState = newCellState;
    }


}
