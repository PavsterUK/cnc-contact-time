package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    @FXML TextArea textArea;
    ArrayList<String> gCodeList = new ArrayList<>();

    public void ChooseFilePressed(ActionEvent actionEvent) {

        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            textArea.setText("NC FILE ACCEPTED : \n " + chooser.getSelectedFile().getAbsolutePath());
        }
        makeStringList(chooser.getSelectedFile());
    }

    private void makeStringList(File file){

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
            String line = null;
            while ( (line = reader.readLine()) != null) {
                gCodeList.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        calcBreakpoints();
    }

    public void calcBreakpoints(){
        AddBreakpoints addbrkpts = new AddBreakpoints(gCodeList, 10.0, 10.0, 2, false);
        List<String> strings = addbrkpts.extractOP();
        textArea.setText(strings.toString());
    }


}


