package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by Andre on 24.04.2017.
 */
public class GraphicsDisplayDynamicBoard {

    int cellSize = 10;

    public GraphicsDisplayDynamicBoard(){

    }



    public void drawNextGen(DynamicGameBoard dynamicGameBoard, GraphicsContext gc, Color color){

        dynamicGameBoard.cellArrayList.get(1).add(new Cell(false));

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

    public void clearDrawing(DynamicGameBoard dynamicGameBoard, GraphicsContext gc){
        for (int x = 0; x < dynamicGameBoard.cellArrayList.size(); x++) {
            for (int y = 0; y < dynamicGameBoard.tempArray.size(); y++) {
                if(dynamicGameBoard.checkCellAlive(x, y))
                gc.setFill(Color.WHITE);
                gc.fillRect(cellSize * x, cellSize * y, cellSize, cellSize);

            }
        }
    }

}
