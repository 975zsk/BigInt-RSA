package prime;

import bigint.BigInt;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Jakob Pupke
 */
abstract class PrimeTester {
    
    BigInt n;
    BigInt nMinusOne;
    BigInt exponent;
    // Concurrency control
    private final Control control = new Control();
    
    public PrimeTester(BigInt n) {
        this.n = n;
        nMinusOne = n.sub(BigInt.ONE);
    }
    
    public class Control {
        /*
        Threads check this flag to see if another 
        thread has found a witness yet
        */
        private volatile boolean isPrime = true;
    }
    
    static final int[] FIRST_PRIMES = {
        2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37
    };
    
    abstract protected boolean condition(BigInt result);
    
    public boolean passesPreTest() {
        if(n.lte(BigInt.ONE))
            return false;
        
        if(n.isEven() && !n.equals(BigInt.TWO))
            return false;
        
        return true;
    }
    
    public boolean isInFirstPrimes() {
        if(n.gt(new BigInt(37))) {
            return false;
        }
        for(int i : FIRST_PRIMES) {
            if(n.equals(new BigInt(i))) {
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
    
    protected boolean isPrime(BigInt number, int[] bases) {
        BigInt a;
        BigInt res;
        
        for(int base : bases) {
            a = new BigInt(base);
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
        BigInt a = Generator.getRandomOdd(size);
        if(a.gte(n))
            a = nMinusOne;
        BigInt res = a.powMod(exponent, n);
        return condition(res);
    }
    
}
