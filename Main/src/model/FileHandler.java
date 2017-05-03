package model;

import javafx.scene.canvas.*;
import javafx.scene.canvas.Canvas;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Andre on 18.04.2017.
 */
public class FileHandler {

    private int x = 0;
    private int y = 0;

    /**
     * This method uses readFileReturnString to return a string of a URL file.
     * Method is made to seperate URL reading from OpenFile reading.
     *
     * @param bufferedReader Takes a bufferedReader to read a file.
     * @return Returns a String of the read file.
     * @throws Exception Throws the exception.
     */

    public String readURLFile(BufferedReader bufferedReader)throws Exception{

        return readFileReturnString(bufferedReader);
    }


    /**
     * This method takes a file and throws it into the readFileReturnString
     * method and returns the String. Methods are separated to be more lucid,
     * and to be used by other methods.
     *
     * @param RLEFile Takes a file, converts it to a String.
     * @return Return the String of the read file.
     * @throws Exception Throws any Exceptions, handled in Controller/PatternController.
     */
    public String readOpenFile(File RLEFile) throws Exception{

        FileReader fileReader = new FileReader(RLEFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        return readFileReturnString(bufferedReader);

    }

    /**
     * This method takes a BufferedReader as parameter which is used
     * to read a file and return a StringBuilder. It checks if there is any
     * signs/letters that is used in the GameOfLife RLE format. If the methods
     * finds a number, it multiplies that number with the next sign.
     *
     * 3o = ooo
     * 2b = bb
     * etc.
     *
     * Method used by readURLFile and readOpenFile.
     *
     * @param bufferedReader Uses a bufferedReader to read a file.
     * @return Returns a String which is basically a StringBuilder.toString().
     * @throws Exception Throws any exceptions.
     */


    public String readFileReturnString(BufferedReader bufferedReader) throws Exception{

        StringBuilder stringBuilder = new StringBuilder();

        if(bufferedReader.readLine() == null){
            System.out.println("No file");
        }

        String text = "";
        String line = "";

        while((line = bufferedReader.readLine())!= null){

            if(line.matches("[o,b,!,$,0-9]*")){
                text = text.concat(line + "\n");
            }
        }

        bufferedReader.close();

        Pattern pattern = Pattern.compile("\\d+|[ob]|\\$");
        Matcher matcher = pattern.matcher(text);

        while(matcher.find()){
            int number = 1;
            if(matcher.group().matches("\\d+")){
                number = Integer.parseInt(matcher.group());
                matcher.find();
            }

            for(int i = 0; i < number; i++){
                stringBuilder.append(matcher.group());
            }
        }

        return stringBuilder.toString();

    }

    /**
     * This method is used to set new values of the current cellArrayList.
     * It reads a String and creates a pattern in the main cellArrayList.
     * Starts with offsetY and offsetX to start setting the pattern in the center
     * of the screen.
     *
     * @param RLEText String is read and creates new boolean state values in the cellArrayList.
     * @param dynamicGameBoard Uses the used DynamicGameBoard.
     */

    public void readStringToBoard(String RLEText, DynamicGameBoard dynamicGameBoard){

        int offsetY = 15;
        int offsetX = 30;

        for(int i = 0; i < RLEText.length(); i++){
            if(RLEText.charAt(i) == 'o'){
                dynamicGameBoard.cellArrayList.get(x+offsetX).get(y+offsetY).setArrayState(true);
                x++;
            }
            if(RLEText.charAt(i) == 'b'){
                dynamicGameBoard.cellArrayList.get(x+offsetX).get(y+offsetY).setArrayState(false);
                x++;
            }
            if(RLEText.charAt(i) == '$'){
                x = 0;
                y++;
            }
        }
    }

    /*public void readStringToBoard(String RLEText, GraphicsDisplayBoard gdb){

        for(int i = 0; i < RLEText.length(); i++){
            if(RLEText.charAt(i) == 'o'){
                gdb.cellGrid[x][y].setState(true);
                x++;
            }
            if(RLEText.charAt(i) == 'b'){
                gdb.cellGrid[x][y].setState(false);
                x++;
            }
            if(RLEText.charAt(i) == '$'){
                x = 0;
                y++;
            }
        }
    }*/



}
