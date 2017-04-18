package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Andre on 18.04.2017.
 */
public class FileHandler {

    public String readRLEFile(File RLEFile) throws Exception{

        FileReader fileReader = new FileReader(RLEFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        String text = "";
        String line = "";

        if(bufferedReader.readLine() == null){
            System.out.println("No file");
        }

        while((line = bufferedReader.readLine())!= null){

            if(line.matches("[o,b,!,$,0-9]*")){
                text = text.concat(line + "\n");
            }
        }

        Pattern pattern = Pattern.compile("\\d+|[ob]|\\$");
        Matcher matcher = pattern.matcher(text);

        while(matcher.find()){
            int number = 1;
            if(matcher.group().matches("\\d+")){
                number = Integer.parseInt(matcher.group());
                System.out.println(number);
                matcher.find();
            }

            for(int i = 0; i < number; i++){
                stringBuilder.append(matcher.group());
            }
        }

        return stringBuilder.toString();

    }

    int x = 5;
    int y = 5;

    Board board;


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
                x = 5;
                y++;
            }
        }
    }


}
