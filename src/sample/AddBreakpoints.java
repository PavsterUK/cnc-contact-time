package sample;


import java.util.ArrayList;
import java.util.List;

public class AddBreakpoints {
    ArrayList<String> gCode;
    double zRapidTo;
    double contTime;
    int blockNum;
    boolean isInternal;

    public AddBreakpoints(ArrayList<String> gCode, double zRapidTo, double contTime, int blockNum, boolean isInternal) {
        this.gCode = gCode;
        this.zRapidTo = zRapidTo;
        this.contTime = contTime;
        this.blockNum = blockNum;
        this.isInternal = isInternal;
    }

    public List<String> extractOP(){
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
        return gCode.subList(opStart, opEnd);
    }
}
