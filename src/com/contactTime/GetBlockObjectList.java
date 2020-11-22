package com.contactTime;

import java.util.ArrayList;
import java.util.List;

public class GetBlockObjectList {

    private int G50 = 0; //Maximum RPM cap
    private int G96 = 0; // Tool surface speed (Vc)
    private float Feed = 0; // Current feedrate
    private List<String> OP; // Operation as list of Strings

    public GetBlockObjectList(List<String> operation){
        this.OP = operation;
    }
    public GetBlockObjectList(){};


    public List<BlockObject> getBlockObjectList(){
        List<BlockObject> blockObjectList = new ArrayList<>();
        String currrentModal = "notSet";
        for (int i = 0; i < OP.size(); i++) {
            String block = OP.get(i);
            currrentModal = getModal(block, currrentModal);
            if (block.contains("G50")) G50 = (int) getGcodeValue(block, "G50", 'S');
            if (block.contains("G96")) G96 = (int) getGcodeValue(block, "G96", 'S');
            if (block.contains("F")) Feed = getGcodeValue(block, "F", 'F');
            ArrayList<String> axisMoved = containsAxisMove(block);
            blockObjectList.add(i, new BlockObject(block, G50, G96, Feed, axisMoved, currrentModal));
        }
        return blockObjectList;
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
        return -999999f; // Returns -999999f is no data found
    }

    // Return an array of moved axis
    public ArrayList<String> containsAxisMove(String block){
        ArrayList<String> axisMoved = new ArrayList<>(0);
        if (block.contains("Z") && !block.contains("(")){
            axisMoved.add("Z");
        }
        if (block.contains("X") && !block.contains("(")){
            axisMoved.add("X");
        }
        return axisMoved;
    }


}
