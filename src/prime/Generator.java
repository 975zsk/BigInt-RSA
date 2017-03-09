package prime;

import java.util.concurrent.ThreadLocalRandom;

import bigint.BigIntDec;

/**
 *
 * @author Jakob Pupke
 */
public class Generator {
    public static BigIntDec getRandom(int size) {
        if(size < 0) {
            throw new IllegalArgumentException("Negative size is not allowed");
        }
        if(size == 1) {
            return new BigIntDec(ThreadLocalRandom.current().nextInt(0, 10));
        }
        int[] digits = new int[size];
        // between 1 and 9 for first digit
        digits[0] = ThreadLocalRandom.current().nextInt(1, 10);
        int i;
        for(i = 1; i < size; i++) {
            // between 0 and 9
            digits[i] = ThreadLocalRandom.current().nextInt(0, 10);
        }
        return new BigIntDec(digits);
    }
    
    public static BigIntDec getRandomOdd(int size) {
        BigIntDec rand = getRandom(size);
        if(rand.isEven()) {
            rand = rand.add(new BigIntDec(1));
        }
        return rand;
    }
}
