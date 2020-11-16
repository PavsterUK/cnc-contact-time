package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class Controller {

    @FXML TextArea textArea;
    @FXML TextField blockNo;
    ProcessNCFile processedNcFile;


    public void ChooseFilePressed(ActionEvent actionEvent) {
        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            procFile(chooser.getSelectedFile());
        }
    }

    public void procFile(File ncFile){
        ProcessNCFile proc = new ProcessNCFile(ncFile);
        printToTextArea(proc.getGcList());
        processedNcFile = proc;

    }

    public void printToTextArea(List<String> ncCode){
        String output = "";
        for (String line : ncCode){
            output += line + "\n";
        }
        textArea.setText(output);
    }


    public void printOP(KeyEvent keyEvent) {
        if (processedNcFile != null) {
           if (processedNcFile.checkIfBlockExists(blockNo)){
               printToTextArea(processedNcFile.extractOP(blockNo));
           }
           else {
               printToTextArea(processedNcFile.getGcList());
           }
        }
    }

    public void calculate(ActionEvent actionEvent) {
        if (processedNcFile == null) return;
        CalculateBreakPoints calculateBreakPoints = new CalculateBreakPoints(processedNcFile.extractOP(blockNo));

    }


}


