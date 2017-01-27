package prime;

import bigint.BigInt;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jakob Pupke
 */
public class GeneratorTest {
    
    static int ROUNDS = 1000;
    static int MIN_SIZE = 1000;
    static int MAX_SIZE = 2000;
    
    public GeneratorTest() {}
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    private int getRandSize() {
        return ThreadLocalRandom.current().nextInt(MIN_SIZE, MAX_SIZE);
    }
    
    @Test
    public void testGet() {
        int size = getRandSize();
        int i;
        BigInt rand;
        for(i = 0; i < ROUNDS; i++) {
            rand = Generator.getRandom(size);
            assertNotEquals(rand.getFirstDigit(), 0);
            assertEquals(rand.getSize(), size);
            size = getRandSize();
        }
    }
    
    @Test
    public void testGetOdd() {
        int size = getRandSize();
        int i;
        BigInt rand;
        for(i = 0; i < ROUNDS; i++) {
            rand = Generator.getRandomOdd(size);
            assertFalse(rand.isEven());
            size = getRandSize();
        }
    }
    
    @Test
    public void testSizeOne() {
        int i;
        BigInt rand;
        for(i = 0; i < ROUNDS; i++) {
            rand = Generator.getRandom(1);
            assertTrue(rand.lt(new BigInt(10)) && rand.gte(BigInt.ZERO));
        }
    }
}
