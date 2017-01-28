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
    
    abstract protected BigInt getExponent(BigInt number);
    abstract protected boolean condition(BigInt result);
    
    private boolean passesPreTest(BigInt n) {
        if(n.lte(BigInt.ONE))
            return false;
        
        if(n.isEven() && !n.equals(BigInt.TWO))
            return false;
        
        for(BigInt a : FIRST_PRIMES) {
            if(!a.powMod(nMinusOne, n).equals(BigInt.ONE))
                return false;
        }
        
        return true;
    }
    
    private boolean firstPrimesContains(BigInt n) {
        for(BigInt x : FIRST_PRIMES) {
            if(x.equals(n))
                return true;
        }
        
        return false;
    }
    
    private void setFields(BigInt number) {
        n = number;
        nMinusOne = n.sub(BigInt.ONE);
        exponent = getExponent(number);
    }
    
    public boolean isPrime(BigInt number, int rounds) {
        setFields(number);
        
        if(firstPrimesContains(number))
            return true;
        
        if(!passesPreTest(number))
            return false;
        
        int i;
        for(i = 0; i < rounds; i++) {
            if(isWitness())
                return false;
        }
        
        return true;
    }
    
    protected boolean isPrime(BigInt number, int[] bases) {
        setFields(number);
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
