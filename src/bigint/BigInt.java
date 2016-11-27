package bigint;

public class BigInt {
    
    int[] digits;
    int size;
    int spart;
    boolean withLeadingZeros = true;
    BigIntOperations operations = new BigIntOperations();
    
    public static final int BASE = 10;
    
    public BigInt() {
        digits = new int[2];
        digits[0] = digits[1] = 0;
        size = 2;
        spart = 1;
    }
    
    public BigInt(BigInt that) {
        this.digits = that.digits;
        this.size = that.size;
        this.spart = that.spart;
    }
    
    public BigInt(String num) {
        int length = num.length();
        if(length == 0) {
            num = "0";
            length = 1;
        }
        spart = length;
        size = spart * 2;
        digits = new int[size];
        int j = size - 1;
        int val;
        for(int i = length - 1; i >= 0; i--) {
            val = Integer.parseInt(String.valueOf(num.charAt(i)));
            digits[j] = val;
            j--;
        }
    }
    
    public void initializeWithSize(int size) {
        digits = new int[size];
        this.size = size;
        spart = 0;
    }
    
    public String toStringWithoutLeadingZeros() {
        if(isZero()) {
            return "0";
        }
        this.withLeadingZeros = false;
        return toString();
    }
    
    @Override
    public String toString() {
        String s = "";
        int startIndex = 0;
        if(!withLeadingZeros) {
            startIndex = getIndexOfFirstSignificantDigit();
        }
        for(int i = startIndex; i < digits.length; i++) {
            s = s + digits[i];
        }
        this.withLeadingZeros = true;
        return s;
    }
    
    public void resetSpart() {
        for(int i = 0; i < digits.length; i++) {
            if(digits[i] != 0) {
                this.spart = digits.length - i;
                return;
            }
        }
        this.spart = 0;
    }
   
    
    public int getIndexOfFirstSignificantDigit() {
        int i = size - spart;
        if(i == size) {
            i--;
        }
        return i;
    }
    
    private boolean isZero() {
        for(int i = size - 1; i >= 0; i--) {
            if(digits[i] != 0) {
                return false;
            }
        }
        return true;
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
        return (digits[size - 1] & 1) == 0;
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
