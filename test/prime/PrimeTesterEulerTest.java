package prime;

import bigint.BigInt;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Jakob Pupke
 */
public class PrimeTesterEulerTest extends PrimeTesterTest {
    
    public PrimeTesterEulerTest() {
        tester = new PrimeTesterEuler();
    }
    
    @Test
    public void testIsPrime() {
        runIsPrimeTest();
    }
    
    @Test
    public void pseudo() {
        // These are not Prime Numbers, but Fermat fails for these bases (a)
        BigInt prime = new BigInt(2001);
        assertTrue(tester.isPrime(prime, new int[] {70, 436, 505, 668, 737, 829, 898, 1103, 1172, 1264, 1333, 1496, 1565, 1931}));
        
        prime = new BigInt(1981);
        assertTrue(tester.isPrime(prime, new int[] {
                44, 45, 239, 282, 284, 327, 328, 521, 522, 565, 610, 611, 804, 848, 850, 893,
                894, 1087, 1088, 1131, 1133, 1177, 1370, 1371, 1416, 1459, 1460, 1653, 1654, 1697,
                1699, 1742, 1936, 1937
        }));
        
        prime = new BigInt(1961);
        assertTrue(tester.isPrime(prime, new int[] {105, 295, 401, 635, 741, 924, 931, 1030, 1037, 1220, 1326, 1560, 1666, 1856}));
        
        prime = new BigInt(1729);
        assertTrue(tester.isPrime(prime, new int[] { 2, 3, 5, 6, 8, 9, 10, 11 }));
    }
}
