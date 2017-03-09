package bigint;

import static bigint.BigIntDec.BASE;
import static bigint.BigIntDec.ZERO;

/**
 *
 * @author Jakob Pupke
 */
public class Helper {
    
    /*
     * Calculates the quotient by dividing
     * only the first couple of digits
     * Note: divisor.length + 1 >= dividend.length >= divisor.length
     */
    public static int guess(BigIntDec dividend, BigIntDec divisor) {
        int intDividend;
        int intDivisor = divisor.digits[0];
        if (dividend.digits.length == divisor.digits.length) {
            intDividend = dividend.digits[0];
        }
        else {
            intDividend = dividend.digits[0] * BASE + dividend.digits[1];
        }
        int res = intDividend / intDivisor;
        BigIntDec e = new BigIntDec(res);
        while(e.mul(divisor).gt(dividend)) {
            res--;
            e = new BigIntDec(res);
        }
        return res;
    } 
    
    /*
     * Returns the four parts needed for Karatsuba
     */
    public static BigIntDec[] getParts(BigIntDec x, BigIntDec y) {
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
        
        BigIntDec[] res = new BigIntDec[4];
        
        res[0] = new BigIntDec(xH);
        res[1] = new BigIntDec(xL);
        res[2] = new BigIntDec(yH);
        res[3] = new BigIntDec(yL);
        
        return res;
    }
    
    public static BigIntDec reduceByAddition(BigIntDec[] bigIntDecs) {
        if (bigIntDecs.length == 0) {
            // This shouldn't ever be the case, but who knows, 
            // lets be super cautious
            return ZERO;
        }
        BigIntDec x = bigIntDecs[0];
        for(int i = 1; i < bigIntDecs.length; i++) {
            x = x.add(bigIntDecs[i]);
        }
        return x;
    }
    
    public static void exchange(BigIntDec x, BigIntDec y) {
        int[] temp = y.digits;
        boolean tempSign = y.sign;
        y.digits = x.digits;
        y.sign = x.sign;
        x.digits = temp;
        x.sign = tempSign;
    }
}
