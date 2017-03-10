package bigint;

import java.util.Arrays;

/**
 * Created by jacke on 3/9/17.
 */
public class App {
    public static void main(String[] args) {
        BigInt32 x = new BigInt32("12232647657898754735847358735973");
        BigInt32 y = new BigInt32("1234");
        //BigInt z = x.add(y);
        System.out.println(Arrays.toString(x.digits));
        //System.out.println(Arrays.toString(x.digits));
    }
}
