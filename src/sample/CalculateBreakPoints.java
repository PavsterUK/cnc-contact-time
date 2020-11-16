package sample;

import java.util.ArrayList;
import java.util.List;

public class CalculateBreakPoints {

    private int G50; //Maximum RPM cap
    private int G96; // Tool surface speed (Vc)
    private float M00; // Rapid to Z position
    private float Feed; // Current feedrate
    private int currRPM; // Current RPM
    private List<String> operation; // Operation Cycle list

    public CalculateBreakPoints(List<String> operation){
        this.operation = operation;
    }

    // Update CurrRpm with X value
    private void getRPM(float x){
        currRPM = (int) ((G96 * 1000) / (x * Math.PI));
    }

    private void getNCData(){
        int lg = operation.size();
        String currrentModal;
        List<Integer> Zmovements = new ArrayList<>();
        List<Integer> Xmovements = new ArrayList<>();
        for (int i = 0; i < lg; i++) {
            String block = operation.get(i);
            if (block.contains("G50")) G50 = getGcodeValue(block, "G50", 'S');
            if (block.contains("G96")) G96 = getGcodeValue(block, "G96", 'S');
            if (block.contains("F")) Feed = getGcodeValue(block, "F", 'F');
            if (block.contains("G0") || block.contains("G00")) currrentModal = "G0";
            if (block.contains("G1") || block.contains("G01")) {currrentModal = "G1";
            Xmovements.add();

        }
    }

    // Get value of required GCode
    public float getGcodeValue(String cncBlock, String code, char ch ){
        String value = "";
        if (cncBlock.contains(code)){
            int index = cncBlock.indexOf(ch);
            for (int i = index + 1; i < cncBlock.length() ; i++) {
                if (Character.isDigit(cncBlock.charAt(i)) || cncBlock.charAt(i) == '.') {
                    value += cncBlock.charAt(i);
                } else{
                    break;
                }
            }
            if (!value.isEmpty()) return Float.parseFloat(value);
        }
        return -1;
    }

    public void test(){
        double elapsedTime = 0;
        for (int i = 100; i >= 10; i--) {
            getRPM(i - 1);
            int mm_min = (int) (0.2 * currRPM);
            float time_per_1mm = 1.00f / mm_min;
            elapsedTime += time_per_1mm * 60;
            System.out.println(String.format(i + "mm DIA  --> " + currRPM + " RPM --> " + (currRPM * 0.2) + "mm/min --> " + "%.3f" + " seconds" + " (time per 1mm %.3f secs", elapsedTime, time_per_1mm * 60)  );
        }

    }
    

}
