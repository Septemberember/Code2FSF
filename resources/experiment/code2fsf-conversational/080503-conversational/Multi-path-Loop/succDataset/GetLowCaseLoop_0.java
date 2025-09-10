public class GetLowCaseLoop_Original {
    public static char getLowCase(char c) {
        char t = 'z';
        while(t >= 'a' && t != c - 'A' + 'a') {
            if(c < 'A' || c > 'Z') {
                return 0;
            }
            t--;
        }
        return t;
    }
}