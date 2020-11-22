package com.contactTime;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
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
        printToTextArea(proc.getGCLIST());
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
               printToTextArea(processedNcFile.getGCLIST());
           }
        }
    }

    public void calculate(ActionEvent actionEvent) {
        if (processedNcFile == null) return;
        GetBlockObjectList getBlockObjectList = new GetBlockObjectList(processedNcFile.extractOP(blockNo));
        List<BlockObject> boList = getBlockObjectList.getBlockObjectList();
        int bno = 0;
        for (BlockObject bo : boList){
            System.out.println("Block no :" + bno);
            System.out.println("axis moved :" + bo.getAXIS_MOVED().toString());
            System.out.println("is rapid " + bo.isRapidMovement());
            System.out.println("is motion " + bo.isMotion());
            System.out.println("------------------------------------------------");
            bno++;
        }
    }




}


