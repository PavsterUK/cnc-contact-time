package sample;

import java.util.List;

public class BlockObject extends ExtractMachiningData {

    public String currentModal;
    private final int g50; //Maximum RPM cap
    private final int g96; // Tool surface speed (Vc)
    private final float f; // Current feedrate

    public boolean isNonMotion; // True is block contains non motion commands
    public boolean isRapidMovement; // True if move is rapid , G0
    public float Xmove; // Move to X position
    public float Zmove; // Move to Z position
    public String block;


    public BlockObject(String block, String currentModal, int G50, int G96, float F, float M00) {
        this.block = block;
        this.currentModal = currentModal;
        g50 = G50;
        g96 = G96;
        f = F;
    }

    public void getMotions(){
        if (containsMotions("G0", "Z")){
           Zmove = getGcodeValue(block, "Z", 'Z');
           isRapidMovement = true;
        }
        if (containsMotions("G1", "X")){

        }
    }


    //Check if block contains motion commands
    // uses stringbuilder to cover cornercases like: G0/G00, G1/G01, G2/G02, G3/G03
    public boolean containsMotions(String Gcode, String axis){
        StringBuilder gCode = new StringBuilder(Gcode);
        gCode.insert(1, "0");
        if (block.contains(Gcode) || block.contains(gCode) || currentModal.equals(Gcode)){
            if (block.contains(axis) && !block.contains("(")) {
                return true;
            }
        }
        return false;
    }

}
