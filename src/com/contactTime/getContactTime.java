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

                if ((timePerBlock + totalContactTime) > ALLOWED_CONTACT_TIME && !block.getAXIS_MOVED().contains("X")){
                    float contactTimeLeft = ALLOWED_CONTACT_TIME - totalContactTime;
                    if (contactTimeLeft > 0) {
                        float zRemainingContactDistance = secToMill(contactTimeLeft, rpm, block.getFEED());
                        finalNC.add(safeReturn(true, (zStart - zRemainingContactDistance), xStart));
                        totalContactTime = timePerBlock - zRemainingContactDistance;
                    } else {
                       finalNC.add(safeReturn(true, zStart, xStart));
                       totalContactTime = 0;
                    }
                } else {
                    totalContactTime += timePerBlock;
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

    private float axisPrevCoord(int blocIndex, String axis){
        float prevCoor = 99999;
        for (int i = blocIndex - 1; i > 0 ; i--) {
            BlockObject block = NCLIST.get(i);
            if (block.getAXIS_MOVED().contains(axis)){
                prevCoor = getGcodeValue(block.getBLOCK(), axis, axis);
                return prevCoor;
            }
        }
        return prevCoor;
    }

    private int getRPM(float x, int ss, int g50){
        int rpm = (int) ((ss * 1000) / (x * Math.PI));
        return Math.min(rpm, g50);
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

        String result = "G1Z" + ZinitialCoord + "\n";
        result += safeLeadOut + "\n";
        result += "G0X" + X_SAFE_STOP + "\n";
        result += "G0Z" + Z_SAFE_STOP + "\n";
        result += "\n" + "M00" + "\n";
        result += "(CHANGE EDGE)" + "\n" + "\n";
        result += "G0Z" + (ZinitialCoord + 4.0) + "\n";
        result += "X" + (xCoord + 4.0) + "\n";
        result += "G1Z" + (ZinitialCoord + 2.0) + "X" + xCoord;
        return result;
    }



}
