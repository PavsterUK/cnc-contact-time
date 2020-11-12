package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    @FXML TextArea textArea;

    public void ChooseFilePressed(ActionEvent actionEvent) {

        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            textArea.setText("NC FILE ACCEPTED : \n " + chooser.getSelectedFile().getAbsolutePath());
        }
        procFile(chooser.getSelectedFile());

    }

    public void procFile(File ncFile){
        ProcessNCFile proc = new ProcessNCFile(ncFile);
        displayOnTextArea(proc.extractOP(proc.makeStringList(), 1));

    }

    public void displayOnTextArea(List<String> ncCode){
        String output = "";
        for (String line : ncCode){
            output += line + "\n";
        }
        textArea.setText(output);
    }



}


