package prime;

import bigint.BigIntDec;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Jakob Pupke
 */
public class PrimeTesterMillerRabinTest extends PrimeTesterTest<PrimeTesterMillerRabin, BigIntDec> {

    public PrimeTesterMillerRabinTest() {
        super(new PrimeTesterMillerRabin.Factory<>(), new BigIntDec.Factory());
    }
    
    @Test
    public void testIsPrime() throws ExecutionException {
        runIsPrimeTest();
    }
    
    @Test
    public void pseudo() {
        // Absolute Euler Pseudo Primes. Miller-Rabin correctly proves that they are not primes ;)
        
        BigIntDec pseudo = new BigIntDec(1729);
        assertFalse(tester.isPrime(pseudo, ROUNDS));
        
        pseudo = new BigIntDec(2465);
        assertFalse(tester.isPrime(pseudo, ROUNDS));
        
        // Miller Rabin fails here
        pseudo = new BigIntDec(2047);
        assertTrue(tester.isPrime(pseudo, new int[] {
            2
        }));
        
        pseudo = new BigIntDec(25351);
        assertTrue(tester.isPrime(pseudo, new int[] {
            5
        }));
        
        pseudo = new BigIntDec(1111);
        assertTrue(tester.isPrime(pseudo, new int[] {
            6
        }));
    }
}
