public class AddHalfSelf_Mutant2{
    public static int addHalfSelf(int x){
        int n = x;
        int addNum = 0;
        if(x < 0){
            n = -x;
        }
        while(n > 0){
            if(n % 2 == 1){
                addNum++;
            }
            n--;
        }
        if(x < 0){
            return x - addNum;
        }
        else{
            return x + addNum;
        }
    }
}