package model;

import javafx.event.Event;
import javafx.scene.input.MouseEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Andre on 18.04.2017.
 */
public class FileHandler {




    public String readFile(File RLEFile) throws Exception{

        FileReader fileReader = new FileReader(RLEFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
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
        System.out.println(text);

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

    int x = 0;
    int y = 0;



    public void readStringToBoard(String RLEText, GraphicsDisplayBoard gdb){

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
    }


}
