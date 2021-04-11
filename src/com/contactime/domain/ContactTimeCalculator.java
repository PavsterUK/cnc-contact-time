package com.contactime.domain;

import com.contactime.utils.NCDataParser;
import java.util.ArrayList;
import java.util.List;

public class ContactTimeCalculator extends NCDataParser {

    private final List<BlockObject> op_object_list; // Operation Cycle as List of Block Objects
    private final float allowed_contact_time; //In seconds
    private List<String> output;
    private final float z_safe_stop;
    private final float x_safe_stop;
    private final boolean isExternal;

    public ContactTimeCalculator(List<BlockObject> OpObjectList, float reqConTime, float ZsafeSt, float XsafeSt, boolean isExt) {
        this.z_safe_stop = ZsafeSt;
        this.x_safe_stop = XsafeSt;
        this.op_object_list = OpObjectList;
        allowed_contact_time = reqConTime * 60;
        isExternal = isExt;
    }

    public void TrackContactTime(){
        float totalContactTime = 0f;
        List<String> finalNC = new ArrayList<>();
        for (int i = 0; i < op_object_list.size(); i++) {
            BlockObject block = op_object_list.get(i);
            if (!block.isRapidMovement() && block.isMotion() && block.getAXIS_MOVED().contains("Z")){
                float xStart = axisPrevCoord(i, "X");
                float zStart = axisPrevCoord(i, "Z");
                float zEnd = getGcodeValue(block.getBlock(), "Z", "Z");
                int rpm = getRPM(xStart, block.getG96(), block.getG50());
                float timePerBlock = getTime(zStart, zEnd, rpm, block.getFeed());

                if (timePerBlock > allowed_contact_time && !block.getAXIS_MOVED().contains("X")){
                    float remainingContactTime = allowed_contact_time - totalContactTime;
                    float zRemainderDist_1 = secToMill(remainingContactTime, rpm, block.getFeed());
                    finalNC.add(safeReturn((zRemainderDist_1 * -1), xStart));
                    totalContactTime = 0;
                    float remaindblockTime = timePerBlock - remainingContactTime;
                    if (remaindblockTime > allowed_contact_time){
                        float zRemainderDist_2 = secToMill(allowed_contact_time, rpm, block.getFeed());
                        finalNC.add(safeReturn( (zRemainderDist_1 * -1) + (zRemainderDist_2 * -1) , xStart));
                        totalContactTime = remaindblockTime - allowed_contact_time;
                    }
                }else if (timePerBlock + totalContactTime > allowed_contact_time && !block.getAXIS_MOVED().contains("X")){
                    float remainCOntactTime = allowed_contact_time - totalContactTime;
                    float zRemainderDist = secToMill(remainCOntactTime, rpm, block.getFeed());
                    finalNC.add(safeReturn((zRemainderDist * -1), xStart));
                    totalContactTime = timePerBlock - remainCOntactTime;
                }
                else{
                    totalContactTime += timePerBlock;
                }

            }
            finalNC.add(block.getBlock());
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
            BlockObject block = op_object_list.get(i);
            if (block.getAXIS_MOVED().contains(axis)){
                prevCoor = getGcodeValue(block.getBlock(), axis, axis);
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
        safeLeadOut += "G0X" + x_safe_stop + "\n";
        safeLeadOut += "G0Z" + z_safe_stop + "\n";
        if (!isExternal){
            safeLeadOut = "W1.U-2." + "\n";
            safeLeadOut += "G0Z" + z_safe_stop + "\n";
            safeLeadOut += "G0X" + x_safe_stop + "\n";
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
