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
        String leadingZeros = "0000000000000000000000000000000000000000";
        BigInt instance = new BigInt(num);
        
        String expResult = leadingZeros + num;
        
        String result = instance.toString();
        assertEquals(expResult, result);
        
        instance = new BigInt();
        assertEquals("00", instance.toString());
        assertEquals("0", instance.toStringWithoutLeadingZeros());
        
    }
    
    @Test
    public void testAdd() {
        BigInt a = new BigInt("3553759357973593");
        BigInt b = new BigInt("903590835045676745");
        String expResult = "907144594403650338";
        BigInt c = a.add(b);
        assertEquals(expResult, c.toStringWithoutLeadingZeros());
        assertEquals(c.spart, expResult.length());
        
        BigInt d = (new BigInt()).add(new BigInt("123"));
        assertEquals("123", d.toStringWithoutLeadingZeros());
    }
    
    @Test
    public void testSub() {
        BigInt a = new BigInt("123");
        BigInt b = new BigInt("103");
        String expResult = "20";
        BigInt c = a.sub(b);
        assertEquals(expResult, c.toStringWithoutLeadingZeros());
        assertEquals(c.spart, expResult.length());
        
        a = new BigInt();
        b = new BigInt();
        c = a.sub(b);
        assertEquals("0", c.toStringWithoutLeadingZeros());
        
        a = new BigInt("474586");
        b = new BigInt("1289");
        expResult = "473297";
        c = a.sub(b);
        assertEquals(expResult, c.toStringWithoutLeadingZeros());
        assertEquals(c.spart, expResult.length());
        
        a = new BigInt("899456");
        b = new BigInt("897988");
        expResult = "1468";
        c = a.sub(b);
        assertEquals(expResult, c.toStringWithoutLeadingZeros());
        assertEquals(c.spart, expResult.length());
    }
    
    @Test
    public void testMul() {
        BigInt a = new BigInt("111");
        BigInt b = new BigInt("5");
        String expResult = "555";
        BigInt c = a.mul(b);
        assertEquals(expResult, c.toStringWithoutLeadingZeros());
        assertEquals(c.spart, expResult.length());
        
        a = new BigInt("434043568");
        b = new BigInt("999");
        expResult = "433609524432";
        c = a.mul(b);
        assertEquals(expResult, c.toStringWithoutLeadingZeros());
        assertEquals(c.spart, expResult.length());
        
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
    
}
