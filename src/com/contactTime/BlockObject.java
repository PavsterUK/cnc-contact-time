package com.contactTime;

import java.util.ArrayList;

public class BlockObject {

    public boolean isMotion() {
        return isMotion;
    }

    public boolean isRapidMovement() {
        return isRapidMovement;
    }

    public ArrayList<String> getAXIS_MOVED() {
        return AXIS_MOVED;
    }

    private final int G50; //Maximum RPM cap
    private final int G96; // Tool surface speed (Vc)
    private final float FEED; // Current feedrate
    private final String BLOCK;

    private boolean isMotion; // True is block contains motion commands
    private boolean isRapidMovement; // True if move is rapid G0 command
    private final ArrayList<String> AXIS_MOVED; //Contains array of axis engaged in motion


    public BlockObject(String block, int g50, int g96, float feed, ArrayList<String> axisMoved, String currModal) {
        BLOCK = block;
        G50 = g50;
        G96 = g96;
        FEED = feed;
        AXIS_MOVED = axisMoved;
        if (axisMoved.size() != 0)
            isMotion = true;
        if (currModal.equals("G0"))
            isRapidMovement = true;
    }

    public int getG50() {
        return G50;
    }

    public int getG96() {
        return G96;
    }

    public float getFEED() {
        return FEED;
    }

    public String getBLOCK() {
        return BLOCK;
    }


}
