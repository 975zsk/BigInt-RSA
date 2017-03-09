package bigint;

/**
 * 
 * @author Jakob Pupke
 * Base 10 implementation.
 */
public class BigIntDec {
    
    int[] digits;
    /*
    true is positive (default)
    false is negative
    */
    boolean sign = true;
    BigIntOperations operations = new BigIntOperations();
    
    public static final int BASE = 10;
    public static BigIntDec ZERO = new BigIntDec();
    public static BigIntDec ONE = new BigIntDec(1);
    public static BigIntDec TWO = new BigIntDec(2);
    
    private static BigIntDec INTEGER_MAX_VAL = new BigIntDec(Integer.MAX_VALUE);
    
    public BigIntDec() {
        digits = new int[1];
        digits[0] = 0;
    }
    
    public BigIntDec(int integer) {
        String[] stringDigits = Integer.toString(integer).split("");
        digits = new int[stringDigits.length];
        for(int i = 0; i < digits.length; i++) {
            digits[i] = Integer.parseInt(stringDigits[i]);
        }
        sign = integer >= 0;
    }
    
    public BigIntDec(BigInt32 int32) {
        BigIntDec val = new BigIntDec(0);
        int i, j;
        j = 0;
        for(i = int32.digits.length - 1; i >= 0; i--) {
            BigIntDec factor = BigInt32.BASE.pow(j);
            val = val.add(new BigIntDec(int32.digits[i]).mul(factor));
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
    
    public BigIntDec(long integer) {
        String[] stringDigits = Long.toString(integer).split("");
        digits = new int[stringDigits.length];
        for(int i = 0; i < digits.length; i++) {
            digits[i] = Integer.parseInt(stringDigits[i]);
        }
        sign = integer >= 0;
    }
    
    public BigIntDec(int[] digits) {
        this.digits = digits;
    }
   
    // Return a copy of that
    public BigIntDec(BigIntDec that) {
        this.digits = that.digits;
        this.sign = that.sign;
    }
    
    public BigIntDec(String num) {
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
    
    public BigIntDec shiftLeftBy(int f) {
        int newSize = digits.length + f;
        int[] newDigits = new int[newSize];
        System.arraycopy(digits, 0, newDigits, 0, digits.length);
        digits = newDigits;
        return this;
    }
    
    // Removes leading zeros
    public BigIntDec resize() {
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
    
    public void extendWithZeros(int targetSize) {
        if (targetSize <= digits.length) {
            return;
        }
        int[] newDigits = new int[targetSize];
        int idx = newDigits.length - digits.length;
        System.arraycopy(digits, 0, newDigits, idx, digits.length);
        digits = newDigits;
    }
    
    public void initializeWithSize(int size) {
        digits = new int[size];
    }
    
    @Override
    public String toString() {
        String s = "";
        if (isNeg()) s = s + "-";
        
        for(int i = 0; i < digits.length; i++) {
            s = s + digits[i];
        }
        
        return s;
    }
    
    public boolean isZero() {
        return digits.length == 1 && digits[0] == 0;
    }
    
    public boolean equals(BigIntDec that) {
        return operations.equals(this, that);
    }
    
    public BigIntDec pow(int e) {
        return operations.pow(this, e);
    }
    
    public BigIntDec powModPrim(int e, BigIntDec p) throws Exception {
        return operations.powModPrim(this, e, p);
    }
    
    public BigIntDec mod(BigIntDec m) {
        return operations.mod(this, m);
    }
    
    public BigIntDec powMod(int e, BigIntDec m) {
        return operations.powMod(this, e, m);
    }
    
    public BigIntDec powMod(BigIntDec e, BigIntDec m) {
        return operations.powMod(this, e, m);
    }
    
    public BigIntDec karatsuba(BigIntDec that) {
        return operations.karatsuba(this, that);
    }
    
    public BigIntDec add(BigIntDec that) {
        return operations.add(this, that);
    }
    
    public BigIntDec sub(BigIntDec that) {
        return operations.sub(this, that);
    }
    
    public BigIntDec mul(BigIntDec that) {
        return operations.mul(this, that);
    }
    
    public DivisionResult div(BigIntDec that) {
        return operations.div(this, that);
    }
    
    public BigIntDec gcd(BigIntDec that) {
        return operations.gcd(this, that);
    }
    
    public GcdLinComb egcd(BigIntDec that) {
        return operations.egcd(this, that);
    }
    
    public boolean isEven() {
        return (digits[digits.length - 1] & 1) == 0;
    }
    
    public boolean gt(BigIntDec that) {
        return operations.gt(this, that);
    }
    
    public boolean lt(BigIntDec that) {
        return operations.lt(this, that);
    }
    
    public boolean gte(BigIntDec that) {
        return operations.gte(this, that);
    }
    
    public boolean lte(BigIntDec that) {
        return operations.lte(this, that);
    }
    
    public boolean isNeg() {
        return !sign;
    }

    boolean isPos() {
        return sign;
    }
    
    /*
     * Returns a new BigIntDec that is negated
     */
    public BigIntDec neg() {
        return new BigIntDec(this).setSign(!sign);
    }
    
    public BigIntDec setSign(boolean sign) {
        this.sign = sign;
        return this;
    }
    
    /*
    Returns a new positive BigIntDec.
    */
    public BigIntDec abs() {
        return new BigIntDec(this).setSign(true);
    }
    
    public int toInt() throws Exception {
        if(gt(INTEGER_MAX_VAL)) {
            throw new Exception("Cannot convert to Integer. Value is too high.");
        }
        return Integer.parseInt(toString());
    }
}
