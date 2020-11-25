package com.contactTime;

import java.util.ArrayList;
import java.util.List;

public class getContactTime extends GetNCData {

    private final List<BlockObject> NCLIST;
    private final float ALLOWED_CONTACT_TIME; //In seconds
    private List<String> output;
    private final float Z_SAFE_STOP;
    private final float X_SAFE_STOP;

    public getContactTime(List<BlockObject> ncList, float reqConTime, float ZsafeSt, float XsafeSt) {
        this.Z_SAFE_STOP = ZsafeSt;
        this.X_SAFE_STOP = XsafeSt;
        this.NCLIST = ncList;
        ALLOWED_CONTACT_TIME = reqConTime * 60;
    }

    public void TrackContactTime(){
        float totalContactTime = 0f;
        List<String> finalNC = new ArrayList<>();
        for (int i = 0; i < NCLIST.size(); i++) {
            BlockObject block = NCLIST.get(i);
            if (!block.isRapidMovement() && block.isMotion() && block.getAXIS_MOVED().contains("Z")){
                float xStart = axisPrevCoord(i, "X");
                float zStart = axisPrevCoord(i, "Z");
                float zEnd = getGcodeValue(block.getBLOCK(), "Z", "Z");
                int rpm = getRPM(xStart, block.getG96(), block.getG50());
                float timePerBlock = getTime(zStart, zEnd, rpm, block.getFEED());

                if ((totalContactTime + timePerBlock) > ALLOWED_CONTACT_TIME && !block.getAXIS_MOVED().contains("X")){
                    float contactTimeLeft = ALLOWED_CONTACT_TIME - totalContactTime;
                    float zRemainingContactDistance = secToMill(contactTimeLeft, rpm, block.getFEED());
                    finalNC.add(safeReturn(true, zRemainingContactDistance, xStart));
                }
            }
            finalNC.add(block.getBLOCK());
        }
        for (String s : finalNC ){
            System.out.println(s);
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

    private String safeReturn(boolean isExternal, float ZinitialCoord, float xCoord){
        String safeLeadOut = "W2.U4.";
        if (!isExternal) safeLeadOut = "W2.U-4.";

        String result = "Z" + ZinitialCoord + "\n";
        result += safeLeadOut + "\n" +
        "G0X" + X_SAFE_STOP + "\n" + "G0Z" + Z_SAFE_STOP +
        "M00" + "\n" + "(CHANGE EDGE)" + "\n" +
        "G0" + (ZinitialCoord + 4.0) + "\n" +
        "X" + (xCoord + 4.0) + "\n" +
        "G1" + (ZinitialCoord + 2) + "X" + xCoord;
        return result;
    }



}
