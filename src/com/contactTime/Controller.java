package com.contactTime;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import java.util.List;

public class Controller {

    @FXML TextArea displayArea;
    @FXML TextField contactTime;
    @FXML TextField blockNo;
    @FXML TextField zRapidPos;
    @FXML TextField xRapidPos;
    @FXML CheckBox isInternal;

    ProcessNCFile processedNcFile;
    List<BlockObject> blockObjectsList;
    List<String> processedOp;


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
        displayArea.setText(output);
    }

    public void printOP(KeyEvent keyEvent) {
        if (processedNcFile == null) return;
               List<String> op = processedNcFile.extractOP(blockNo);
               blockObjectsList = new GetNCData(op).getBlockObjectList();
               printToTextArea(op);
    }

    public void calculate(ActionEvent actionEvent) {
        float contTime = Float.parseFloat(contactTime.getText());
        float zRapidTo = Float.parseFloat(zRapidPos.getText());
        float xRapidTo = Float.parseFloat(xRapidPos.getText());
        boolean isExternal = !isInternal.isSelected();

        if (processedNcFile == null) return;
        getContactTime gc = new getContactTime(blockObjectsList, contTime, zRapidTo, xRapidTo, isExternal);
        gc.TrackContactTime();
        printToTextArea(gc.getOutput());
        processedOp = gc.getOutput();
    }

    public void applyToNCFIle(ActionEvent actionEvent){
       printToTextArea(ProcessNCFile.overwriteNCFile(processedNcFile, processedOp, processedNcFile.extractOP(blockNo)));

    }



}


