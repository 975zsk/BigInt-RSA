package bigint;

/**
 *
 * @author Jakob Pupke
 * Base 2^32 implementation. Its not really 2^32 because in Java there are
 * no unsigned numbers. Integer.MAX_VALUE is 2^31 - 1.
 */
public class BigInt32 {
    int[] digits;
    boolean sign = true;
    static final BigInt BASE = new BigInt(Integer.MAX_VALUE);
    
    public BigInt32(String number) {
        BigInt val = new BigInt(number);
        DivisionResult divResult;
        BigInt rem;
        digits = new int[getRequiredSize(number)];
        int i = digits.length - 1;
        while(val.gt(BigInt.ZERO)) {
            divResult = val.div(BASE);
            rem = divResult.rest;
            val = divResult.quotient;
            digits[i] = Integer.parseInt(rem.toString());
            i--;
        }
    }
    
    @Override
    public String toString() {
        // convert to a BASE 10 BigInt
        BigInt base10 = new BigInt(this);
        return base10.toString();
    }
    
    /**
      * How many digits does a decimal number need in BASE 2^32
      */
    private int getRequiredSize(String decimalNumber) {
        int length = decimalNumber.length();
        // IMAO haha. This is probably far from correct.. but quite accurate.
        // TODO: Test this for decimal numbers with more than 300 digits
        return length / 10 + String.valueOf(length).length();
    }
}
