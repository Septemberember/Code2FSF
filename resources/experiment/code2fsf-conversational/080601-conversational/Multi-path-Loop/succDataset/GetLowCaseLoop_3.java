public class GetLowCaseLoop {
    public static char getLowCase(char c) {
        char t = 'a';
        while(t <= 'z' && t != c - 'A' + 'a') {
            if(c < 'A' || c > 'Z') {
                return 0;
            }
            t++;
        }
        return t;
    }
}