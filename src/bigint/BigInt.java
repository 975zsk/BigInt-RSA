package bigint;

/**
 * 
 * @author Jakob Pupke
 * Base 10 implementation.
 */
public class BigInt {
    
    int[] digits;
    /*
    true is positive (default)
    false is negative
    */
    boolean sign = true;
    
    static final int BASE = 10;
    public static BigInt ZERO = new BigInt();
    public static BigInt ONE = new BigInt(1);
    public static BigInt TWO = new BigInt(2);
    
    private static BigInt INTEGER_MAX_VAL = new BigInt(Integer.MAX_VALUE);
    
    public BigInt() {
        digits = new int[1];
        digits[0] = 0;
    }
    
    public BigInt(int integer) {
        String[] stringDigits = Integer.toString(integer).split("");
        digits = new int[stringDigits.length];
        for(int i = 0; i < digits.length; i++) {
            digits[i] = Integer.parseInt(stringDigits[i]);
        }
        sign = integer >= 0;
    }
    
    public BigInt(BigInt32 int32) {
        BigInt val = new BigInt(0);
        int i, j;
        j = 0;
        for(i = int32.digits.length - 1; i >= 0; i--) {
            BigInt factor = BigInt32.BASE.pow(j);
            val = val.add(new BigInt(int32.digits[i]).mul(factor));
            j++;
        }
        digits = val.digits;
        sign = val.sign;
    }
    
    public int getFirstDigit() {
        return digits[0];
    }
    
    public int getSize() {
        return digits.length;
    }
    
    public BigInt(long integer) {
        String[] stringDigits = Long.toString(integer).split("");
        digits = new int[stringDigits.length];
        for(int i = 0; i < digits.length; i++) {
            digits[i] = Integer.parseInt(stringDigits[i]);
        }
        sign = integer >= 0;
    }
    
    public BigInt(int[] digits) {
        this.digits = digits;
    }
   
    // Return a copy of that
    public BigInt(BigInt that) {
        this.digits = that.digits;
        this.sign = that.sign;
    }
    
    public BigInt(String num) {
        int length = num.length();
        if (length >= 1 && num.charAt(0) == '-') {
            sign = false;
            num = num.substring(1);
            length = length - 1;
        }
        else if (length == 0) {
            num = "0";
            length = 1;
        }
        digits = new int[length];
        int j = length - 1;
        int val;
        for(int i = length - 1; i >= 0; i--) {
            val = Integer.parseInt(String.valueOf(num.charAt(i)));
            digits[j] = val;
            j--;
        }
    }
    
    BigInt shiftLeftBy(int f) {
        int newSize = digits.length + f;
        int[] newDigits = new int[newSize];
        System.arraycopy(digits, 0, newDigits, 0, digits.length);
        digits = newDigits;
        return this;
    }
    
    // Removes leading zeros
    BigInt resize() {
        if (isZero()) {
            return this;
        }
        int idx = 0;
        for(int i = 0; i < digits.length; i++) {
            if (digits[i] != 0) {
                idx = i;
                break;
            }
            idx = i;
        }
        int newLength = digits.length - idx;
        int[] newDigits = new int[newLength];
        System.arraycopy(digits, idx, newDigits, 0, newLength);
        digits = newDigits;
        return this;
    }
    
    void extendWithZeros(int targetSize) {
        if (targetSize <= digits.length) {
            return;
        }
        int[] newDigits = new int[targetSize];
        int idx = newDigits.length - digits.length;
        System.arraycopy(digits, 0, newDigits, idx, digits.length);
        digits = newDigits;
    }
    
    void initializeWithSize(int size) {
        digits = new int[size];
    }
    
    @Override
    public String toString() {
        String s = "";
        if (isNeg()) s = s + "-";

        for (int digit : digits) {
            s = s + digit;
        }
        
        return s;
    }
    
    boolean isZero() {
        return digits.length == 1 && digits[0] == 0;
    }
    
    public boolean equals(BigInt that) {
        return BigIntOperations.equals(this, that);
    }
    
    BigInt pow(int e) {
        return BigIntOperations.pow(this, e);
    }
    
    BigInt powModPrim(int e, BigInt p) throws Exception {
        return BigIntOperations.powModPrim(this, e, p);
    }
    
    public BigInt mod(BigInt m) {
        return BigIntOperations.mod(this, m);
    }
    
    BigInt powMod(int e, BigInt m) {
        return BigIntOperations.powMod(this, e, m);
    }
    
    public BigInt powMod(BigInt e, BigInt m) {
        return BigIntOperations.powMod(this, e, m);
    }
    
    BigInt karatsuba(BigInt that) {
        return BigIntOperations.karatsuba(this, that);
    }
    
    public BigInt add(BigInt that) {
        return BigIntOperations.add(this, that);
    }
    
    public BigInt sub(BigInt that) {
        return BigIntOperations.sub(this, that);
    }
    
    public BigInt mul(BigInt that) {
        return BigIntOperations.mul(this, that);
    }
    
    public DivisionResult div(BigInt that) {
        return BigIntOperations.div(this, that);
    }
    
    BigInt gcd(BigInt that) {
        return BigIntOperations.gcd(this, that);
    }
    
    public GcdLinComb egcd(BigInt that) {
        return BigIntOperations.egcd(this, that);
    }
    
    public boolean isEven() {
        return (digits[digits.length - 1] & 1) == 0;
    }
    
    public boolean gt(BigInt that) {
        return BigIntOperations.gt(this, that);
    }
    
    public boolean lt(BigInt that) {
        return BigIntOperations.lt(this, that);
    }
    
    public boolean gte(BigInt that) {
        return BigIntOperations.gte(this, that);
    }
    
    public boolean lte(BigInt that) {
        return BigIntOperations.lte(this, that);
    }
    
    boolean isNeg() {
        return !sign;
    }

    boolean isPos() {
        return sign;
    }
    
    /*
     * Returns a new BigInt that is negated
     */
    BigInt neg() {
        return new BigInt(this).setSign(!sign);
    }
    
    BigInt setSign(boolean sign) {
        this.sign = sign;
        return this;
    }
    
    /*
    Returns a new positive BigInt.
    */
    BigInt abs() {
        return new BigInt(this).setSign(true);
    }
    
    int toInt() throws Exception {
        if(gt(INTEGER_MAX_VAL)) {
            throw new Exception("Cannot convert to Integer. Value is too high.");
        }
        return Integer.parseInt(toString());
    }
}
