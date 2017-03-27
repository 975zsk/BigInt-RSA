package rsa;

import bigint.BigInt;
import bigint.BigIntFactory;
import prime.Generator;
import prime.PrimeTestRunner;
import prime.PrimeTesterEuler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 *
 * @author Jakob Pupke
 */
public final class Rsa<T extends BigInt> {
    
    private int size = 15;
    private int rounds = 20;
    
    private PrimeTestRunner<PrimeTesterEuler, T> tester;
    private Generator<T> generator;

    T p, q, n, e, d, phiN;
    
    public Rsa(BigIntFactory<T> fact) {
        setup(fact);
    }
    
    public Rsa(T e, int size, BigIntFactory<T> fact) throws InterruptedException, ExecutionException {
        setup(fact);
        this.e = e;
        this.size = size;
        setRandomPrimes();
        calculateValues();
    }
    
    public Rsa(T e, BigIntFactory<T> fact) throws InterruptedException, ExecutionException {
        setup(fact);
        this.e = e;
        setRandomPrimes();
        calculateValues();
    }
    
    public Rsa(T p, T q, T e, BigIntFactory<T> fact) {
        setup(fact);
        this.p = p;
        this.q = q;
        this.e = e;
        calculateValues();
    }
    
    private void calculateValues() {
        calculateN();
        calculatePhiN();
        calculateD();
    }

    private void setup(BigIntFactory<T> fact) {
        tester = new PrimeTestRunner<>(new PrimeTesterEuler.Factory(), fact);
        this.generator = new Generator<>(fact);
    }
    
    private void setRandomPrimes() throws InterruptedException, ExecutionException {
        // get p and q concurrently
        ExecutorService executor = Executors.newFixedThreadPool(2);
        List<Future<T>> list = new ArrayList<>();

        for(int i = 0; i <= 1; i++) {
            Callable<T> worker = new Callable<T>() {
                @Override
                public T call() throws Exception {
                    return getRandomPrime();
                }
            };
            Future<T> submit = executor.submit(worker);
            list.add(submit);
        }

        try {
            Future<T> pFuture = list.get(0);
            p = pFuture.get();
            Future<T> qFuture = list.get(1);
            q = qFuture.get();
            while(p.equals(q)) {
                // Ups p == q
                // This will probably never happen
                q = getRandomPrime();
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Execution failed: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }
    
    Keys generateRSAKeys() {
        PublicKey publicKey = new PublicKey(e, n);
        SecretKey secretKey = new SecretKey(p, q, d, n);
        return new Keys(secretKey, publicKey);
    }
    
    static <T> T encrypt(PublicKey pk, BigInt block) {
        return (T) block.powMod(pk.e, pk.n);
    }
    
    static <T> T decrypt(SecretKey sk, BigInt cipher) {
        return (T) cipher.powMod(sk.d, sk.n);
    }
    
    private void calculateN() {
        n = (T) p.mul(q);
    }
    
    private void calculatePhiN() {
        phiN = (T) p.dec().mul(q.dec());
        // TODO assert that gcd(e, phiN) == 1
    }
    
    private T getRandomPrime() {
        T a = generator.getRandomOdd(size);
        while(!tester.isPrime(a, rounds, false)) {
            a = (T) a.inc(2);
        }
        return a;
    }

    private void calculateD() {
        d = (T) e.egcd(phiN).u;
        // Oh my god....
        // http://crypto.stackexchange.com/questions/10805/how-does-one-deal-with-a-negative-d-in-rsa
        if(d.isNeg()) {
            d = (T) d.add(phiN);
        }
    }
}
