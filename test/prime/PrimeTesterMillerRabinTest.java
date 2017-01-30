package prime;

import bigint.BigInt;
import java.util.concurrent.ExecutionException;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Jakob Pupke
 */
public class PrimeTesterMillerRabinTest extends PrimeTesterTest<PrimeTesterMillerRabin> {

    public PrimeTesterMillerRabinTest() {
        super(new PrimeTesterMillerRabin.Factory());
    }
    
    @Test
    public void testIsPrime() throws ExecutionException {
        runIsPrimeTest();
    }
    
    @Test
    public void pseudo() {
        // Absolute Euler Pseudo Primes. Miller-Rabin correctly proves that they are not primes ;)
        
        BigInt pseudo = new BigInt(1729);
        assertFalse(tester.isPrime(pseudo, ROUNDS));
        
        pseudo = new BigInt(2465);
        assertFalse(tester.isPrime(pseudo, ROUNDS));
        
        // Miller Rabin fails here
        pseudo = new BigInt(2047);
        assertTrue(tester.isPrime(pseudo, new int[] {
            2
        }));
        
        pseudo = new BigInt(25351);
        assertTrue(tester.isPrime(pseudo, new int[] {
            5
        }));
        
        pseudo = new BigInt(1111);
        assertTrue(tester.isPrime(pseudo, new int[] {
            6
        }));
    }
}
