package bigint;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BigIntDecTest extends BigIntTest<BigIntDec> {
    BigIntFactory<BigIntDec> factory;
    public BigIntDecTest() {
        super(new BigIntDec.Factory());
        factory = new BigIntDec.Factory();
    }

    @Test
    public void testResize() {
        int[] a = new int[5];
        a[0] = 0;
        a[1] = 0;
        a[2] = 1;
        a[3] = 2;
        a[4] = 3;
        BigInt x = factory.build(a);
        assertEquals("123", x.resize().toString());
    }

    @Test
    public void testExtendWithZeros() {
        BigInt x = factory.build(1234);
        x.extendWithZeros(10);
        assertEquals("0000001234", x.toString());
    }

    @Test
    public void testShiftLeftBy() {
        BigInt a = factory.build(567);
        a.shiftLeftBy(4);
        assertEquals("5670000", a.toString());
    }
}
