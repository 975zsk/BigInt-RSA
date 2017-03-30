package rsa;

import bigint.BigInt;
import bigint.BigInt32;
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
public final class Rsa {
    
    private int size = 15;
    private int rounds = 20;

    private PrimeTestRunner<PrimeTesterEuler> tester;
    private Generator generator;

    BigInt32 p, q, n, e, d, phiN;
    
    public Rsa() {
        setup();
    }
    
    public Rsa(BigInt32 e, int size) throws InterruptedException, ExecutionException {
        setup();
        this.e = e;
        this.size = size;
        setRandomPrimes();
        calculateValues();
    }
    
    public Rsa(BigInt32 e) throws InterruptedException, ExecutionException {
        setup();
        this.e = e;
        setRandomPrimes();
        calculateValues();
    }
    
    public Rsa(BigInt32 p, BigInt32 q, BigInt32 e) {
        setup();
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

    private void setup() {
        tester = new PrimeTestRunner<>(new PrimeTesterEuler.Factory());
        generator = new Generator();
    }
    
    private void setRandomPrimes() throws InterruptedException, ExecutionException {
        p = getRandomPrime();
        q = getRandomPrime();
        while(p.equals(q)) {
            // Ups p == q
            // This will probably never happen
            q = getRandomPrime();
        }
        // get p and q concurrently (UNSTABLE)
        /*ExecutorService executor = Executors.newFixedThreadPool(2);
        List<Future<BigInt32>> list = new ArrayList<>();

        for(int i = 0; i <= 1; i++) {
            Callable<BigInt32> worker = new Callable<BigInt32>() {
                @Override
                public BigInt32 call() throws Exception {
                    return getRandomPrime();
                }
            };
            Future<BigInt32> submit = executor.submit(worker);
            list.add(submit);
        }

        try {
            Future<BigInt32> pFuture = list.get(0);
            p = pFuture.get();
            Future<BigInt32> qFuture = list.get(1);
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
        }*/
    }
    
    Keys generateRSAKeys() {
        PublicKey publicKey = new PublicKey(e, n);
        SecretKey secretKey = new SecretKey(p, q, d, n);
        return new Keys(secretKey, publicKey);
    }
    
    static BigInt32 encrypt(PublicKey pk, BigInt block) {
        return (BigInt32) block.powMod(pk.e, pk.n);
    }
    
    static BigInt32 decrypt(SecretKey sk, BigInt cipher) {
        return (BigInt32) cipher.powMod(sk.d, sk.n);
    }
    
    private void calculateN() {
        n = (BigInt32) p.mul(q);
    }
    
    private void calculatePhiN() {
        phiN = (BigInt32) p.dec().mul(q.dec());
    }
    
    private BigInt32 getRandomPrime() {
        BigInt32 a = generator.getRandomOdd(size);
        while(!tester.isPrime(a, rounds, false)) {
            a = (BigInt32) a.inc(2);
        }
        return a;
    }

    private void calculateD() {
        d = (BigInt32) e.egcd(phiN).u;
        // Oh my god....
        // http://crypto.stackexchange.com/questions/10805/how-does-one-deal-with-a-negative-d-in-rsa
        if(d.isNeg()) {
            d = (BigInt32) d.add(phiN);
        }
    }
}
