public class GetLowCaseLoop {
    public static char getLowCase(char c) {
        char t = 'f';
        while(t <= 'z' && t != c - 'A' + 'a') {
            if(c < 'F' || c > 'Z') {
                return 0;
            }
            t++;
        }
        return t;
    }
}