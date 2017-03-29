package model;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

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

    FileChooser fileChooser;
    File saveFile;
    Controller controller;




    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void savePatternButton() throws Exception{
        fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Text files(*.txt)", "*.txt");
        fileChooser.getExtensionFilters().addAll(extensionFilter);

        saveFile = fileChooser.showSaveDialog(null);

        if(saveFile != null){
            saveMethod(textAreaPattern.getText(), saveFile);
        }


    }

    public void saveMethod(String content, File file) throws Exception{
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.close();
    }


}
