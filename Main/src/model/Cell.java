package model;

/**
 * Created by Andre on 10.02.2017.
 */
public class Cell {


    private boolean cellState = false;
    private boolean newCellState = false;


    private boolean listCellState = false;
    private boolean copyListCellState = false;


    /**
     * Cell constructor
     *
     * Creates a cell object with a boolean state.
     * Is used for the DynamicGameBoard class.
     *
     * @param state value of the cell state
     */

    public Cell(boolean state){
        this.listCellState = state;
    }


    /**
     * This method is used to get the state of a cell.
     * The main ArrayList cellArrayList uses listCellState
     * variable for its cells.
     *
     * @return returns the boolean value of listCellState
     */

    public boolean getArrayState(){ return listCellState;}

    /**
     * This method is used to get the state of a cell.
     * The copy ArrayList copyArrayList uses copyListCellState
     * variable for its cells.
     *
     * @return returns the boolean value of copyListCellState
     */

    public boolean getCopyArrayState(){ return copyListCellState;}

    /**
     * Set-method for setting a new value to listCellState.
     *
     * @param listCellState takes a boolean value and sets a new boolean value.
     */

    public void setArrayState(boolean listCellState){this.listCellState = listCellState;}

    /**
     * Set-method for setting a new value to copyListCellState.
     *
     * @param copyListCellState takes a boolean value and sets a new boolean value.
     */

    public void setCopyArrayState(boolean copyListCellState){this.copyListCellState = copyListCellState;}


    /**
     * Cell constructor
     *
     * We used this constructor in the Board class which is deprecated.
     * This code below is not used in the currently made Game Of Life.
     *
     * @param cellHeight sets the cellHeight
     * @param cellWidth sets the cellWidth
     * @param cellState sets the boolean state of a cell.
     * @deprecated
     */

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
