package com.contactTime;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

public class Controller {

    @FXML TextArea displayArea;
    @FXML TextField contactTime;
    @FXML TextField blockNo;
    @FXML TextField zRapidPos;
    @FXML TextField xRapidPos;
    @FXML CheckBox isInternal;
    @FXML ImageView imageView;


    ProcessNCFile processedNcFile;
    List<BlockObject> blockObjectsList;
    List<String> processedOp;

    public void initialize(){
        Image image = new Image(new File("/home/pavel/Desktop/cnc-contact-time/Images/image.png").toURI().toString());
        imageView.setImage(image);
    }

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

    public void printOP(KeyEvent keyEvent) throws URISyntaxException {
        if (processedNcFile == null) return;
               List<String> op = processedNcFile.extractOP(blockNo);
               blockObjectsList = new GetNCData(op).getBlockObjectList();
               printToTextArea(op);
    }

    public void calculate(ActionEvent actionEvent) {
        if (processedNcFile == null) return;

        float contTime = Float.parseFloat(contactTime.getText());
        float zRapidTo = Float.parseFloat(zRapidPos.getText());
        float xRapidTo = Float.parseFloat(xRapidPos.getText());
        boolean isExternal = !isInternal.isSelected();

        getContactTime gc = new getContactTime(blockObjectsList, contTime, zRapidTo, xRapidTo, isExternal);
        gc.TrackContactTime();
        printToTextArea(gc.getOutput());
        processedOp = gc.getOutput();
    }

    public void applyToNCFIle(ActionEvent actionEvent){
        ProcessNCFile proc = processedNcFile;
        List<String> updatedList = ProcessNCFile.replaceOpAndSaveFile(processedNcFile, processedOp, processedNcFile.extractOP(blockNo));
        printToTextArea(updatedList);

    }



}


