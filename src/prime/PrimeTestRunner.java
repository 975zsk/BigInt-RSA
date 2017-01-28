package prime;

import bigint.BigInt;
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
public class PrimeTestRunner<T extends PrimeTester> {
    
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();
    T tester;
    TesterFactory<T> fact;
    
    public PrimeTestRunner(TesterFactory<T> fact) {
        this.fact = fact;
    }
    
    public boolean isPrime(BigInt number, int rounds) {
        this.tester = fact.build(number);
        
        if(!tester.passesPreTest()) {
            return false;
        }
        
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        
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
                e.printStackTrace();
                return false;
            }
        }
        
        executor.shutdown();
        
        return true;
    }
    
    public boolean isPrime(BigInt number, int[] bases) {
        this.tester = fact.build(number);
        return tester.isPrime(number, bases);
    }
    
    class Worker implements Callable<Boolean> {
        
        int r;
        
        public Worker(int r) {
            this.r = r;
        }

        @Override
        public Boolean call() throws Exception {
            return tester.testForWitnesses(r);
        }
        
    }
}
