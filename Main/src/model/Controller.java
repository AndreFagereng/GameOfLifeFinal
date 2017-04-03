package model;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML Canvas canvas;
    @FXML Button startPauseBtn, clearBtn, saveAs;
    @FXML ColorPicker colorPicker;
    @FXML Slider speedSlider, zoomSlider;
    @FXML Text showGen;
    @FXML TextArea textAreaPattern;
    

    //Controller class

    private Board board;
    private GraphicsContext gc;
    private AnimationTimer timer;
    private int speed;
    private GraphicsDisplayBoard gdb;
    Color aliveCellColor;
    Stage stage;
    FileChooser fileChooser;
    File RLEFormatFile;
    FileReader fileReader;
    BufferedReader bufferedReader;
    Pattern pattern;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colorPicker.setValue(Color.valueOf("#ffffb3"));
        aliveCellColor = colorPicker.getValue();
        gc = canvas.getGraphicsContext2D();
        board = new Board(canvas);
        gdb = new GraphicsDisplayBoard(gc, canvas);
        pattern = new Pattern();


        onClickCellEvent();
        showGenerationText();
        timerMethod();
        onChangeColor();
        dragAndDrawEvent();

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
        startPauseBtn.setText("Start");
        timer.stop();
        clearGenerationText();
    }

    public void onStop() {
        timer.stop();
    }

    public void onStart() {

        if (startPauseBtn.getText().toString().equals("Start")) {
            startPauseBtn.setText("Pause");
            timer.start();
        }
        else if (startPauseBtn.getText().toString().equals("Pause")) {
            startPauseBtn.setText("Start");
            timer.stop();
        }
    }

    public void nextGen() {
        gdb.updateBoard(gc);
        gdb.drawNextGen(gc, aliveCellColor, board);
        board.generation.set(board.generation.get() + 1);
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

            if (x == -1 || y == -1 || x == board.gridWidth || y == board.gridHeight) {
                throw new ArrayIndexOutOfBoundsException();
            }
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

    public void dragAndDrawEvent(){
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {

            int x = (int) (e.getX() / board.cellSize);
            int y = (int) (e.getY() / board.cellSize);

            try {
                if (!gdb.cellGrid[x][y].getState()) {
                    gdb.cellGrid[x][y].setState(true);

                    gc.setFill(aliveCellColor);
                    gc.fillRect(x * board.cellWidth, y * board.cellHeight, board.cellHeight - 1, board.cellWidth - 1);

                }
            } catch (ArrayIndexOutOfBoundsException ae) {

            }
        });
    }

    public void createPattern() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../View/createOwnPattern.fxml"));
        stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Patterns");
        stage.show();
    }
    /*
    public void closeButtonAction(){
        // get a handle to the stage
        Stage stage = (Stage) saveAs.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
       */

    public void readRLEPattern(File RLEFormatFile) throws Exception{
        fileReader = new FileReader(RLEFormatFile);
        bufferedReader = new BufferedReader(fileReader);
        gdb.clearBoard(gc);

        int nextChar;
        int x = 5;
        int y = 5;

        try{

            while((nextChar = bufferedReader.read()) != -1){
                char c = (char) nextChar;

                if (Character.toString(c).matches("[o]")) {
                    gdb.cellGrid[x][y].setState(true);
                    x++;
                } else if (Character.toString(c).matches("[b]")) {
                    gdb.cellGrid[x][y].setState(false);
                    x++;
                } else if (Character.toString(c).matches("[$]")){
                    x = 5;
                    y++;
                }

            }

            bufferedReader.close();

        }catch (NullPointerException NPE){
            System.out.println("User did`nt select file");
        }catch (ArrayIndexOutOfBoundsException Ar){
            System.out.println("Too big");
        }
        gdb.drawNextGen(gc, colorPicker.getValue(), board);

    }


    public void openAndReadRLEFormat() throws Exception{
        fileChooser = new FileChooser();
        RLEFormatFile = fileChooser.showOpenDialog(null);

        if(RLEFormatFile != null){
            readRLEPattern(RLEFormatFile);
        }
    }


    public void readFromUrl() throws Exception{
        String test = JOptionPane.showInputDialog("URL");

        URL url = new URL(test);
        URLConnection conn = url.openConnection();

        try {

            bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            int nextChar;


            while((nextChar = bufferedReader.read()) != -1){
                char c = (char)nextChar;

            }



           /* int nextChar;
            int x = 5;
            int y = 5;

            while((nextChar = bufferedReader.read()) != -1){
                char c = (char) nextChar;

                if (Character.toString(c).matches("[o]")) {
                    gdb.cellGrid[x][y].setState(true);
                    x++;
                } else if (Character.toString(c).matches("[b]")) {
                    gdb.cellGrid[x][y].setState(false);
                    x++;
                } else if (Character.toString(c).matches("[$]")){
                    x = 5;
                    y++;
                }
                System.out.print(c);
*/

            gdb.drawNextGen(gc, colorPicker.getValue(), board);
            bufferedReader.close();

        }catch(MalformedURLException ME){
            System.out.println("test");
        }
    }

    public void exitProgram() {
        System.exit(0);
    }
}



