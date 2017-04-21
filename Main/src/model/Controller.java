package model;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;

import static model.Patterns.*;
import static model.GraphicsDisplayBoard.newPattern;

public class Controller implements Initializable {

    @FXML
    Canvas canvas;
    @FXML
    Button startPauseBtn, clearBtn, saveAs;
    @FXML
    ColorPicker colorPicker;
    @FXML
    Slider speedSlider, zoomSlider;
    @FXML
    Text showGen;
    @FXML
    TextArea textAreaPattern;
    @FXML
    ChoiceBox<PatternType> choicePattern;


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
    BufferedReader bufferedReader;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        onPatternDraw();

        colorPicker.setValue(Color.valueOf("#ffffb3"));
        aliveCellColor = colorPicker.getValue();
        gc = canvas.getGraphicsContext2D();
        board = new Board(canvas);
        gdb = new GraphicsDisplayBoard(gc, canvas);




        showGenerationText();
        timerMethod();
        onChangeColor();



    }

    private void onPatternDraw() {
        choicePattern.getItems().addAll(PatternType.values());
        choicePattern.valueProperty().addListener(e -> getSelectedPattern());
        choicePattern.getSelectionModel().selectLast();
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
        } else if (startPauseBtn.getText().toString().equals("Pause")) {
            startPauseBtn.setText("Start");
            timer.stop();
        }
    }

    public void nextGen() {
        timer.stop();
        startPauseBtn.setText("Start");
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

    private EventHandler onClickCellEvent = new EventHandler() {
        @Override
        public void handle(Event event) {
            int x = getXPosition(event);
            int y = getYPosition(event);

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
        }
    };


    public EventHandler onDragCellEvent = new EventHandler() {
        @Override
        public void handle(Event event) {
            int x = getXPosition(event);
            int y = getYPosition(event);
            try {
                if (!gdb.cellGrid[x][y].getState()) {
                    gdb.cellGrid[x][y].setState(true);

                    gc.setFill(aliveCellColor);
                    gc.fillRect(x * board.cellWidth, y * board.cellHeight, board.cellHeight - 1, board.cellWidth - 1);

                }
            } catch (ArrayIndexOutOfBoundsException ae) {

            }
        }
    };


    public void createPattern() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../View/createOwnPattern.fxml"));
        stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Patterns");
        stage.show();
    }

    public void onReadURLFile() throws Exception  {

        FileHandler fileHandler = new FileHandler();

        String test = JOptionPane.showInputDialog("Paste URL");
            URL url = new URL(test);
            URLConnection conn = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));


            String text = "";
            try {
                text = fileHandler.readURLFile(bufferedReader);
            }catch (NullPointerException nullPoint){
                System.out.println("Catch works");
            }
            gdb.clearBoard(gc);
            fileHandler.readStringToBoard(text, gdb);
            gdb.drawNextGen(gc, colorPicker.getValue(), board);

    }


    public void onOpenRLEFile() throws Exception{

        try {
            FileHandler fileHandler = new FileHandler();
            fileChooser = new FileChooser();
            RLEFormatFile = fileChooser.showOpenDialog(null);


            if (RLEFormatFile != null) {
                fileHandler.readOpenFile(RLEFormatFile);
                gdb.clearBoard(gc);
            } else {
                System.out.println("Something wrong with file");
            }

            try {
                fileHandler.readStringToBoard(fileHandler.readOpenFile(RLEFormatFile), gdb);
                gdb.drawNextGen(gc, colorPicker.getValue(), board);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("ArrayOutOfBound");
            }
            }catch (NullPointerException np){
            System.out.println("No file selected");

        }
    }



    private enum PatternType{
        Glider("Glider", glider),
        Exploder("Exploder", exploder),
        Painting("Painting", painting),
        Draw("Draw your own", new int[][]{});

        private int[][] pattern;
        private String displayName;

        PatternType(String d, int[][] p){
            pattern = p;
            displayName = d;
        }



        @Override
        public String toString(){return displayName;}
    }

    private void getSelectedPattern(){
        PatternType selectedPattern = choicePattern.getValue();
        newPattern = selectedPattern.pattern;

        if(selectedPattern == PatternType.Draw){
            canvas.setOnMouseClicked(onClickCellEvent);
            canvas.setOnMouseDragged(onDragCellEvent);
        }else {
            canvas.setOnMouseClicked(onDrawSelectedPattern);
            canvas.setOnMouseDragged(null);

        }
    }

    private EventHandler onDrawSelectedPattern = new EventHandler(){
        @Override
        public void handle(Event event) {

            int[][] pattern = newPattern;
            int offsetX = getXPosition(event);
            int offsetY = getYPosition(event);

            try{
            for (int x = 0; x < pattern.length; x++) {
                for (int y = 0; y < pattern[x].length; y++)
                    if (pattern[x][y] == 1) {
                        gdb.cellGrid[x + offsetX][y + offsetY].setState(true);
                        System.out.println("1");
                    } else if(pattern[x][y] == 0) {
                        gdb.cellGrid[x + offsetX][y + offsetY].setState(false);
                        System.out.println("0");
                    }
                    gdb.drawNextGen(gc, colorPicker.getValue(), board);
            }
        }catch(ArrayIndexOutOfBoundsException e){
                System.out.println("ArrayOutOfBound exception catched");
            }
        }
    };

    private int getYPosition(Event event) {
        MouseEvent e = (MouseEvent) event;
        return (int) (e.getY() / board.cellSize);
    }

    private int getXPosition(Event event) {
        MouseEvent e = (MouseEvent) event;
        return (int) (e.getX() / board.cellSize);
    }


    /*

    public void onOpenRLEFile()throws Exception{
        fileChooser = new FileChooser();
        RLEFormatFile = fileChooser.showOpenDialog(null);
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("RLE Files", "*.rle");

        fileReader = new FileReader(RLEFormatFile);
        fileChooser.getExtensionFilters().addAll(filter);

        gdb.clearBoard(gc);

        readsStringBuilderAndDraw();
    }

    public void readsStringBuilderAndDraw() throws Exception{

        gdb.clearBoard(gc);
        StringBuilder stringBuilder = returnFileToStringBuilder(bufferedReader);
        int x = 0;
        int y = 0;



        while(stringBuilder.length() > 0){
            int count = returnAmountOfCellsInt(stringBuilder);
            char type = stringBuilder.charAt(0);
            stringBuilder.deleteCharAt(0);

            for(int i = 0; i < count; i++) {


                if (Character.toString(type).matches("[o]")) {
                    System.out.println(count + " " + 'o');
                    gdb.cellGrid[x][y].setState(true);
                    x++;
                } else if (Character.toString(type).matches("[b]")) {
                    System.out.println(count + " " + 'b');
                    gdb.cellGrid[x][y].setState(false);
                    x++;
                } else if (Character.toString(type).matches("[$]")){
                    System.out.println("Mellomrom");
                    x=0;
                    y++;
                }
            }


        }
        System.out.println(stringBuilder);
        gdb.drawNextGen(gc, colorPicker.getValue(), board);

    }


    public StringBuilder returnFileToStringBuilder(BufferedReader bufferedReader){
        try {
            this.bufferedReader = bufferedReader;
            bufferedReader = new BufferedReader(fileReader);
            stringBuilder = new StringBuilder();
            String text = "";

            int nextChar;


            while((nextChar = bufferedReader.read()) != -1){

                stringBuilder.append((char)nextChar);

            }



        }catch (FileNotFoundException f){
            System.out.println("File not found");
        }catch (IOException io){
            System.out.println("Ok");
        }

        return stringBuilder;
    }

    public int returnAmountOfCellsInt(StringBuilder stringBuilder){

        int numberLenght = 0;
        String amountOfCells = "";
        int amountOfCellsInteger;

        if (!Character.isDigit(stringBuilder.charAt(0))) {
            return 1;
        }


        for (int i = 0; i < stringBuilder.length(); i++) {

            char character = stringBuilder.charAt(i);

            if (!Character.toString(character).matches("[0-9]")) {

                numberLenght = i ;
                break;
            }
        }

        amountOfCells = stringBuilder.substring(0, numberLenght);
        amountOfCellsInteger = Integer.parseInt(amountOfCells);

        stringBuilder.delete(0, numberLenght);

        return amountOfCellsInteger;

    }

  /*
    public void closeButtonAction(){
        // get a handle to the stage
        Stage stage = (Stage) saveAs.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
       */
/*
    StringBuilder stringBuilder;

    public StringBuilder readFileAndReturnString(BufferedReader bufferedReader){

        try {

            stringBuilder = new StringBuilder();

            int nextChar;

            while((nextChar = bufferedReader.read()) != -1 && nextChar != '$'){
                stringBuilder.append((char)nextChar);
            }

        }catch (FileNotFoundException f){
            System.out.println("File not found");
        }catch (IOException io){
            System.out.println("Ok");
        }

        return stringBuilder;
    }




    public void readRLEPattern(File RLEFormatFile) throws Exception {
        fileReader = new FileReader(RLEFormatFile);
        bufferedReader = new BufferedReader(fileReader);
        gdb.clearBoard(gc);

        int nextChar;
        int x = 5;
        int y = 5;

        try {

            while ((nextChar = bufferedReader.read()) != -1) {
                char c = (char) nextChar;

                if (Character.toString(c).matches("[o]")) {
                    gdb.cellGrid[x][y].setState(true);
                    x++;
                } else if (Character.toString(c).matches("[b]")) {
                    gdb.cellGrid[x][y].setState(false);
                    x++;
                } else if (Character.toString(c).matches("[$]")) {
                    x = 5;
                    y++;
                }

            }

            bufferedReader.close();

        } catch (NullPointerException NPE) {
            System.out.println("User did`nt select file");
        } catch (ArrayIndexOutOfBoundsException Ar) {
            System.out.println("Too big");
        }
        gdb.drawNextGen(gc, colorPicker.getValue(), board);

    }


    public void openAndReadRLEFormat() throws Exception {
        fileChooser = new FileChooser();
        RLEFormatFile = fileChooser.showOpenDialog(null);

        if (RLEFormatFile != null) {
            readRLEPattern(RLEFormatFile);
        }
    }

*/




    public void exitProgram() {
        System.exit(0);
    }
}



