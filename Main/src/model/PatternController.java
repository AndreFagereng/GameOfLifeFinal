package model;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Andre on 29.03.2017.
 */
public class PatternController implements Initializable {


    @FXML TextArea textAreaPattern;
    @FXML Button saveAs;

    FileChooser fileChooser;
    File saveFile;
    Controller controller;




    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }



    public void savePatternButton() throws Exception{
        String textIntoRle;
        StringBuilder runLengthEncoding = new StringBuilder();
        int amountOfB = 0;
        int amountOfO = 0;

        fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Text files(*.txt)", "*.txt");
        fileChooser.getExtensionFilters().addAll(extensionFilter);

        saveFile = fileChooser.showSaveDialog(null);

        textIntoRle = textAreaPattern.getText();
        textIntoRle += "!";
        for (int i = 0; i < textAreaPattern.getText().length(); i++) {
            if (textIntoRle.charAt(i) == 'b') {
                amountOfB++;
                if (textIntoRle.charAt(i+1) != 'b') {
                    if (amountOfB == 1) {
                        runLengthEncoding.append("b");
                        amountOfB = 0;
                        amountOfO = 0;
                    }
                    else {
                        runLengthEncoding.append(amountOfB + "b");
                        amountOfB = 0;
                        amountOfO = 0;
                    }
                }
            }
            else if (textIntoRle.charAt(i) == 'o') {
                amountOfO++;
                if (textIntoRle.charAt(i+1) != 'o') {
                    if (amountOfO == 1) {
                        runLengthEncoding.append("o");
                        amountOfB = 0;
                        amountOfO = 0;
                    }
                    else {
                        runLengthEncoding.append(amountOfO + "o");
                        amountOfB = 0;
                        amountOfO = 0;
                    }
                }
            }
            else if (textIntoRle.charAt(i) == '$') {
                runLengthEncoding.append("$");
                amountOfB = 0;
                amountOfO = 0;
            } else if (textIntoRle.charAt(i) == '!') {
                continue;
            }
        }

        if (runLengthEncoding.charAt(runLengthEncoding.length()) != '!') {
            runLengthEncoding.append('!');
        }

        if (saveFile != null){
            saveMethod(runLengthEncoding.toString(), saveFile);
            Stage stage = (Stage) saveAs.getScene().getWindow();
            stage.close();
        }


    }

    public void saveMethod(String content, File file) throws Exception{
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.close();
    }
}
