package sample;


import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProcessNCFile {
    File file;
    List<String> gcList; // GCode File as List of Strings

    public ProcessNCFile(File file) {
        this.file = file;
        this.gcList = makeStringList();
    }

    private List<String> makeStringList(){
        List<String> gCodeList = new ArrayList<>();
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
        return gCodeList;
    }

    public List<String> extractOP(List<String> gCode, TextField opBlocNo){
        int opStart = 0;
        int opEnd = 0;
        for (int i = 0; i < gCode.size(); i++) {
            if (gCode.get(i).contains("N" + opBlocNo.getText()) || gCode.get(i).contains(opBlocNo.getText().toUpperCase())) {
                opStart = i;
                for (int j = opStart; j < gCode.size(); j++) {
                    if (gCode.get(j).contains("M1") || gCode.get(j).contains("M01")){
                        opEnd = j;
                        break;
                    }
                }
                break;
            }
        }
        return gCode.subList(opStart, opEnd + 1);
    }

    public boolean checkIfBlockExists(TextField opBlocNo){
        for (int j = 0; j < gcList.size(); j++) {
            if (gcList.get(j).contains("N" + opBlocNo) || gcList.get(j).contains(opBlocNo.getText().toUpperCase())){
                return true;
            }
        }
        return false;
    }

    public List<String> getGcList() {
        return gcList;
    }
}
