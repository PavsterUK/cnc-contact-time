package sample;

import java.util.ArrayList;
import java.util.List;

public class ExtractMachiningData {

    private int G50; //Maximum RPM cap
    private int G96; // Tool surface speed (Vc)
    private float Feed; // Current feedrate
    private List<String> operation; // Operation as list of String

    public ExtractMachiningData(List<String> operation){
        this.operation = operation;
    }

    public void getBlockObjectList(){
        int lg = operation.size();
        List<BlockObject> blockObjectList = new ArrayList<>();
        String currrentModal = "notSet";
        for (int i = 0; i < lg; i++) {
            String block = operation.get(i);
            currrentModal = getModal(block, currrentModal);
            if (block.contains("G50"))
                G50 = (int) getGcodeValue(block, "G50", 'S');
            if (block.contains("G96"))
                G96 = (int) getGcodeValue(block, "G96", 'S');
            if (block.contains("F"))
                Feed = getGcodeValue(block, "F", 'F');
            if (containsMotions(block, "G1", "Z", currrentModal)){
                
            }


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
            if (!value.isEmpty())
                return Float.parseFloat(value);
        }
        return Float.POSITIVE_INFINITY; // Returns MAX_VALUE if not found required data
    }

    // Find what is curren modal code, G1 or G0
    private String getModal(String block, String currentModal){
        if ( (block.contains("G0") || block.contains("G00")) && !block.contains("(") ){
            return "G0";
        }else if ( (block.contains("G1") || block.contains("G01" )) && !block.contains("(")){
            return "G1";
        }
        return currentModal;
    }

    //Check if block contains motion commands
    // uses stringbuilder to cover cornercases like: G0/G00, G1/G01, G2/G02, G3/G03
    public boolean containsMotions(String block, String Gcode, String axis, String currModal){
        StringBuilder gCode = new StringBuilder(Gcode);
        gCode.deleteCharAt(1); //If G00/G01 etc passed, delete one zero
        if (block.contains(Gcode) || block.contains(gCode) || currModal.equals(Gcode)){
            if (block.contains(axis) && !block.contains("(")) {
                return true;
            }
        }
        return false;
    }



}
