package prime;

import bigint.BigIntDec;

import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Jakob Pupke
 */
abstract class PrimeTester {
    
    BigIntDec n;
    BigIntDec nMinusOne;
    BigIntDec exponent;
    // Concurrency control
    private final Control control = new Control();
    
    public PrimeTester(BigIntDec n) {
        this.n = n;
        nMinusOne = n.sub(BigIntDec.ONE);
    }
    
    public class Control {
        /*
        Threads check this flag to see if another 
        thread has found a witness yet
        */
        private volatile boolean isPrime = true;
    }
    
    public static final int[] FIRST_PRIMES = {
        2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37
    };
    
    abstract protected boolean condition(BigIntDec result);
    
    public boolean passesPreTest() {
        if(n.lte(BigIntDec.ONE))
            return false;
        
        if(n.isEven() && !n.equals(BigIntDec.TWO))
            return false;
        
        return true;
    }
    
    public boolean isInFirstPrimes() {
        if(n.gt(new BigIntDec(37))) {
            return false;
        }
        for(int i : FIRST_PRIMES) {
            if(n.equals(new BigIntDec(i))) {
                return true;
            }
        }
        return false;
    }
    
    /*
    This method is called by the threads spawned by the PrimeTestRunner
    */
    public boolean testForWitnesses(int rounds) {
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
    
    protected boolean isPrime(BigIntDec number, int[] bases) {
        BigIntDec a;
        BigIntDec res;
        
        for(int base : bases) {
            a = new BigIntDec(base);
            res = a.powMod(exponent, n);
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
        BigIntDec a = Generator.getRandomOdd(size);
        if(a.gte(n))
            a = nMinusOne;
        BigIntDec res = a.powMod(exponent, n);
        return condition(res);
    }
    
}
