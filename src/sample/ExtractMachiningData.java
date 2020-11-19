package sample;

import java.util.ArrayList;
import java.util.List;

public class ExtractMachiningData {

    private int G50; //Maximum RPM cap
    private int G96; // Tool surface speed (Vc)
    private float M00; // Rapid to Z position
    private float Feed; // Current feedrate
    private List<String> operation; // Operation Cycle list

    public ExtractMachiningData(){};

    public ExtractMachiningData(List<String> operation){
        this.operation = operation;
    }

    public void makeBlockObjectList(){
        int lg = operation.size();
        List<BlockObject> blockObjectList = new ArrayList<>();
        String currrentModal = "notSet";
        for (int i = 0; i < lg; i++) {
            String block = operation.get(i);
            currrentModal = getModal(block, currrentModal);
            if (block.contains("G50")) G50 = (int) getGcodeValue(block, "G50", 'S');
            if (block.contains("G96")) G96 = (int) getGcodeValue(block, "G96", 'S');
            if (block.contains("F")) Feed = getGcodeValue(block, "F", 'F');
            BlockObject blObj = new BlockObject(block, currrentModal, G50, G96, Feed, M00);
            blockObjectList.add(i, blObj);
        }
        System.out.println(blockObjectList.size());

    }

    // Get value of GCode
    public float getGcodeValue(String cncBlock, String code, char ch ){
        String value = "";
        if (cncBlock.contains(code)){
            int index = cncBlock.indexOf(ch);
            for (int i = index + 1; i < cncBlock.length() ; i++) {
                if (Character.isDigit(cncBlock.charAt(i)) || cncBlock.charAt(i) == '.' || cncBlock.charAt(i) == '-') {
                    value += cncBlock.charAt(i);
                } else{
                    break;
                }
            }
            if (!value.isEmpty()) return Float.parseFloat(value);
        }
        return -0.001f; // Returns -0.001 if unable to extract requred data
    }

    // Find what is curren modal code, G1 or G0
    private String getModal(String block, String currentModal){
        if (block.contains("G0") || block.contains("G00") && !block.contains("(")){
            return "G0";
        }else if (block.contains("G1") || block.contains("G01" ) && !block.contains("(")){
            return "G1";
        }
        return currentModal;
    }



}
