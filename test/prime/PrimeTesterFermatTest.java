package prime;

import bigint.BigInt32;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertTrue;

/**
 *
 * @author Jakob Pupke
 */
public class PrimeTesterFermatTest extends PrimeTesterTest<PrimeTesterFermat> {

    public PrimeTesterFermatTest() {
        super(new PrimeTesterFermat.Factory());
    }
    
    @Test
    public void testIsPrime() throws ExecutionException {
        runIsPrimeTest();
    }
    
    @Test
    public void pseudo() {
        // These are not Prime Numbers, but Fermat fails for these bases (a)
        BigInt32 prime = new BigInt32(117);
        assertTrue(tester.isPrime(prime, new int[] {8, 44, 53, 64, 73, 109}));
        
        prime = new BigInt32(123);
        assertTrue(tester.isPrime(prime, new int[] {40, 83}));
        
        prime = new BigInt32(183);
        assertTrue(tester.isPrime(prime, new int[] {62, 121}));
        
        prime = new BigInt32(203);
        assertTrue(tester.isPrime(prime, new int[] {57, 146}));
        
        prime = new BigInt32(244);
        assertTrue(tester.isPrime(prime, new int[] {13, 169}));
        
        prime = new BigInt32(2735);
        assertTrue(tester.isPrime(prime, new int[] {546, 2189}));
        
        prime = new BigInt32(4984);
        assertTrue(tester.isPrime(prime, new int[] {
            121, 345, 401, 449, 625, 809, 865, 1313, 1425, 1521,
            1577, 1873, 2025, 2137, 2241, 2353, 2585, 2969, 3193,
            3249, 3473, 3665, 3777, 4369, 4377, 4393, 4425, 4489,
            4617, 4673, 4873, 4897
        }));
        
        prime = new BigInt32(4991);
        assertTrue(tester.isPrime(prime, new int[] {
            139, 461, 622, 643, 666, 783, 804, 965, 988, 1149,
            1287, 1310, 1427, 1611, 1954, 2092, 2255, 2414, 2416,
            2575, 2577, 2736, 2899, 3037, 3380, 3564, 3681, 3704,
            3842, 4003, 4026, 4187, 4208, 4325, 4348, 4369, 4530,
            4852
        }));
        
        prime = new BigInt32(4997);
        assertTrue(tester.isPrime(prime, new int[] {1842, 3155}));
        
        prime = new BigInt32(2737);
        assertTrue(tester.isPrime(prime, new int[] {
            22, 24, 45, 47, 93, 114, 116, 137, 139, 160, 162, 183,
            185, 206, 208, 229, 254, 275, 277, 298, 300, 321, 344,
            346, 367, 369, 390, 415, 436, 438, 461, 482, 484, 505,
            507, 528, 530, 551, 576, 597, 599, 620, 622, 643, 645,
            666, 668, 689, 691, 712, 737, 758, 760, 781, 783, 804,
            806, 827, 829, 852, 873, 898, 919, 921, 942, 944, 965,
            967, 988, 990, 1011, 1013, 1034, 1059, 1080, 1082, 1103,
            1126, 1128, 1149, 1151, 1172, 1174, 1195, 1220, 1243, 1264,
            1266, 1287, 1289, 1310, 1312, 1333, 1335, 1356, 1381, 1402,
            1404, 1425, 1427, 1448, 1450, 1471, 1473, 1494, 1517, 1542,
            1563, 1565, 1586, 1588, 1609, 1611, 1634, 1655, 1657, 1678,
            1703, 1724, 1726, 1747, 1749, 1770, 1772, 1793, 1795, 1816,
            1818, 1839, 1864, 1885, 1908, 1910, 1931, 1933, 1954, 1956,
            1977, 1979, 2000, 2025, 2046, 2048, 2069, 2071, 2092, 2094,
            2115, 2117, 2138, 2140, 2161, 2186, 2207, 2209, 2230, 2232,
            2253, 2255, 2276, 2299, 2301, 2322, 2347, 2368, 2370, 2391,
            2393, 2416, 2437, 2439, 2460, 2462, 2483, 2508, 2529, 2531,
            2552, 2554, 2575, 2577, 2598, 2600, 2621, 2623, 2644, 2690,
            2692, 2713, 2715
        }));
    }
}
