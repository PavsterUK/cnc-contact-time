package sample;

public class CalculateBreakPoints {
    private int currRPM;



    // Update CurrRpm with X value
//    private void getRPM(float x){
//        currRPM = (int) ((G96 * 1000) / (x * Math.PI));
//    }

    public void test(){
        double elapsedTime = 0;
        for (int i = 100; i >= 10; i--) {
            //getRPM(i - 1);
            int mm_min = (int) (0.2 * currRPM);
            float time_per_1mm = 1.00f / mm_min;
            elapsedTime += time_per_1mm * 60;
            System.out.println(String.format(i + "mm DIA  --> " + currRPM + " RPM --> " + (currRPM * 0.2) + "mm/min --> " + "%.3f" + " seconds" + " (time per 1mm %.3f secs", elapsedTime, time_per_1mm * 60)  );
        }

    }
}
