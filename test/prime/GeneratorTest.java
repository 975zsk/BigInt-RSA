package prime;

import bigint.BigInt32;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;

/**
 *
 * @author Jakob Pupke
 */
public class GeneratorTest {
    
    private final int ROUNDS = 1000;
    private final int MIN_SIZE = 1000;
    private final int MAX_SIZE = 2000;
    private Generator generator = new Generator();
    
    private int getRandSize() {
        return ThreadLocalRandom.current().nextInt(MIN_SIZE, MAX_SIZE);
    }
    
    @Test
    public void testGet() {
        int size = getRandSize();
        int i;
        BigInt32 rand;
        for(i = 0; i < ROUNDS; i++) {
            rand = generator.getRandom(size);
            assertNotEquals(rand.getFirstDigit(), 0);
            assertEquals(rand.getSize(), size);
            size = getRandSize();
        }
    }
    
    @Test
    public void testGetOdd() {
        int size = getRandSize();
        int i;
        BigInt32 rand;
        for(i = 0; i < ROUNDS; i++) {
            rand = generator.getRandomOdd(size);
            assertFalse(rand.isEven());
            size = getRandSize();
        }
    }
    
    @Test
    public void testSizeOne() {
        int i;
        BigInt32 rand;
        for(i = 0; i < ROUNDS; i++) {
            rand = generator.getRandom(1);
            assertTrue(rand.lt(new BigInt32(BigInt32.BASE)) && rand.gte(BigInt32.Factory.ZERO));
        }
    }
}
