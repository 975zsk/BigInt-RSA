package bigint;

/**
 *
 * @author Jakob Pupke
 */
public abstract class BigInt {
    int[] digits;
    boolean sign = true;

    public BigInt() {
        digits = new int[1];
        digits[0] = 0;
    }

    BigInt setSign(boolean sign) {
        this.sign = sign;
        return this;
    }

    public int getSize() {
        return digits.length;
    }
    protected abstract BigIntOperations<BigInt> getOps();

    boolean isZero() {
        return digits.length == 1 && digits[0] == 0;
    }

    void initializeWithSize(int size) {
        digits = new int[size];
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

    public boolean equals(BigInt that) {
        return getOps().equals(this, that);
    }

    BigInt pow(int e) {
        return getOps().pow(this, e);
    }

    BigInt powModPrim(int e, BigInt p) throws Exception {
        return getOps().powModPrim(this, e, p);
    }

    public BigInt mod(BigInt m) {
        return getOps().mod(this, m);
    }

    BigInt powMod(int e, BigInt m) {
        return getOps().powMod(this, e, m);
    }

    public BigInt powMod(BigInt e, BigInt m) {
        return getOps().powMod(this, e, m);
    }

    BigInt karatsuba(BigInt that) {
        return getOps().karatsuba(this, that);
    }

    public BigInt add(BigInt that) {
        return getOps().add(this, that);
    }

    public BigInt sub(BigInt that) {
        return getOps().sub(this, that);
    }

    public BigInt mul(BigInt that) {
        return getOps().mul(this, that);
    }

    public DivisionResult div(BigInt that) {
        return getOps().div(this, that);
    }

    BigInt gcd(BigInt that) {
        return getOps().gcd(this, that);
    }

    public GcdLinComb egcd(BigInt that) {
        return getOps().egcd(this, that);
    }

    public boolean isEven() {
        return (digits[digits.length - 1] & 1) == 0;
    }

    public boolean gt(BigInt that) {
        return getOps().gt(this, that);
    }

    public boolean lt(BigInt that) {
        return getOps().lt(this, that);
    }

    public boolean gte(BigInt that) {
        return getOps().gte(this, that);
    }

    public boolean lte(BigInt that) {
        return getOps().lte(this, that);
    }

    int toInt() throws Exception {
        if(gt(new BigIntDec(Integer.MAX_VALUE))) {
            throw new Exception("Cannot convert to Integer. Value is too high.");
        }
        return Integer.parseInt(toString());
    }

    BigInt shiftLeftBy(int f) {
        int newSize = digits.length + f;
        int[] newDigits = new int[newSize];
        System.arraycopy(digits, 0, newDigits, 0, digits.length);
        digits = newDigits;
        return this;
    }

    public boolean isNeg() {
        return !sign;
    }

    public boolean isPos() {
        return sign;
    }

    public abstract BigInt neg();
    public abstract BigInt abs();

    public abstract BigInt inc();
    public abstract BigInt inc(int by);
    public abstract BigInt dec();
    public abstract BigInt dec(int by);
}
