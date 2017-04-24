package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by Andre on 24.04.2017.
 */
public class GraphicsDisplayDynamicBoard {

    Color bkGroundColor = Color.LIGHTGREY;

    public GraphicsDisplayDynamicBoard(){

    }


    public void drawNextGen(DynamicGameBoard dynamicGameBoard, GraphicsContext gc, Color color){
        for (int i = 0; i < dynamicGameBoard.cellArrayList.size(); i++) {
            for (int y = 0; y < dynamicGameBoard.tempArray.size(); y++) {
                if (dynamicGameBoard.checkCellAlive(i, y)) {
                    gc.setFill(color);
                    gc.fillRect(10 * i, 10 * y, 9, 9);
                }else if(!dynamicGameBoard.checkCellAlive(i, y)) {
                    gc.setFill(Color.WHITE);
                    gc.fillRect(10 * i, 10 * y, 9, 9);
                }

            }
        }
    }

    public void clearDrawing(DynamicGameBoard dynamicGameBoard, GraphicsContext gc){
        for (int x = 0; x < dynamicGameBoard.cellArrayList.size(); x++) {
            for (int y = 0; y < dynamicGameBoard.tempArray.size(); y++) {
                if(dynamicGameBoard.checkCellAlive(x, y))
                gc.setFill(Color.WHITE);
                gc.fillRect(x * 10, y * 10, 9, 9);

            }
        }
    }

}
