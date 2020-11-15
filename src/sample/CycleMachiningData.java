package sample;


public class CycleMachiningData {



    public int getGcodeValue(String cncBlock, String code, char ch ){
        String value = "";
        if (cncBlock.contains(code)){
            int index = cncBlock.indexOf(ch);
            for (int i = index + 1; i < cncBlock.length() ; i++) {
                if (Character.isDigit(cncBlock.charAt(i))) {
                    value += cncBlock.charAt(i);
                } else{
                    break;
                }
            }
            if (!value.isEmpty()) return Integer.parseInt(value);
        }
        return -1;
    }

}
