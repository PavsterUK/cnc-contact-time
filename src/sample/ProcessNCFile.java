package sample;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProcessNCFile {
    File file;

    public ProcessNCFile(File file) {
        this.file = file;
    }

    public List<String> makeStringList(){
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

    public List<String> extractOP(List<String> gCode, int blockNum){
        List<String> singleOP = new ArrayList<>();
        int opStart = 0;
        int opEnd = 0;
        for (int i = 0; i < gCode.size(); i++) {
            if (gCode.get(i).contains("N" + blockNum)) {
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
}
