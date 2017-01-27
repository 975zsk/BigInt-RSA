package prime;

import bigint.BigInt;

import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Jakob Pupke
 */
public class PrimeTesterFermat extends PrimeTester {
    
    @Override
    protected boolean isPrime(BigInt number, int rounds) {
        if(firstPrimesContains(number)) {
            return true;
        }
        if(!passesPreTest(number)) {
            return false;
        }
        int i;
        for(i = 0; i < rounds; i++) {
            if(isWitness(number)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    protected boolean isWitness(BigInt n) {
        int size = ThreadLocalRandom.current().nextInt(3, n.getSize() - 1);
        BigInt a = Generator.getRandomOdd(size);
        return !a.powMod(n.sub(BigInt.ONE), n).equals(BigInt.ONE);
    }

    @Override
    protected boolean isPrime(BigInt n, int[] bases) {
        BigInt a;
        for(int base : bases) {
            a = new BigInt(base);
            if(!a.powMod(n.sub(BigInt.ONE), n).equals(BigInt.ONE)) {
                return false;
            }
        }
        return true;
    }

}
