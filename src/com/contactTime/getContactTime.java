package com.contactTime;

import java.util.List;

public class getContactTime extends GetNCData {

    private final List<BlockObject> NCLIST;
    private float requiredContactTime; //In seconds

    public getContactTime(List<BlockObject> ncList, float reqConTime) {
        this.NCLIST = ncList;
        requiredContactTime = reqConTime * 60;
    }

    public void TrackContactTime(){
        float contactTime = 0f;
        for (int i = 0; i < NCLIST.size(); i++) {
            BlockObject block = NCLIST.get(i);
            if (!block.isRapidMovement() && block.isMotion() && block.getAXIS_MOVED().contains("Z")){
                float xStart = axisPrevCoord(i, "X");
                float zStart = axisPrevCoord(i, "Z");
                float zEnd = getGcodeValue(block.getBLOCK(), "Z", "Z");
                int rpm = getRPM(xStart, block.getG96(), block.getG50());
                contactTime += getTime(zStart, zEnd, rpm, block.getFEED());
                if (contactTime >= requiredContactTime){

                }

            }
        }
    }

    private float getTime(Float zStart, Float zEnd, int rpm, float feed){
        float mm_min = feed * rpm;
        float combinedTravelLength = getAbsolute(zStart) + getAbsolute(zEnd);
        return (combinedTravelLength / mm_min) * 60;

    }

    private float axisPrevCoord(int index, String axis){
        float axs = 99999;
        for (int i = index - 1; i > 0 ; i--) {
            BlockObject block = NCLIST.get(i);
            if (block.getAXIS_MOVED().contains(axis)){
                axs = getGcodeValue(block.getBLOCK(), axis, axis);
                return axs;
            }
        }
        return axs;
    }

    private int getRPM(float x, int ss, int g50){
        int rpm = (int) ((ss * 1000) / (x * Math.PI));
        return (rpm > g50) ? g50 : rpm;
    }

    private float getAbsolute(float i){
       return (i < 0) ? (i * -1) : i;
    }

    private float secToMill(float seconds, int rpm, float feed){
        float mm_sec = rpm * feed / 60;
        return seconds * mm_sec;
    }



}
