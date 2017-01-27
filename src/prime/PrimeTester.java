package prime;

import bigint.BigInt;

/**
 *
 * @author Jakob Pupke
 */
abstract class PrimeTester {
    
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
    
    protected boolean passesPreTest(BigInt n) {
        if(n.lte(BigInt.ONE)) {
            return false;
        }
        if(n.isEven() && !n.equals(BigInt.TWO)) {
            return false;
        }
        BigInt nMinusOne = n.sub(BigInt.ONE);
        for(BigInt a : FIRST_PRIMES) {
            if(!a.powMod(nMinusOne, n).equals(BigInt.ONE)) {
                return false;
            }
        }
        return true;
    }
    
    protected boolean firstPrimesContains(BigInt n) {
        for(BigInt x : FIRST_PRIMES) {
            if(x.equals(n)) {
                return true;
            }
        }
        return false;
    }
    
    protected abstract boolean isPrime(BigInt number, int rounds);
    protected abstract boolean isPrime(BigInt number, int[] bases);
    protected abstract boolean isWitness(BigInt number);
}
