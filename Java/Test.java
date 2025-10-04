
package Java;
import java.util.Collections;

public class Test {
    public static void main(String[] args) {
        int[] numbers = {10, 20, 30, 40, 50};
        for (int number : numbers) {
            System.out.println(number);
        }
        java.util.Arrays.sort(numbers);
        char[] test = new char[26];
        if(test[0] != '\0')
        System.out.println("initial"+test[0]+"tes"+test[25]);

        
    }
}