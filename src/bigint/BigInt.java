package bigint;

/**
 * 
 * @author Jakob Pupke
 */
public class BigInt {
    
    int[] digits;
    /*
    true is positive (default)
    false is negative
    */
    boolean sign = true;
    BigIntOperations operations = new BigIntOperations();
    
    public static final int BASE = 10;
    public static BigInt ZERO = new BigInt();
    
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
    
    public BigInt shiftLeftBy(int f) {
        int newSize = digits.length + f;
        int[] newDigits = new int[newSize];
        System.arraycopy(digits, 0, newDigits, 0, digits.length);
        digits = newDigits;
        return this;
    }
    
    public BigInt resize() {
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
    
    public boolean equals(BigInt that) {
        return operations.equals(this, that);
    }
    
    public BigInt pow(int e) {
        return operations.pow(this, e);
    }
    
    public BigInt powModPrim(int e, BigInt p) {
        return operations.powModPrim(this, e, p);
    }
    
    public BigInt mod(BigInt m) {
        return operations.mod(this, m);
    }
    
    public BigInt powMod(int e, BigInt m) {
        return operations.powMod(this, e, m);
    }
    
    public BigInt karatsuba(BigInt that) {
        return operations.karatsuba(this, that);
    }
    
    public BigInt add(BigInt that) {
        return operations.add(this, that);
    }
    
    public BigInt sub(BigInt that) {
        return operations.sub(this, that);
    }
    
    public BigInt mul(BigInt that) {
        return operations.mul(this, that);
    }
    
    public DivisionResult div(BigInt that) {
        return operations.div(this, that);
    }
    
    public BigInt gcd(BigInt that) {
        return operations.gcd(this, that);
    }
    
    public GcdLinComb egcd(BigInt that) {
        return operations.egcd(this, that);
    }
    
    public boolean isEven() {
        return (digits[digits.length - 1] & 1) == 0;
    }
    
    public boolean gt(BigInt that) {
        return operations.gt(this, that);
    }
    
    public boolean lt(BigInt that) {
        return operations.lt(this, that);
    }
    
    public boolean gte(BigInt that) {
        return operations.gte(this, that);
    }
    
    public boolean lte(BigInt that) {
        return operations.lte(this, that);
    }
    
    public boolean isNeg() {
        return !sign;
    }

    boolean isPos() {
        return sign;
    }
    
    /*
     * Returns a new BigInt that is negated
     */
    public BigInt neg() {
        return new BigInt(this).setSign(!sign);
    }
    
    public BigInt setSign(boolean sign) {
        this.sign = sign;
        return this;
    }
    
    /*
    Returns a new positive BigInt.
    */
    public BigInt abs() {
        return new BigInt(this).setSign(true);
    }
}
