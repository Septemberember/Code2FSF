public class EchoIntLoop_Mutant5 {
    public static int echo(int x) {
        int res = 0;
        for(int i = 0; i < x; i++) {
            res = res - 1;
        }
        return res;
    }
}