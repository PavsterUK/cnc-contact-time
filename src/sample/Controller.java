package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import javax.swing.*;

public class Controller {

    @FXML TextArea textArea;

    public void ChooseFilePressed(ActionEvent actionEvent) {
        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            textArea.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }
}


