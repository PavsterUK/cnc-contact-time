
package sample;


import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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

    public List<String> extractOP(TextField textField){
        String blockNo = stringifyBlockNo(textField);
        if (!checkIfBlockExists(textField) || blockNo.isEmpty()){
            return Collections.emptyList();
        }
        int opStart = 0;
        int opEnd = 0;
        for (int i = 0; i < gcList.size(); i++) {
            String iBlock = gcList.get(i);
            if (iBlock.contains(blockNo)) {
                opStart = i;
                for (int j = opStart; j < gcList.size(); j++) {
                    String jBlock = gcList.get(j);
                    if (jBlock.contains("M1") || jBlock.contains("M01")){
                        opEnd = j;
                        break;
                    }
                }
                break;
            }
        }
        return gcList.subList(opStart, opEnd + 1);
    }

    public boolean checkIfBlockExists(TextField textField){
        String blockNo = stringifyBlockNo(textField);
        if (blockNo.isEmpty()){
            return false;
        }
        for (int j = 0; j < gcList.size(); j++) {
            if (gcList.get(j).contains(blockNo)){
                return true;
            }
        }
        return false;
    }

    private String stringifyBlockNo(TextField text){
        String blockNo = text.getText().toUpperCase();
        if (!blockNo.matches((".*\\d.*"))){
            return "";
        }
        if (!blockNo.contains("N")){
            blockNo = "N" + blockNo;
        }
        return  blockNo;
    }

    public List<String> getGcList() {
        return gcList;
    }
}