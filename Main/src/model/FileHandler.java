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

    public String readURLFile(BufferedReader bufferedReader)throws Exception{

        return readFileReturnString(bufferedReader);
    }

    public String readOpenFile(File RLEFile) throws Exception{

        FileReader fileReader = new FileReader(RLEFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        return readFileReturnString(bufferedReader);

    }


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
