package bigint;

import static bigint.BigInt.BASE;
import static bigint.BigInt.ZERO;

/**
 *
 * @author Jakob Pupke
 */
class Helper {
    
    /*
     * Calculates the quotient by dividing
     * only the first couple of digits
     * Note: divisor.length + 1 >= dividend.length >= divisor.length
     */
    static int guess(BigInt dividend, BigInt divisor) {
        int intDividend;
        int intDivisor = divisor.digits[0];
        if (dividend.digits.length == divisor.digits.length) {
            intDividend = dividend.digits[0];
        }
        else {
            intDividend = dividend.digits[0] * BASE + dividend.digits[1];
        }
        int res = intDividend / intDivisor;
        BigInt e = new BigInt(res);
        while(e.mul(divisor).gt(dividend)) {
            res--;
            e = new BigInt(res);
        }
        return res;
    } 
    
    /*
     * Returns the four parts needed for Karatsuba
     */
    static BigInt[] getParts(BigInt x, BigInt y) {
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
        
        BigInt[] res = new BigInt[4];
        
        res[0] = new BigInt(xH);
        res[1] = new BigInt(xL);
        res[2] = new BigInt(yH);
        res[3] = new BigInt(yL);
        
        return res;
    }
    
    static BigInt reduceByAddition(BigInt[] bigInts) {
        if (bigInts.length == 0) {
            // This shouldn't ever be the case, but who knows, 
            // lets be super cautious
            return ZERO;
        }
        BigInt x = bigInts[0];
        for(int i = 1; i < bigInts.length; i++) {
            x = x.add(bigInts[i]);
        }
        return x;
    }
    
    static void exchange(BigInt x, BigInt y) {
        int[] temp = y.digits;
        boolean tempSign = y.sign;
        y.digits = x.digits;
        y.sign = x.sign;
        x.digits = temp;
        x.sign = tempSign;
    }
}
