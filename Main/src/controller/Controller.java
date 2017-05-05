package controller;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.util.Duration;
import model.*;

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
    ScrollPane scrollPane;
    @FXML
    Canvas canvas;
    @FXML
    Button startPauseBtn, clearBtn, saveAs;
    @FXML
    ColorPicker colorPicker;
    @FXML
    Slider speedSlider, zoomSlider, volumeSlider;
    @FXML
    Text showGen, showAliveCells;
    @FXML
    TextArea textAreaPattern;
    @FXML
    ChoiceBox<PatternType> choicePattern;
    @FXML
    CheckBox checkBoxSound;


    //Controller class

    private Board board;
    private DynamicGameBoard dynamicGameBoard;
    private GraphicsContext gc;
    private AnimationTimer timer;
    private int speed;
    private GraphicsDisplayBoard gdb;
    Color aliveCellColor;
    Stage stage;
    FileChooser fileChooser;
    File RLEFormatFile;
    BufferedReader bufferedReader;
    GraphicsDisplayDynamicBoard graphicsDisplayDynamicBoard;
    AudioPlaySound audioPlaySound;
    Timeline timeline;
    Alert alert;


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        onPatternDraw();
        onChangeColor();
        timerMethod();


        colorPicker.setValue(Color.valueOf("#1a3399"));
        aliveCellColor = colorPicker.getValue();
        gc = canvas.getGraphicsContext2D();
        audioPlaySound = new AudioPlaySound();

        dynamicGameBoard = new DynamicGameBoard(250, 250, false);
        graphicsDisplayDynamicBoard = new GraphicsDisplayDynamicBoard();


        graphicsDisplayDynamicBoard.drawNextGen(dynamicGameBoard, gc, aliveCellColor);
        zoomSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {


                canvas.setScaleX(newValue.doubleValue() / 10);
                canvas.setScaleY(newValue.doubleValue() / 10);

            }
        });


       /* zoomSlider.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                canvas.setScaleX(100);
               canvas.setScaleY(zoomSlider.getValue() *10 );
                canvas.setScaleX(zoomSlider.getValue()* 10);


               // canvas.setScaleY(zoomSlider.getValue());
            }
        });*/

     /*   canvas.setOnScroll((ScrollEvent event) -> {

            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            // Adjust the zoom factor as per your requirement
            double zoomFactor = 1.05;
            double deltaY = event.getDeltaY();
            if (deltaY < 0){
                zoomFactor = 2.0 - zoomFactor;
            }


            double cameraZoomFactor = zoomFactor;

            gc.translate(300, 300);
            gc.scale(cameraZoomFactor, cameraZoomFactor);
            gc.translate(-300,-300);


        });
       */
     /*   board = new Board(canvas);
        gdb = new GraphicsDisplayBoard(gc, canvas);


        showGenerationText();
        timerMethod();
        onChangeColor();
*/
        showText(showGen, dynamicGameBoard.generation);
        showText(showAliveCells, dynamicGameBoard.cellsAlive);
    }

    /**
     * Method mutes and un-mutes Audio clip.
     */

    public void onMuteSound() {
        if (!checkBoxSound.isSelected()) {
            audioPlaySound.pause();
            System.out.println("Pause");
        } else if (checkBoxSound.isSelected()) {
            audioPlaySound.resume();
            System.out.println("Resume");
        }
    }

    /**
     * Method uses two methods to run the game.
     * One controls the ArrayLists,
     * one controls the drawing methods.
     */

    public void nextGenDyn() {
        dynamicGameBoard.onNextGen();
        graphicsDisplayDynamicBoard.drawNextGen(dynamicGameBoard, gc, aliveCellColor);
    }

    /**
     * Method step into the next generation of the game
     */

    public void onNextGenStep() {
        timer.stop();
        startPauseBtn.setText("Start");
        nextGenDyn();
        dynamicGameBoard.generation.set(dynamicGameBoard.generation.get() + 1);
    }

    /**
     * This method is used for the ChoiceBox in the game.
     * Automatically uses the last Pattern as default.
     */

    private void onPatternDraw() {
        choicePattern.getItems().addAll(PatternType.values());
        choicePattern.valueProperty().addListener(e -> getSelectedPattern());
        choicePattern.getSelectionModel().selectLast();
    }

    /**
     * Method binds a text with a IntegerProperty
     */

    private void showText(Text text, IntegerProperty integerProperty) {
        text.textProperty().bind(Bindings.concat(integerProperty));
    }

    /**
     * Method clears a IntegerProperty and sets it to 0.
     *
     * @param integerProperty Clear the used IntegerProperty.
     */

    private void clearText(IntegerProperty integerProperty) {
        integerProperty.setValue(0);
    }


    /**
     * Method is used as timer for the Game.
     * Commented code is old code for static version of the game.
     */
    public void timerMethod() {
        speed = 0;
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                speed++;
                if (speed > Math.abs((int) speedSlider.getValue())) {
                    nextGenDyn();
                    speed = 0;
                    dynamicGameBoard.generation.set(dynamicGameBoard.generation.get() + 1);

                    /*gdb.updateBoard(gc);
                    gdb.drawNextGen(gc, aliveCellColor, board);
                    board.generation.set(board.generation.get() + 1);*/
                }
            }
        };
    }

    /**
     * Method used for Clear button.
     * Clears all living cells and stops the timer.
     */

    public void onClear() {
        dynamicGameBoard.clearCellState();
        graphicsDisplayDynamicBoard.clearDrawing(dynamicGameBoard, gc);
        startPauseBtn.setText("Start");
        timer.stop();
        clearText(dynamicGameBoard.cellsAlive);
        clearText(dynamicGameBoard.generation);
    }

    /**
     * Method used for Start/Stop button.
     * Stops the timer for Game of Life and stops the timer.
     */

    public void onStop() {
        startPauseBtn.setText("Start");
        timer.stop();
    }

    /**
     * Method used for Start/Stop button.
     * Starts the timer for Game of Life and starts the timer.
     */

    public void onStart() {
        if (startPauseBtn.getText().equals("Start")) {
            startPauseBtn.setText("Pause");
            timer.start();
        } else if (startPauseBtn.getText().equals("Pause")) {
            startPauseBtn.setText("Start");
            timer.stop();
        }
    }



    /**
     * Method used for ColorPicker in UI.
     */
    private void onChangeColor() {
        colorPicker.setOnAction(e -> {
            aliveCellColor = colorPicker.getValue();
            graphicsDisplayDynamicBoard.drawNextGen(dynamicGameBoard, gc, aliveCellColor);
        });
    }


    /**
     * EventHandler method for drawing on the canvas with simple
     * mouse clicks.
     */

    private EventHandler onClickCellEvent = new EventHandler() {
        @Override
        public void handle(Event event) {
            int x = getXPosition(event);
            int y = getYPosition(event);

            if (x == -1 || y == -1 || x == canvas.getHeight() / 10 || y == canvas.getWidth() / 10) {
                throw new ArrayIndexOutOfBoundsException();
            }
            if (dynamicGameBoard.checkCellAlive(x, y)) {
                dynamicGameBoard.cellArrayList.get(x).get(y).setArrayState(false);

                gc.setFill(Color.WHITE);
                gc.fillRect(x * graphicsDisplayDynamicBoard.cellSize, y * graphicsDisplayDynamicBoard.cellSize, graphicsDisplayDynamicBoard.getCellSize(), graphicsDisplayDynamicBoard.getCellSize());

            } else {
                dynamicGameBoard.cellArrayList.get(x).get(y).setArrayState(true);

                gc.setFill(colorPicker.getValue());
                gc.fillRect(x * graphicsDisplayDynamicBoard.cellSize, y * graphicsDisplayDynamicBoard.cellSize, graphicsDisplayDynamicBoard.getCellSize(), graphicsDisplayDynamicBoard.getCellSize());


            }
        }
    };


    /**
     * EventHandler method for drawing on the canvas with
     * a mouse dragged event.
     */

    public EventHandler onDragCellEvent = new EventHandler() {
        @Override
        public void handle(Event event) {
            int x = getXPosition(event);
            int y = getYPosition(event);
            try {
                if (x == -1 || y == -1 || x == canvas.getHeight() / 10 || y == canvas.getWidth() / 10) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                if (!dynamicGameBoard.checkCellAlive(x, y)) {
                    dynamicGameBoard.cellArrayList.get(x).get(y).setArrayState(true);

                    gc.setFill(aliveCellColor);
                    gc.fillRect(x * graphicsDisplayDynamicBoard.getCellSize(), y * graphicsDisplayDynamicBoard.getCellSize(), graphicsDisplayDynamicBoard.getCellSize(), graphicsDisplayDynamicBoard.getCellSize());

                }
            } catch (ArrayIndexOutOfBoundsException aoob) {
                System.out.println("Array is out of bound ( TOP OR LEFT ) ");
            }

        }
    };




    /**
     * Method stops the current Game and loads a new stage
     * with Create Pattern, Open Pattern, Open URL Pattern methods.
     *
     * @throws Exception Throws any Exceptions.
     */
    public void createPattern() throws Exception {
        onStop();
        Parent root = FXMLLoader.load(getClass().getResource("../view/createOwnPattern.fxml"));
        stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Patterns");
        stage.show();


    }

    /**
     * Method is used to open a .rle file. Uses methods from
     * FileHandler to read and draw a file to the DynamicGameBoard
     *
     * @throws Exception Throws any exceptions.
     */

    public void onReadURLFile() throws Exception {

        FileHandler fileHandler = new FileHandler();
        onStop();

        try {
            String urlPath = JOptionPane.showInputDialog("Paste URL");
            URL url = new URL(urlPath);
            URLConnection conn = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        } catch (MalformedURLException mal) {
            System.out.println("Wrong URLPath");
            alert.show();
        } catch (NullPointerException nu) {
            nu.printStackTrace();
            System.out.println("NullPointer");
        }

        String text = "";
        try {
            text = fileHandler.readURLFile(bufferedReader);
        } catch (NullPointerException nullPoint) {
            System.out.println("No URL file found");
        }
        dynamicGameBoard.clearCellState();
        graphicsDisplayDynamicBoard.clearDrawing(dynamicGameBoard, gc);
        fileHandler.readStringToBoard(text, dynamicGameBoard);
        graphicsDisplayDynamicBoard.drawNextGen(dynamicGameBoard, gc, colorPicker.getValue());

    }

    /**
     * Method is used to open a .rle file. Uses methods from
     * FileHandler to read and draw a file to the DynamicGameBoard
     *
     * @throws Exception Throws any exceptions.
     */


    public void onOpenRLEFile() throws Exception {
        alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Error occurred");
        alert.setContentText("No file selected");


        onStop();

        try {
            FileHandler fileHandler = new FileHandler();
            fileChooser = new FileChooser();
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("RLE Files (*.rle)", "*.rle");
            fileChooser.getExtensionFilters().add(filter);
            RLEFormatFile = fileChooser.showOpenDialog(null);


            if (RLEFormatFile != null) {
                fileHandler.readOpenFile(RLEFormatFile);
                dynamicGameBoard.clearCellState();
                graphicsDisplayDynamicBoard.clearDrawing(dynamicGameBoard, gc);
                fileHandler.readStringToBoard(fileHandler.readOpenFile(RLEFormatFile), dynamicGameBoard);
                graphicsDisplayDynamicBoard.drawNextGen(dynamicGameBoard, gc, colorPicker.getValue());
            } else {
                System.out.println("Something wrong with file");
            }

            try {

            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("ArrayOutOfBound");
            }
        } catch (NullPointerException np) {
            alert.show();
            System.out.println("No file selected");
        }
    }


    /**
     *
     * Enum with different static patterns to be used in game.
     *
     */

    private enum PatternType {
        Glider("Glider", glider),
        Exploder("Exploder", exploder),
        Painting("Painting", painting),
        Draw("Draw your own", new int[][]{});

        private int[][] pattern;
        private String displayName;


        PatternType(String d, int[][] p) {
            pattern = p;
            displayName = d;
        }


        @Override
        public String toString() {
            return displayName;
        }
    }

    /**
     * Method gets the selected Pattern in Game of Life.
     * If user selected PatternType.Draw, used can only use
     * methods in the scope. Else he can only use the
     * onDrawSelectedPattern method.
     */

    private void getSelectedPattern() {
        PatternType selectedPattern = choicePattern.getValue();
        newPattern = selectedPattern.pattern;

        if (selectedPattern == PatternType.Draw) {
            canvas.setOnMouseClicked(onClickCellEvent);
            canvas.setOnMouseDragged(onDragCellEvent);
        } else {
            canvas.setOnMouseClicked(onDrawSelectedPattern);
            canvas.setOnMouseDragged(null);

        }
    }


    /**
     * EventHandler method for drawing a selected Pattern
     * on the canvas. Draws the pattern from where user
     * clicked on canvas.
     */

    private EventHandler onDrawSelectedPattern = new EventHandler() {
        @Override
        public void handle(Event event) {

            int[][] pattern = newPattern;
            int offsetX = getXPosition(event);
            int offsetY = getYPosition(event);

            try {
                for (int x = 0; x < pattern.length; x++) {
                    for (int y = 0; y < pattern[x].length; y++)
                        if (pattern[x][y] == 1) {
                            dynamicGameBoard.cellArrayList.get(x + offsetX).get(y + offsetY).setArrayState(true);
                            System.out.println("1");
                        } else if (pattern[x][y] == 0) {
                            dynamicGameBoard.cellArrayList.get(x + offsetX).get(y + offsetY).setArrayState(false);
                            System.out.println("0");
                        }
                    graphicsDisplayDynamicBoard.drawNextGen(dynamicGameBoard, gc, colorPicker.getValue());
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("ArrayOutOfBound exception catched");
            }
        }
    };


    /**
     * Method returns the y position of a MouseClicked.
     * Divides the value by cellSize(10) to get exact coordinate.
     *
     * @param event Takes a event as parameter.
     * @return Returns the x coordinate of Mouse click, divided by cellSize.
     */
    private int getYPosition(Event event) {
        MouseEvent e = (MouseEvent) event;
        return (int) (e.getY() / 10);
    }

    /**
     * Method returns the x position of a MouseClicked.
     * Divides the value by cellSize(10) to get exact coordinate.
     *
     * @param event Takes a event as parameter.
     * @return Returns the x coordinate of Mouse click, divided by cellSize.
     */

    private int getXPosition(Event event) {
        MouseEvent e = (MouseEvent) event;
        return (int) (e.getX() / 10);
    }

    /**
     * Method exits the program.
     */

    public void exitProgram() {
        System.exit(0);
    }

    /*private void showGenerationText() {
        showGen.textProperty().bind(Bindings.concat(board.generation));
    }

    private void clearGenerationText() {
        board.generation.set(0);
    }*/


     /*  private void onChangeColor() {
        colorPicker.setOnAction(e -> {
            aliveCellColor = colorPicker.getValue();
            gdb.drawNextGen(gc, aliveCellColor, board);
        });
    }*/

     /*public void onClear() {
        board.clearCellState();
        gdb.clearBoard(gc);
        startPauseBtn.setText("Start");
        timer.stop();
        clearGenerationText();
    }*/

        /*public void nextGen() {
        timer.stop();
        startPauseBtn.setText("Start");
        gdb.updateBoard(gc);
        gdb.drawNextGen(gc, aliveCellColor, board);
        board.generation.set(board.generation.get() + 1);
    }*/


      /*private EventHandler onClickCellEvent = new EventHandler() {
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
    };*/




      /*public EventHandler onDragCellEvent = new EventHandler() {
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
    };*/

     /*public void onReadURLFile() throws Exception  {

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

    }*/




   /* public void onOpenRLEFile() throws Exception{

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
    }*/


     /*private EventHandler onDrawSelectedPattern = new EventHandler(){
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
    };*/



   /* private int getYPosition(Event event) {
        MouseEvent e = (MouseEvent) event;
        return (int) (e.getY() / board.cellSize);
    }

    private int getXPosition(Event event) {
        MouseEvent e = (MouseEvent) event;
        return (int) (e.getX() / board.cellSize);
    }*/


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



}



