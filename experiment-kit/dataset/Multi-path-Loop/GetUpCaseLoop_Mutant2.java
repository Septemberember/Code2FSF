public class GetUpCaseLoop_Mutant2 {
    public static char getUpCase(char c) {
        if(c < 'a' || c > 'z') {
            return 0;
        }
        char t = 'Z';
        while(t >= 'A' && t != c - 'a' + 'A') {
            t--;
        }
        return t;
    }
}