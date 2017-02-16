package rsa;

import bigint.BigInt;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import prime.Generator;
import prime.PrimeTestRunner;
import prime.PrimeTesterEuler;
import prime.PrimeTesterMillerRabin;

/**
 *
 * @author Jakob Pupke
 */
public final class Rsa {
    
    int size = 50;
    int rounds = 20;
    
    PrimeTestRunner<PrimeTesterMillerRabin> millerRabinTester = new PrimeTestRunner<>(new PrimeTesterMillerRabin.Factory());
    PrimeTestRunner<PrimeTesterEuler> eulerTester = new PrimeTestRunner<>(new PrimeTesterEuler.Factory());
    BigInt p;
    BigInt q;
    BigInt n;
    BigInt e;
    BigInt d;
    BigInt phiN;
    
    public Rsa() {}
    
    public Rsa(BigInt e, int size) throws InterruptedException, ExecutionException {
        this.e = e;
        this.size = size;
        setRandomPrimes();
        calculateValues();
    }
    
    public Rsa(BigInt e) throws InterruptedException, ExecutionException {
        this.e = e;
        setRandomPrimes();
        calculateValues();
    }
    
    public Rsa(BigInt p, BigInt q, BigInt e) {
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
    
    private void setRandomPrimes() throws InterruptedException, ExecutionException {
        // get p and q concurrently
        ExecutorService executor = Executors.newFixedThreadPool(2);
        List<Future<BigInt>> list = new ArrayList<>();
        
        for(int i = 0; i <= 1; i++) {
            Callable<BigInt> worker = new Callable<BigInt>() {
                @Override
                public BigInt call() throws Exception {
                    return getRandomPrime();
                }
            };
            Future<BigInt> submit = executor.submit(worker);
            list.add(submit);
        }
        
        try {
            Future<BigInt> pFuture = list.get(0);
            p = pFuture.get();
            Future<BigInt> qFuture = list.get(1);
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
    
    public Keys generateRSAKeys() {
        PublicKey publicKey = new PublicKey(e, n);
        SecretKey secretKey = new SecretKey(p, q, d, n);
        return new Keys(secretKey, publicKey);
    }
    
    public static Keys generateRSAKeys(BigInt e, int size) throws InterruptedException, ExecutionException {
        Rsa rsa = new Rsa(e, size);
        PublicKey publicKey = new PublicKey(rsa.e, rsa.n);
        SecretKey secretKey = new SecretKey(rsa.p, rsa.q, rsa.d, rsa.n);
        return new Keys(secretKey, publicKey);
    };
    
    public static BigInt encrypt(PublicKey pk, BigInt block) {
        return block.powMod(pk.e, pk.n);
    }
    
    public static BigInt decrypt(SecretKey sk, BigInt cipher) {
        return cipher.powMod(sk.d, sk.n);
    }
    
    private void calculateN() {
        n = p.mul(q);
    }
    
    private void calculatePhiN() {
        phiN = p.sub(BigInt.ONE).mul(q.sub(BigInt.ONE));
        // TODO assert that gcd(e, phiN) == 1
    }
    
    private BigInt getRandomPrime() {
        BigInt a;
        a = Generator.getRandomOdd(size);
        while(!eulerTester.isPrime(a, rounds, false)) {
            a = a.add(BigInt.TWO);
        }
        return a;
    }

    private void calculateD() {
        d = e.egcd(phiN).u;
        // Oh my god....
        // http://crypto.stackexchange.com/questions/10805/how-does-one-deal-with-a-negative-d-in-rsa
        if(d.lt(BigInt.ZERO)) {
            d = d.add(phiN);
        }
    }
}
