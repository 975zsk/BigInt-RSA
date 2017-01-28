package prime;

import bigint.BigInt;
import java.util.concurrent.ExecutionException;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Jakob Pupke
 */
public class PrimeTesterTest {
    
    PrimeTester tester;
    static int ROUNDS = 50;
    
    public void runIsPrimeTest() throws ExecutionException {
        
        // PRIMES
        
        BigInt prime = new BigInt("2409130781894986571956777721649968801511465915451196376269177305066867");
        assertTrue(tester.isPrime(prime, ROUNDS));
        
        prime = new BigInt("9547848065153773335707495885453566120069130270246768806790708393909999");
        assertTrue(tester.isPrime(prime, ROUNDS));
        
        prime = new BigInt(2);
        assertTrue(tester.isPrime(prime, ROUNDS));
        
        prime = new BigInt(5);
        assertTrue(tester.isPrime(prime, ROUNDS));
        
        prime = new BigInt(7);
        assertTrue(tester.isPrime(prime, ROUNDS));
        
        prime = new BigInt(13);
        assertTrue(tester.isPrime(prime, ROUNDS));
        
        // NOT PRIMES
        
        prime = new BigInt("30024369469850064594387327432754385943969493248284238828538521");
        assertFalse(tester.isPrime(prime, ROUNDS));
        
        prime = new BigInt("45065795679459567865734568458668435765995697569421");
        assertFalse(tester.isPrime(prime, ROUNDS));
        
        prime = new BigInt("32498435834865326945234875183518437534758431");
        assertFalse(tester.isPrime(prime, ROUNDS));
        
        prime = new BigInt("21346587698797674564778007867511111");
        assertFalse(tester.isPrime(prime, ROUNDS));
        
        prime = new BigInt("7665795679597579578956967957");
        assertFalse(tester.isPrime(prime, ROUNDS));
        
        prime = new BigInt("457568756845876866786");
        assertFalse(tester.isPrime(prime, ROUNDS));
    }
}
