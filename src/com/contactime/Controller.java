package com.contactime;

import com.contactime.domain.BlockObject;
import com.contactime.domain.ContactTimeCalculator;
import com.contactime.utils.NCDataParser;
import com.contactime.utils.InputFileManager;
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


    InputFileManager processedNcFile;
    List<BlockObject> blockObjectsList;
    List<String> processedOp;

    public void initialize(){
        Image image = new Image(new File("Images/image.png").toURI().toString());
        imageView.setImage(image);
    }

    public void ChooseFilePressed(ActionEvent actionEvent) {
        InputFileManager proc = new InputFileManager();
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
               blockObjectsList = new NCDataParser(op).getBlockObjectList();
               printToTextArea(op);
    }

    public void calculate(ActionEvent actionEvent) {
        if (processedNcFile == null) return;

        float contTime = Float.parseFloat(contactTime.getText());
        float zRapidTo = Float.parseFloat(zRapidPos.getText());
        float xRapidTo = Float.parseFloat(xRapidPos.getText());
        boolean isExternal = !isInternal.isSelected();

        ContactTimeCalculator gc = new ContactTimeCalculator(blockObjectsList, contTime, zRapidTo, xRapidTo, isExternal);
        gc.TrackContactTime();
        printToTextArea(gc.getOutput());
        processedOp = gc.getOutput();
    }

    public void applyToNCFIle(ActionEvent actionEvent){
        InputFileManager proc = processedNcFile;
        List<String> updatedList = InputFileManager.replaceOpAndSaveFile(processedNcFile, processedOp, processedNcFile.extractOP(blockNo));
        printToTextArea(updatedList);

    }



}


