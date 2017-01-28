package prime;

import bigint.BigInt;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Jakob Pupke
 */
abstract class PrimeTester {
    
    class Control {
        /* If a thread proves that n is not a prime
           it sets the flag to false. Then all other
           threads know that they can stop testing
        */
        public volatile boolean isPrime = true;
    }
    
    private static final int NUM_THREADS = 5;
    
    BigInt n;
    BigInt nMinusOne;
    BigInt exponent;
    private final Control control = new Control();
    
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
    
    public boolean isPrime(BigInt number, int rounds) throws ExecutionException {
        setFields(number);
        
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        
        if(firstPrimesContains(number))
            return true;
        
        /* This actually makes it slower */ 
        //if(!passesPreTest(number))
        //    return false;
        
        List<Future<Boolean>> list = new ArrayList<>();
        int roundsPerThread = rounds / NUM_THREADS;
        for(int i = 0; i < NUM_THREADS; i++) {
            Callable<Boolean> worker = new Worker(roundsPerThread);
            Future<Boolean> submit = executor.submit(worker);
            list.add(submit);
        }
        
        for(Future<Boolean> future : list) {
            try {
                boolean res = future.get();
                if(!res) return false;
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Execution failed..");
                return false;
            } finally {
                executor.shutdown();
            }
        }
        
        return true;
    }
    
    private boolean testForWitnesses(int rounds) {
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
    
    class Worker implements Callable<Boolean> {
        
        int r;
        
        public Worker(int r) {
            this.r = r;
        }

        @Override
        public Boolean call() throws Exception {
            return testForWitnesses(r);
        }
        
    }
}
