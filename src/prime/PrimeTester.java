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
    private final Control control = new Control();
    
    public PrimeTester(BigInt n) {
        this.n = n;
        nMinusOne = n.sub(BigInt.ONE);
    }
    
    public class Control {
        private volatile boolean isPrime = true;
    }
    
    static final BigInt[] FIRST_PRIMES = {
        new BigInt(2),
        new BigInt(3),
        new BigInt(5),
        new BigInt(7),
        new BigInt(11),
        new BigInt(13),
        new BigInt(17),
        new BigInt(19),
        new BigInt(23),
        new BigInt(29),
        new BigInt(31),
        new BigInt(37)
    };
    
    abstract protected boolean condition(BigInt result);
    
    public boolean passesPreTest() {
        if(n.lte(BigInt.ONE))
            return false;
        
        if(n.isEven() && !n.equals(BigInt.TWO))
            return false;
        
//        for(BigInt a : FIRST_PRIMES) {
//            if(!a.powMod(nMinusOne, n).equals(BigInt.ONE))
//                return false;
//        }
        
        return true;
    }
    
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
