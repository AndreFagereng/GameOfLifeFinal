package model;

/**
 * Created by Andre on 10.02.2017.
 */
public class Cell {

    private boolean listCellState = false;
    private boolean copyListCellState = false;


    /**
     * Cell constructor
     *
     * Creates a cell object with a boolean state.
     *
     * @param state value of the cell state
     */

    public Cell(boolean state){
        this.listCellState = state;
    }


    /**
     * 
     * @return
     */

    public boolean getArrayState(){ return listCellState;}
    public boolean getCopyArrayState(){ return copyListCellState;}

    public void setArrayState(boolean listCellState){this.listCellState = listCellState;}
    public void setCopyArrayState(boolean copyListCellState){this.copyListCellState = copyListCellState;}


    private boolean cellState = false;
    private boolean newCellState = false;


    public Cell(int cellHeight, int cellWidth, boolean cellState){
        this.cellState = cellState;
    }

    public void setState(boolean state){
       this.cellState = state;
    }
    public void setNewState(boolean newCellState){
       this.newCellState = newCellState;
    }

    public boolean getState(){
        return cellState;
    }
    public boolean getNewState(){
        return newCellState;
    }
}
