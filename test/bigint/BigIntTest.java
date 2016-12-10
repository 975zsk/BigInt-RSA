/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bigint;

import java.util.Random;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jacke
 */
public class BigIntTest {
    
    public BigIntTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of toString method, of class BigInt.
     */
    @Test
    public void testToString() {
        String num = "9021484789375297489789478923565365828490";
        BigInt instance = new BigInt(num);
        
        String result = instance.toString();
        assertEquals(num, result);
        
    }
    
    @Test
    public void testExtendWithZeros() {
        BigInt x = new BigInt("1234");
        x.extendWithZeros(10);
        assertEquals("0000001234", x.toString());
    }
    
    @Test
    public void testKaratsuba() {
        BigInt x = new BigInt("1234");
        BigInt y = new BigInt("4321");
        BigInt c = x.karatsuba(y);
        assertEquals("5332114", c.toString());
        
        x = new BigInt("88888888");
        y = new BigInt("88888888");
        c = x.karatsuba(y);
        assertEquals("7901234409876544", c.toString());
    }
    
    @Test
    public void testAdd() {
        BigInt a = new BigInt("3553759357973593");
        BigInt b = new BigInt("903590835045676745");
        String expResult = "907144594403650338";
        BigInt c = a.add(b);
        assertEquals(expResult, c.toString());
        
        BigInt d = (new BigInt()).add(new BigInt("123"));
        assertEquals("123", d.toString());
        
        assertEquals("3553759357973594", a.add(new BigInt("1")).toString());
    }
    
    @Test
    public void testSub() {
        BigInt a = new BigInt("123");
        BigInt b = new BigInt("103");
        String expResult = "20";
        BigInt c = a.sub(b);
        assertEquals(expResult, c.toString());
        
        a = new BigInt();
        b = new BigInt();
        c = a.sub(b);
        assertEquals("0", c.toString());
        
        a = new BigInt("474586");
        b = new BigInt("1289");
        expResult = "473297";
        c = a.sub(b);
        assertEquals(expResult, c.toString());
        
        a = new BigInt("899456");
        b = new BigInt("897988");
        expResult = "1468";
        c = a.sub(b);
        assertEquals(expResult, c.toString());
    }
    
    @Test
    public void testMul() {
        BigInt a = new BigInt("111");
        BigInt b = new BigInt("5");
        String expResult = "555";
        BigInt c = a.mul(b);
        assertEquals(expResult, c.toString());
        
        a = new BigInt("434043568");
        b = new BigInt("999");
        expResult = "433609524432";
        c = a.mul(b);
        assertEquals(expResult, c.toString());
        
        a = new BigInt();
        b = new BigInt();
        
        assertEquals(a.mul(b).toString(), "0");
        
        assertEquals("0", a.mul(new BigInt("21313435334564566353")).toString());
        
        a = new BigInt("99");
        b = new BigInt("32");
        assertEquals("3168", a.mul(b).toString());
        
        a = new BigInt("123456");
        b = new BigInt("1000000");
        
        assertEquals("123456000000", a.mul(b).toString());
    }

    @Test
    public void testIsEven() {
        BigInt instance = new BigInt();
        assertTrue(instance.isEven());
        instance = new BigInt("1");
        assertFalse(instance.isEven());
        instance = new BigInt("123154356841");
        assertFalse(instance.isEven());
    }

    @Test
    public void testEquals() {
        BigInt a = new BigInt();
        BigInt b = new BigInt();
        assertTrue(a.equals(b));
        
        a = new BigInt("123");
        b = new BigInt("123");
        
        assertTrue(a.equals(b));
        
        b = new BigInt("12315364123");
        
        assertFalse(a.equals(b));
        
        a = new BigInt("12345");
        b = new BigInt("10");
        assertTrue(a.equals(b.add(new BigInt("12335"))));
        assertFalse(a.equals(b.add(new BigInt("12334"))));
        
        a = new BigInt("90345723904294274093482048907897782615276532482347588650870");
        b = new BigInt("90345723904294274093482048907897782615276532482347588650870");
        
        assertTrue(a.equals(b));
    }
    
    @Test
    public void testGt() {
        BigInt a = new BigInt();
        BigInt b = new BigInt();
        assertFalse(a.gt(b));
        
        a = new BigInt("567");
        b = new BigInt("566");
        assertTrue(a.gt(b));
        
        a = new BigInt("567");
        b = new BigInt("5668");
        assertFalse(a.gt(b));
        
        a = new BigInt("567");
        b = new BigInt("567");
        assertFalse(a.gt(b));
    }
    
    @Test
    public void testLt() {
        BigInt a = new BigInt();
        BigInt b = new BigInt();
        assertFalse(a.lt(b));
        
        a = new BigInt("567");
        b = new BigInt("566");
        assertFalse(a.lt(b));
        
        a = new BigInt("567");
        b = new BigInt("5668");
        assertTrue(a.lt(b));
        
        a = new BigInt("567");
        b = new BigInt("567");
        assertFalse(a.lt(b));
    }
    
    
    @Test
    public void testResize() {
        int[] a = new int[5];
        a[0] = 0;
        a[1] = 0;
        a[2] = 1;
        a[3] = 2;
        a[4] = 3;
        BigInt x = new BigInt(a);
        assertEquals("123", x.resize().toString());
    }
    
    @Test
    public void testShiftLeftBy() {
        BigInt a = new BigInt("567");
        BigInt b = a.shiftLeftBy(4);
        assertEquals("5670000", b.toString());
        BigInt c = new BigInt("567");
        BigInt d = c.power(3);
        assertTrue(b.equals(d));
    }
    
}
