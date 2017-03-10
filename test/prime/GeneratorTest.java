package prime;

import bigint.BigInt32;
import bigint.BigIntFactory;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;

/**
 *
 * @author Jakob Pupke
 */
public class GeneratorTest {
    
    static int ROUNDS = 1000;
    static int MIN_SIZE = 1000;
    static int MAX_SIZE = 2000;
    BigIntFactory<BigInt32> factory = new BigInt32.Factory();
    Generator<BigInt32> generator = new Generator(factory);
    
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
            assertTrue(rand.lt(new BigInt32(factory.getBase())) && rand.gte(BigInt32.Factory.ZERO));
        }
    }
}
