package prime;

import bigint.BigInt32;
import bigint.BigIntFactory;

import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Jakob Pupke
 */
abstract class PrimeTester {

    final BigInt32 n;
    final BigInt32 nMinusOne;
    BigInt32 exponent;
    // Concurrency control
    private final Control control = new Control();
    private final Generator generator;

    PrimeTester(BigInt32 n) {
        this.n = n;
        nMinusOne = (BigInt32) n.dec();
        this.generator = new Generator();
    }

    private class Control {
        /*
        Threads check this flag to see if another
        thread has found a witness yet
        */
        private volatile boolean isPrime = true;
    }

    static final int[] FIRST_PRIMES = {
            2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37
    };

    abstract protected boolean condition(BigInt32 result);

    boolean passesPreTest() {
        // n > 2 && n is odd OR n is 2
        return (!n.isEven() && n.gt(BigInt32.Factory.TWO)) || n.equals(BigInt32.Factory.TWO);
    }

    boolean isInFirstPrimes() {
        if(n.gt(new BigInt32(FIRST_PRIMES[FIRST_PRIMES.length - 1]))) {
            return false;
        }

        for(int i : FIRST_PRIMES) {
            if(n.equals(new BigInt32(i)))
                return true;
        }

        return false;
    }

    /*
    This method is called by the threads spawned by the PrimeTestRunner
    */
    boolean testForWitnesses(int rounds) {
        int i;
        // Only do this as long as control.isPrime is true
        for(i = 0; i < rounds && control.isPrime; i++) {
            if(isWitness()) {
                control.isPrime = false;
                return false;
            }
        }

        return control.isPrime;
    }

    boolean isPrime(int[] bases) {
        BigInt32 a;
        BigInt32 res;

        for(int base : bases) {
            a = new BigInt32(base);
            res = (BigInt32) a.powMod(exponent, n);
            if(condition(res))
                return false;
        }

        return true;
    }

    private boolean isWitness() {
        final int MIN = 3;
        int size;
        if(n.getSize() < MIN)
            size = n.getSize();
        else
            size = ThreadLocalRandom.current().nextInt(MIN, n.getSize() + 1);
        BigInt32 a = generator.getRandomOdd(size);
        if(a.gte(n))
            a = nMinusOne;
        BigInt32 res = (BigInt32) a.powMod(exponent, n);
        return condition(res);
    }

}
