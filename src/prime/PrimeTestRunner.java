package prime;

import bigint.BigInt;
import bigint.BigIntFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author Jakob Pupke
 */
public class PrimeTestRunner<T extends PrimeTester, E extends BigInt> {
    
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();
    private int roundsPerThread;
    private T tester;
    private TesterFactory<T, E> fact;
    private BigIntFactory<E> bigIntFactory;
    
    public PrimeTestRunner(TesterFactory<T, E> fact, BigIntFactory<E> bigIntFac) {
        this.fact = fact;
        this.bigIntFactory = bigIntFac;
    }
    
    boolean isPrime(E number, int rounds) {
        return isPrime(number, rounds, true);
    }
    
    public boolean isPrime(E number, int rounds, boolean checkFirstPrimes) {
        this.tester = fact.build(number, bigIntFactory);
        
        if(!tester.passesPreTest()) {
            return false;
        }
        
        if(tester.isInFirstPrimes()) {
            return true;
        }
        
        if(checkFirstPrimes && !tester.isPrime(PrimeTester.FIRST_PRIMES)) {
            return false;
        }
        
        // OK randomly choose `a` and run tasks concurrently
        
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        
        List<Future<Boolean>> list = new ArrayList<>();
        
        if(checkFirstPrimes) {
            // We have already checked the first primes
            rounds = rounds - PrimeTester.FIRST_PRIMES.length;
        }
        
        roundsPerThread = rounds / NUM_THREADS;
        for(int i = 0; i < NUM_THREADS; i++) {
            Callable<Boolean> worker = new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return tester.testForWitnesses(roundsPerThread);
                }
            };
            Future<Boolean> submit = executor.submit(worker);
            list.add(submit);
        }
        
        for(Future<Boolean> future : list) {
            try {
                boolean res = future.get();
                if(!res) {
                    executor.shutdown();
                    return false;
                }
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Execution failed: " + e.getMessage());
                return false;
            }
        }
        
        executor.shutdown();
        return true;
    }
    
    boolean isPrime(E number, int[] bases) {
        this.tester = fact.build(number, bigIntFactory);
        return tester.isPrime(bases);
    }
}
