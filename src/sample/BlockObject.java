package sample;

import java.util.List;

public class BlockObject {
    public String currentModal;
    public boolean isMotion;
    public boolean isRapidMovement;
    public float Xmove;
    public float Zmove;
    public String block;

    public BlockObject(String block, String currentModal) {
        this.block = block;
        this.currentModal = currentModal;
    }



}
