package sample;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    Canvas canvas;
    @FXML
    Button startPauseBtn, clearBtn;
    @FXML
    ColorPicker colorPicker;
    @FXML
    Slider speedSlider, zoomSlider;
    @FXML
    Text showGen;

    //Controller class
    
    private Board board;
    private GraphicsContext gc;
    private AnimationTimer timer;
    private int speed;
    private GraphicsDisplayBoard gdb;
    Color aliveCellColor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colorPicker.setValue(Color.GREEN);
        aliveCellColor = colorPicker.getValue();
        gc = canvas.getGraphicsContext2D();
        board = new Board(canvas);
        gdb = new GraphicsDisplayBoard(gc, canvas);


        onClickCellEvent();
        showGenerationText();
        timerMethod();
        onChangeColor();
        //dragAndDrawEvent();


    }

    private void showGenerationText() {
        showGen.textProperty().bind(Bindings.concat(board.generation));
    }

    private void clearGenerationText() {
        board.generation.set(0);
    }

    public void timerMethod() {
        speed = 0;
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                speed++;
                if (speed > Math.abs((int) speedSlider.getValue())) {
                    gdb.updateBoard(gc);
                    gdb.drawNextGen(gc, aliveCellColor, board);

                    speed = 0;
                    board.generation.set(board.generation.get() + 1);
                }
            }
        };
    }

    public void onClear() {
        board.clearCellState();
        gdb.clearBoard(gc);
        timer.stop();
        clearGenerationText();
    }

    public void onStop() {
        timer.stop();
    }

    public void onStart() {
        timer.start();
    }

    private void onChangeColor() {
        colorPicker.setOnAction(e -> {
            aliveCellColor = colorPicker.getValue();
            gdb.drawNextGen(gc, aliveCellColor, board);
        });
    }

    private void onClickCellEvent() {
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {


            int x = (int) (e.getX() / board.cellSize);
            int y = (int) (e.getY() / board.cellSize);

            if (gdb.cellGrid[x][y].getState()) {
                gdb.cellGrid[x][y].setState(false);

                gc.setFill(Color.LIGHTGREY);
                gc.fillRect(x * board.cellWidth, y * board.cellHeight, board.cellHeight - 1, board.cellWidth - 1);

            } else if (!gdb.cellGrid[x][y].getState()) {
                gdb.cellGrid[x][y].setState(true);
                gc.setFill(aliveCellColor);
                gc.fillRect(x * board.cellWidth, y * board.cellHeight, board.cellHeight - 1, board.cellWidth - 1);


            }
        });
    }

    public void dragAndDrawEvent() {
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {

            int x = (int) (e.getX() / board.cellSize);
            int y = (int) (e.getY() / board.cellSize);

            if (gdb.cellGrid[x][y].getState()) {
                gdb.cellGrid[x][y].setState(false);
                gc.clearRect(x * board.cellWidth, y * board.cellHeight, board.cellHeight - 1, board.cellWidth - 1);

            } else if (!gdb.cellGrid[x][y].getState()) {
                gdb.cellGrid[x][y].setState(true);

                gc.setFill(aliveCellColor);
                gc.fillRect(x * board.cellWidth, y * board.cellHeight, board.cellHeight - 1, board.cellWidth - 1);

            }
        });
    }
}



