public class AddHalfSelf_Mutant1{
    public static int addHalfSelf(int x){
        int n = x;
        int addNum = 0;
        if(x < 0){
            n = -x;
        }
        while(n > 0){
            if(n % 2 == 0){
                addNum++;
            }
            n--;
        }
        if(x < 0){
            return x - addNum - 1;
        }
        else{
            return x + addNum + 1;
        }
    }
}