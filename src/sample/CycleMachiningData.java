package sample;

import java.util.List;

public class CycleMachiningData {

    private List<String> cncOP;
    private int maxSpinSpeed;
    private int surfaceSpeed;



    public CycleMachiningData(List<String> cncOperation){
        this.cncOP = cncOperation;
        getFieldData();
    }

    public void getFieldData(){

    }

    private int getSurfaceSpeed(String cncBlock){
        String surfaceSpeed = "";
        if (cncBlock.contains("G96")){
            int index = cncBlock.indexOf('S');
            for (int i = index; i < cncBlock.length() ; i++) {
                if (Character.isDigit(cncBlock.charAt(i))){
                    surfaceSpeed += cncBlock.charAt(i);
                }
                else{
                    return Integer.parseInt(surfaceSpeed);
                }
            }
        }
    return -1;
    }

}
