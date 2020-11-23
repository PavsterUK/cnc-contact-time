package com.contactTime;

import java.util.List;

public class getContactTime extends GetNCData {

    private final List<BlockObject> NCLIST;
    private float ContactTime = 0;
    private int surfSp;

    public getContactTime(List<BlockObject> ncList) {
        this.NCLIST = ncList;
    }

    public void Calculate(){
        for (int i = 0; i < NCLIST.size(); i++) {
            BlockObject block = NCLIST.get(i);
            if (!block.isRapidMovement() && block.isMotion() && block.getAXIS_MOVED().contains("Z")){
                float zStart = axisPrevCoord(i, "Z");
                float zEnd = getGcodeValue(block.getBLOCK(), "Z", "Z");

            }
        }
    }


    private float getTime(Float zStart, Float zEnd, float x, float f){
        return 1;
    }

    private float axisPrevCoord(int index, String axis){
        float axs = 99999;
        for (int i = index - 1; i > 0 ; i--) {
            BlockObject block = NCLIST.get(i);
            if (block.getAXIS_MOVED().contains(axis)){
                axs = getGcodeValue(block.getBLOCK(), axis, axis);
            }
        }
        return axs;
    }

    //Update CurrRpm with X value
    private void getRPM(float x, int ss){
        int currRPM = (int) ((ss * 1000) / (x * Math.PI));
    }




}
