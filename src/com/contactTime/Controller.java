package com.contactTime;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import java.util.List;

public class Controller {

    @FXML TextArea textArea;
    @FXML TextField blockNo;
    ProcessNCFile processedNcFile;
    List<BlockObject> blockObjectsList;


    public void ChooseFilePressed(ActionEvent actionEvent) {
        ProcessNCFile proc = new ProcessNCFile();
        printToTextArea(proc.getNcList());
        processedNcFile = proc;
    }


    public void printToTextArea(List<String> ncList){
        String output = "";
        for (String line : ncList){
            output += line + "\n";
        }
        textArea.setText(output);
    }


    public void printOP(KeyEvent keyEvent) {
        if (processedNcFile != null) {
           if (processedNcFile.checkIfBlockExists(blockNo)){
               List<String> op = processedNcFile.extractOP(blockNo);
               blockObjectsList = new GetNCData(op).getBlockObjectList();
               printToTextArea(op);
           }
           else {
               printToTextArea(processedNcFile.getNcList());
           }
        }
    }

    public void calculate(ActionEvent actionEvent) {
        if (processedNcFile == null) return;
        getContactTime gc = new getContactTime(blockObjectsList);
        gc.Calculate();
    }




}


