package com.contactime.domain;

import java.util.ArrayList;

/*
Represents single g-code block where all
information contained within this block is
broken down into relevant cnc info.
 */


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

    private final int g50; //Maximum RPM cap
    private final int g96; // Tool surface speed (Vc)
    private final float feed; // Current feedrate
    private final String block; // Block of g-code

    private boolean isMotion; // True is block contains motion commands
    private boolean isRapidMovement; // True if move is rapid G0 command
    private final ArrayList<String> AXIS_MOVED; //Contains array of axis engaged in motion


    public BlockObject(String block, int g50, int g96, float feed, ArrayList<String> axisMoved, String currModal) {
        this.block = block;
        this.g50 = g50;
        this.g96 = g96;
        this.feed = feed;
        AXIS_MOVED = axisMoved;
        if (axisMoved.size() != 0)
            isMotion = true;
        if (currModal.equals("G0"))
            isRapidMovement = true;
    }

    public int getG50() {
        return g50;
    }

    public int getG96() {
        return g96;
    }

    public float getFeed() {
        return feed;
    }

    public String getBlock() {
        return block;
    }

}
