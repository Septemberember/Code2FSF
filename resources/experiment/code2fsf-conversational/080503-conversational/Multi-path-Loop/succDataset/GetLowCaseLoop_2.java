public class GetLowCaseLoop {
    public static char getLowCase(char c) {
        char t = 'f';
        while(t >= 'a' && t != c - 'A' + 'a') {
            if(c < 'A' || c > 'F') {
                return 0;
            }
            t--;
        }
        return t;
    }
}