package model;

/**
 * Created by Andre on 10.02.2017.
 */
public class Cell {

    private boolean cellState = false;
    private boolean newCellState = false;

    private boolean listCellState = false;

    public Cell(int cellHeight, int cellWidth, boolean cellState){
        this.cellState = cellState;
    }

    public Cell(boolean state){
        this.listCellState = state;
    }

    public boolean getState(){
        return cellState;
    }
    public boolean getArrayState(){ return listCellState;}
    public boolean getNewState(){
        return newCellState;
    }

    public void setArrayState(boolean listCellState){this.listCellState = listCellState;}

    public void setState(boolean state){
       this.cellState = state;
    }

    public void setNewState(boolean newCellState){
       this.newCellState = newCellState;
    }


}
