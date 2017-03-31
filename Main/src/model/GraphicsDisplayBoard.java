package model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by Andre on 13.02.2017.
 */
public class GraphicsDisplayBoard {



    Cell cellGrid[][];
    Cell copyGrid[][];
    Board board;
    Color defaultColor = Color.valueOf("#ffffb3");
    Color bkGroundColor = Color.LIGHTGREY;


    public GraphicsDisplayBoard(GraphicsContext gc, Canvas canvas) {

        board = new Board(canvas);

        cellGrid = board.cellGrid;
        copyGrid = board.copyGrid;

        for (int x = 0; x < board.gridWidth; x++) {
            for (int y = 0; y < board.gridHeight; y++) {
                if (cellGrid[x][y].getState()) {
                    gc.setFill(defaultColor);
                    gc.fillRect(x * board.cellSize, y * board.cellSize, board.cellHeight - 1, board.cellWidth - 1);
                } else if (!cellGrid[x][y].getState()) {
                    gc.setFill(bkGroundColor);
                    gc.fillRect(x * board.cellSize, y * board.cellSize, board.cellWidth - 1, board.cellHeight - 1);
                }
            }
        }
    }

    public void updateBoard(GraphicsContext gc) {
        board.checkNeighbours();
        clearBoard(gc);
        board.updateCell();
    }

    /*
        public void test(GraphicsContext gc){
            for(Cell[] g : cellGrid){
                for(Cell t : g){
                    gc.setEffect(new Glow(12));
                }
            }
        }
        */
    public void clearBoard(GraphicsContext gc) {
        for (int x = 0; x < board.gridWidth; x++) {
            for (int y = 0; y < board.gridHeight; y++) {
                cellGrid[x][y].setState(false);
                gc.setFill(bkGroundColor);
                gc.fillRect(x * board.cellSize, y * board.cellSize, board.cellWidth - 1, board.cellHeight - 1);

            }
        }
    }


    public void drawNextGen(GraphicsContext gc, Color color, Board board) {
        for (int x = 0; x < board.gridWidth; x++) {
            for (int y = 0; y < board.gridHeight; y++) {
                if (cellGrid[x][y].getState()) {
                    gc.setFill(color);
                    gc.fillRect(x * board.cellSize, y * board.cellSize, board.cellWidth - 1, board.cellHeight - 1);
                } else if (!cellGrid[x][y].getState()) {
                    gc.setFill(bkGroundColor);
                    gc.fillRect(x * board.cellSize, y * board.cellSize, board.cellWidth - 1, board.cellHeight - 1);
                }
            }

        }


    }


}
