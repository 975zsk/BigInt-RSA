package bigint;

import java.lang.reflect.Array;

/**
 *
 * @author Jakob Pupke
 */
class Helper<T extends BigInt> {

    private BigIntFactory<T> factory;

    public Helper(BigIntFactory<T> fact) {
        factory = fact;
    }
    
    /*
     * Calculates the quotient by dividing
     * only the first couple of digits
     * Note: divisor.length + 1 >= dividend.length >= divisor.length
     */
    public int guess(T dividend, T divisor) {
        int intDividend;
        int intDivisor = divisor.digits[0];
        if (dividend.digits.length == divisor.digits.length) {
            intDividend = dividend.digits[0];
        }
        else {
            intDividend = dividend.digits[0] * factory.getBase() + dividend.digits[1];
        }
        int res = intDividend / intDivisor;
        T e = factory.build(res);
        while(e.mul(divisor).gt(dividend)) {
            res--;
            e = factory.build(res);
        }
        return res;
    } 
    
    /*
     * Returns the four parts needed for Karatsuba
     */
    public T[] getParts(T x, T y) {
        int size = x.digits.length;
        int halfL = size / 2;
        int halfR = size - halfL;
        
        int[] xH = new int[halfL];
        int[] xL = new int[halfR];
        int[] yH = new int[halfL];
        int[] yL = new int[halfR];
        
        int k = halfR - 1;
        int l = halfL - 1;
        for(int i = size - 1; i >= 0; i--) {
            if (i >= halfL) {
                xL[k] = x.digits[i];
                yL[k] = y.digits[i];
                k--;
            }
            else {
                xH[l] = x.digits[i];
                yH[l] = y.digits[i];
                l--;
            }
        }
        
        T[] res = (T[]) Array.newInstance(x.getClass(), 4);

        res[0] = factory.build(xH);
        res[1] = factory.build(xL);
        res[2] = factory.build(yH);
        res[3] = factory.build(yL);
        
        return res;
    }
    
    public T reduceByAddition(T[] bigIntDecs) {
        if (bigIntDecs.length == 0) {
            // This shouldn't ever be the case, but who knows, 
            // lets be super cautious
            return factory.getZero();
        }
        T x = bigIntDecs[0];
        for(int i = 1; i < bigIntDecs.length; i++) {
            x = (T) x.add(bigIntDecs[i]);
        }
        return x;
    }
    
    public void exchange(T x, T y) {
        int[] temp = y.digits;
        boolean tempSign = y.sign;
        y.digits = x.digits;
        y.sign = x.sign;
        x.digits = temp;
        x.sign = tempSign;
    }
}
