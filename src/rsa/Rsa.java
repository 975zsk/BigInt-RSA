package rsa;

import bigint.BigIntDec;
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
    BigIntDec p;
    BigIntDec q;
    BigIntDec n;
    BigIntDec e;
    BigIntDec d;
    BigIntDec phiN;
    
    public Rsa() {}
    
    public Rsa(BigIntDec e, int size) throws InterruptedException, ExecutionException {
        this.e = e;
        this.size = size;
        setRandomPrimes();
        calculateValues();
    }
    
    public Rsa(BigIntDec e) throws InterruptedException, ExecutionException {
        this.e = e;
        setRandomPrimes();
        calculateValues();
    }
    
    public Rsa(BigIntDec p, BigIntDec q, BigIntDec e) {
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
        List<Future<BigIntDec>> list = new ArrayList<>();
        
        for(int i = 0; i <= 1; i++) {
            Callable<BigIntDec> worker = new Callable<BigIntDec>() {
                @Override
                public BigIntDec call() throws Exception {
                    return getRandomPrime();
                }
            };
            Future<BigIntDec> submit = executor.submit(worker);
            list.add(submit);
        }
        
        try {
            Future<BigIntDec> pFuture = list.get(0);
            p = pFuture.get();
            Future<BigIntDec> qFuture = list.get(1);
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
    
    public static Keys generateRSAKeys(BigIntDec e, int size) throws InterruptedException, ExecutionException {
        Rsa rsa = new Rsa(e, size);
        PublicKey publicKey = new PublicKey(rsa.e, rsa.n);
        SecretKey secretKey = new SecretKey(rsa.p, rsa.q, rsa.d, rsa.n);
        return new Keys(secretKey, publicKey);
    };
    
    public static BigIntDec encrypt(PublicKey pk, BigIntDec block) {
        return block.powMod(pk.e, pk.n);
    }
    
    public static BigIntDec decrypt(SecretKey sk, BigIntDec cipher) {
        return cipher.powMod(sk.d, sk.n);
    }
    
    private void calculateN() {
        n = p.mul(q);
    }
    
    private void calculatePhiN() {
        phiN = p.sub(BigIntDec.ONE).mul(q.sub(BigIntDec.ONE));
        // TODO assert that gcd(e, phiN) == 1
    }
    
    private BigIntDec getRandomPrime() {
        BigIntDec a;
        a = Generator.getRandomOdd(size);
        while(!eulerTester.isPrime(a, rounds, false)) {
            a = a.add(BigIntDec.TWO);
        }
        return a;
    }

    private void calculateD() {
        d = e.egcd(phiN).u;
        // Oh my god....
        // http://crypto.stackexchange.com/questions/10805/how-does-one-deal-with-a-negative-d-in-rsa
        if(d.lt(BigIntDec.ZERO)) {
            d = d.add(phiN);
        }
    }
}
