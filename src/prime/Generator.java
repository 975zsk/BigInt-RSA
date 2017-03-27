package prime;

import bigint.BigInt;
import bigint.BigIntFactory;

import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Jakob Pupke
 */
public class Generator<T extends BigInt> {

    private BigIntFactory factory;
    private int base;

    public Generator(BigIntFactory<T> fact) {
        this.factory = fact;
        this.base = fact.getBase();
    }

    public T getRandom(int size) {
        if(size < 0) {
            throw new IllegalArgumentException("Negative size is not allowed");
        }
        if(size == 1) {
            return (T) factory.build(ThreadLocalRandom.current().nextInt(0, base));
        }
        int[] digits = new int[size];
        // between 1 and base for first digit
        digits[0] = ThreadLocalRandom.current().nextInt(1, base);
        int i;
        for(i = 1; i < size; i++) {
            // between 1 and base
            digits[i] = ThreadLocalRandom.current().nextInt(0, base);
        }
        return (T) factory.build(digits);
    }
    
    public T getRandomOdd(int size) {
        T rand = getRandom(size);
        if(rand.isEven()) {
            rand = (T) rand.add((T) factory.build(1));
        }
        return rand;
    }
}
