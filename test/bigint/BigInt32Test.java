package bigint;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 *
 * @author Jakob Pupke
 */
public class BigInt32Test extends BigIntTest<BigInt32> {

    BigIntFactory<BigInt32> factory;

    public BigInt32Test() {
        super(new BigInt32.Factory());
        factory = new BigInt32.Factory();
    }
    
    @Test
    public void testConstructorAndToString() {
        String[] numbers = {
            "7435357357437595763865783673434844444444444444449538948324893859838954565676",
            "359349059",
            "0",
            "1",
            "2",
            "9999999999999999999999999999999999009607595769999999999999999999999999999",
            "999999999999999999999999999999999900960759576999999999999999999999999999",
            "99999999999999999999999999999999990096075957699999999999999999999999999",
            "9999999999999999999999999999999999009607595769999999999999999999999999",
            "999999999999999999999999999999999900960759576999999999999999999999999",
            "99999999999999999999999999999999990096075957699999999999999999999999",
            "9999999999999999999999999999999999009607595769999999999999999999999",
            "999999999999999999999999999999999900960759576999999999999999999999",
            "99999999999999999999999999999999990096075957699999999999999999999",
            "9999999999999999999999999999999999009607595769999999999999999999999999999"
                + "9999999999999999999999999999999999999999999999999999999999992343234599"
        };
        
        BigInt32 int32;
        String decimalString;
        
        for(String number : numbers) {
            int32 = new BigInt32(number);
            decimalString = int32.toString();
            assertTrue(decimalString.equals(number));
        }
    }

    @Test
    public void testResize() {
        int[] a = new int[5];
        a[0] = 0;
        a[1] = 0;
        a[2] = 0;
        a[3] = 0;
        a[4] = Integer.MAX_VALUE - 50;
        BigInt x = factory.build(a);
        assertEquals(Integer.toString(Integer.MAX_VALUE - 50), x.resize().toString());
    }

    @Test
    public void testExtendWithZeros() {
        BigInt x = factory.build(1234);
        x.extendWithZeros(10);
        assertEquals("[0, 0, 0, 0, 0, 0, 0, 0, 0, 1234]", Arrays.toString(x.digits));
    }

    @Test
    public void testShiftLeftBy() {
        BigInt a = factory.build(233645);
        a.shiftLeftBy(4);
        assertEquals("[233645, 0, 0, 0, 0]", Arrays.toString(a.digits));
    }
}
