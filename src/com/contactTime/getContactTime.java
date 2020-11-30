package com.contactTime;

import java.util.ArrayList;
import java.util.List;

public class getContactTime extends GetNCData {

    private final List<BlockObject> OP_OBJECT_LIST; // Operation Cycle as List of Block Objects
    private final float ALLOWED_CONTACT_TIME; //In seconds
    private List<String> output;
    private final float Z_SAFE_STOP;
    private final float X_SAFE_STOP;
    private boolean isExternal;

    public getContactTime(List<BlockObject> OpObjectList, float reqConTime, float ZsafeSt, float XsafeSt, boolean isExt) {
        this.Z_SAFE_STOP = ZsafeSt;
        this.X_SAFE_STOP = XsafeSt;
        this.OP_OBJECT_LIST = OpObjectList;
        ALLOWED_CONTACT_TIME = reqConTime * 60;
        isExternal = isExt;
    }

    public void TrackContactTime(){
        float totalContactTime = 0f;
        List<String> finalNC = new ArrayList<>();
        for (int i = 0; i < OP_OBJECT_LIST.size(); i++) {
            BlockObject block = OP_OBJECT_LIST.get(i);
            if (!block.isRapidMovement() && block.isMotion() && block.getAXIS_MOVED().contains("Z")){
                float xStart = axisPrevCoord(i, "X");
                float zStart = axisPrevCoord(i, "Z");
                float zEnd = getGcodeValue(block.getBLOCK(), "Z", "Z");
                int rpm = getRPM(xStart, block.getG96(), block.getG50());
                float timePerBlock = getTime(zStart, zEnd, rpm, block.getFEED());

                if (timePerBlock > ALLOWED_CONTACT_TIME && !block.getAXIS_MOVED().contains("X")){
                    float remainingContactTime = ALLOWED_CONTACT_TIME - totalContactTime;
                    float zRemainderDist_1 = secToMill(remainingContactTime, rpm, block.getFEED());
                    finalNC.add(safeReturn((zRemainderDist_1 * -1), xStart));
                    totalContactTime = 0;
                    float remaindblockTime = timePerBlock - remainingContactTime;
                    if (remaindblockTime > ALLOWED_CONTACT_TIME){
                        float zRemainderDist_2 = secToMill(ALLOWED_CONTACT_TIME, rpm, block.getFEED());
                        finalNC.add(safeReturn( (zRemainderDist_1 * -1) + (zRemainderDist_2 * -1) , xStart));
                        totalContactTime = remaindblockTime - ALLOWED_CONTACT_TIME;
                    }
                }else if (timePerBlock + totalContactTime > ALLOWED_CONTACT_TIME && !block.getAXIS_MOVED().contains("X")){
                    float remainCOntactTime = ALLOWED_CONTACT_TIME - totalContactTime;
                    float zRemainderDist = secToMill(remainCOntactTime, rpm, block.getFEED());
                    finalNC.add(safeReturn((zRemainderDist * -1), xStart));
                    totalContactTime = timePerBlock - remainCOntactTime;
                }
                else{
                    totalContactTime += timePerBlock;
                }

            }
            finalNC.add(block.getBLOCK());
        }
        output = finalNC;
    }

    private float getTime(Float zStart, Float zEnd, int rpm, float feed){
        float mm_min = feed * rpm;
        float combinedTravelLength = getAbsolute(zStart) + getAbsolute(zEnd);
        return (combinedTravelLength / mm_min) * 60;

    }

    private float axisPrevCoord(int blocIndex, String axis){
        float prevCoor = 99999;
        for (int i = blocIndex - 1; i > 0 ; i--) {
            BlockObject block = OP_OBJECT_LIST.get(i);
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

    private String safeReturn(float remCtDst, float xCoord){
        int remainCutDist = Math.round(remCtDst);
        String result = "G1Z" + remainCutDist + ".\n";
        result += safeLeadOut();
        result += "\n" + "M00" + "\n";
        result += "(CHANGE EDGE)" + "\n" + "\n";
        result += safeLeadIn(remainCutDist, xCoord);
        return result;
    }


    private String safeLeadOut(){
        String safeLeadOut = "W1.U2." + "\n";
        safeLeadOut += "G0X" + X_SAFE_STOP + "\n";
        safeLeadOut += "G0Z" + Z_SAFE_STOP + "\n";
        if (!isExternal){
            safeLeadOut = "W1.U-2." + "\n";
            safeLeadOut += "G0Z" + Z_SAFE_STOP + "\n";
            safeLeadOut += "G0X" + X_SAFE_STOP + "\n";
        }
        return safeLeadOut;
    }

    private String safeLeadIn(int zStartPos, float xStartPos){
        String safeLeadIn = "G0Z" + (zStartPos + 2) + ".\n";
        safeLeadIn += "G0X" + (xStartPos + 2) + ".\n";
        safeLeadIn += "G1X" + xStartPos + "Z" + (zStartPos + 1) + ".";
        if (!isExternal){
            safeLeadIn = "G0X" + (xStartPos - 2) + ".\n";
            safeLeadIn += "G0Z" + (zStartPos + 2) + ".\n";
            safeLeadIn += "G1X" + xStartPos + "Z" + (zStartPos + 1) + ".";
        }
        return safeLeadIn;
    }

    public List<String> getOutput() {
        return output;
    }
}
