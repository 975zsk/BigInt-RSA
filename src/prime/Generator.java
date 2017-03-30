package prime;

import bigint.BigInt32;

import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Jakob Pupke
 */
public class Generator {

    public BigInt32 getRandom(int size) {
        if(size < 0) {
            throw new IllegalArgumentException("Negative size is not allowed");
        }
        if(size == 1) {
            return new BigInt32(ThreadLocalRandom.current().nextInt(0, BigInt32.BASE));
        }
        int[] digits = new int[size];
        // between 1 and base for first digit
        digits[0] = ThreadLocalRandom.current().nextInt(1, BigInt32.BASE);
        int i;
        for(i = 1; i < size; i++) {
            // between 1 and base
            digits[i] = ThreadLocalRandom.current().nextInt(0, BigInt32.BASE);
        }
        return new BigInt32(digits);
    }
    
    public BigInt32 getRandomOdd(int size) {
        BigInt32 rand = getRandom(size);
        if(rand.isEven()) {
            rand = (BigInt32) rand.inc(1);
        }
        return rand;
    }
}
