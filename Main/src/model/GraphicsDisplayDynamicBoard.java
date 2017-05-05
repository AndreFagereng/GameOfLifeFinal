package model;

import controller.Controller;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by Andre on 24.04.2017.
 */
public class GraphicsDisplayDynamicBoard extends Controller {

    public int cellSize = 10;

    public GraphicsDisplayDynamicBoard(){

    }

    /**
     * This method is the main method for drawing the nextGeneration of
     * the game. It iterates through the chosen DynamicGameBoard and
     * checks if cells are alive. If they live, they are drawn with a color
     * chosen by the ColorPicker. Dead cells are also drawn but with white background.
     *
     * @param dynamicGameBoard Draws on the chosen DynamicBoard
     * @param gc Uses GraphicContext to draw on the canvas.
     * @param color Color is used as parameter to get the ColorPicker value in Controller Class.
     */

    public void drawNextGen(DynamicGameBoard dynamicGameBoard, GraphicsContext gc, Color color){


        for (int i = 0; i < dynamicGameBoard.cellArrayList.size(); i++) {
            for (int y = 0; y < dynamicGameBoard.tempArray.size(); y++) {
                if (dynamicGameBoard.checkCellAlive(i, y)) {
                    gc.setFill(color);
                    gc.fillRect(cellSize * i, cellSize * y, cellSize, cellSize);
                }else if(!dynamicGameBoard.checkCellAlive(i, y)) {
                    gc.setFill(Color.WHITE);
                    gc.fillRect(cellSize * i, cellSize * y, cellSize, cellSize);
                }

            }
        }
    }

    /**
     *
     * This method iterates through a dynamicBoard and draws all
     * aliveCells to white. Indicating that all is dead.
     *
     * @param dynamicGameBoard Draws on the chosen DynamicBoard
     * @param gc Uses GraphicContext to draw on the canvas.
     */

    public void clearDrawing(DynamicGameBoard dynamicGameBoard, GraphicsContext gc){
        for (int x = 0; x < dynamicGameBoard.cellArrayList.size(); x++) {
            for (int y = 0; y < dynamicGameBoard.tempArray.size(); y++) {
                if(dynamicGameBoard.checkCellAlive(x, y))
                gc.setFill(Color.WHITE);
                gc.fillRect(cellSize * x, cellSize * y, cellSize, cellSize);

            }
        }
    }

    /**
     * A getter to return the used cellSize for each drawn cell.
     * @return Return an int variable.
     */

    public int getCellSize(){
        return cellSize;
    }


    /**
     * A setter for setting the cellSize to a new value.
     * @param cellSize Takes and int to set a new cellSize.
     */
    public void setCellSize(int cellSize){
        this.cellSize = cellSize;
    }



}
