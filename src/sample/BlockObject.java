package sample;

import java.util.List;

public class BlockObject {

    private final int g50; //Maximum RPM cap
    private final int g96; // Tool surface speed (Vc)
    private final float f; // Current feedrate

    public boolean isMotion; // True is block contains motion commands
    public boolean isRapidMovement; // True if move is rapid G0 command
    public float Xmove; // Move along X axis
    public float Zmove; // Move along Z axis
    public String block;


    public BlockObject(String block, int G50, int G96, float F) {
        this.block = block;
        g50 = G50;
        g96 = G96;
        f = F;
    }





}
