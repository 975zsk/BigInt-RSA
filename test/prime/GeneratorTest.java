package prime;

import bigint.BigIntDec;
import org.junit.AfterClass;
import org.junit.BeforeClass;
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
    Generator<BigIntDec> generator = new Generator(new BigIntDec.Factory());
    
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
        BigIntDec rand;
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
        BigIntDec rand;
        for(i = 0; i < ROUNDS; i++) {
            rand = generator.getRandomOdd(size);
            assertFalse(rand.isEven());
            size = getRandSize();
        }
    }
    
    @Test
    public void testSizeOne() {
        int i;
        BigIntDec rand;
        for(i = 0; i < ROUNDS; i++) {
            rand = generator.getRandom(1);
            assertTrue(rand.lt(new BigIntDec(10)) && rand.gte(BigIntDec.Factory.ZERO));
        }
    }
}
