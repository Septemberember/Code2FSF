public class WaterFee_Original {
    public static int calcWaterFee(int tons) {
        int n = tons;
        int fee = 0;
        while (n > 0) {
            if (n > 30) {
                fee += 5;
            } else if (n > 10) {
                fee += 4;
            } else {
                fee += 3;
            }
            n--;
        }
        return fee;
    }
}