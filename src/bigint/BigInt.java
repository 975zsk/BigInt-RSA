package bigint;

public class BigInt {
    
    int[] digits;
    BigIntOperations operations = new BigIntOperations();
    
    public static final int BASE = 10;
    
    public BigInt() {
        digits = new int[1];
        digits[0] = 0;
    }
    
    public BigInt(BigInt that) {
        this.digits = that.digits;
    }
    
    public BigInt(String num) {
        int length = num.length();
        if(length == 0) {
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
    
    public BigInt resize() {
        if(isZero()) {
            return this;
        }
        int idx = 0;
        for(int i = 0; i < digits.length; i++) {
            if(digits[i] != 0) {
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
    
    public void initializeWithSize(int size) {
        digits = new int[size];
    }
    
    @Override
    public String toString() {
        String s = "";
        for(int i = 0; i < digits.length; i++) {
            s = s + digits[i];
        }
        return s;
    }
    
    private boolean isZero() {
        return digits.length == 1 && digits[0] == 0;
    }
    
    public boolean equals(BigInt that) {
        return operations.equals(this, that);
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
}
