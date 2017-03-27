package prime;

import bigint.BigInt;
import bigint.BigIntFactory;

import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Jakob Pupke
 */
abstract class PrimeTester<T extends BigInt> {

    T n;
    T nMinusOne;
    T exponent;
    // Concurrency control
    private final Control control = new Control();
    protected BigIntFactory<T> factory;
    private Generator<T> generator;

    PrimeTester(T n, BigIntFactory<T> fact) {
        this.n = n;
        nMinusOne = (T) n.dec();
        this.factory = fact;
        this.generator = new Generator<>(fact);
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

    abstract protected boolean condition(T result);

    boolean passesPreTest() {
        if(n.lte(factory.getOne()))
            return false;

        if(n.isEven() && !n.equals(factory.getTwo()))
            return false;

        return true;
    }

    boolean isInFirstPrimes() {
        if(n.gt(factory.build(37))) {
            return false;
        }
        for(int i : FIRST_PRIMES) {
            if(n.equals(factory.build(i))) {
                return true;
            }
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
        T a;
        T res;

        for(int base : bases) {
            a = factory.build(base);
            res = (T) a.powMod(exponent, n);
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
        T a = generator.getRandomOdd(size);
        if(a.gte(n))
            a = nMinusOne;
        T res = (T) a.powMod(exponent, n);
        return condition(res);
    }

}
