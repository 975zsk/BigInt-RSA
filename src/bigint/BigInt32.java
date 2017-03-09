package bigint;

/**
 *
 * @author Jakob Pupke
 * Base 2^32 implementation. Its not really 2^32 because in Java there are
 * no unsigned numbers. Integer.MAX_VALUE is 2^31 - 1.
 */
public class BigInt32 extends BigInt {
    // This is only used for converting a string of digits to BigInt32
    static final BigIntDec BASE = new BigIntDec(Integer.MAX_VALUE);
    private static BigIntOperations<BigInt32> operations = new BigIntOperations<>(new BigInt32.Factory());

    public static class Factory implements BigIntFactory<BigInt32> {

        static BigInt32 ONE = new BigInt32(1);

        @Override
        public BigInt32 build() {
            return new BigInt32();
        }

        @Override
        public BigInt32 build(BigInt32 that) {
            return new BigInt32(that);
        }

        @Override
        public BigInt32 build(int integer) {
            return new BigInt32(integer);
        }

        @Override
        public BigInt32 build(int[] digits) {
            return new BigInt32(digits);
        }

        @Override
        public BigInt32 build(String number) {
            return new BigInt32(number);
        }

        @Override
        public int getBase() {
            return 10;
        }

        @Override
        public BigInt32 getZero() {
            return new BigInt32();
        }

        @Override
        public BigInt32 getOne() {
            return new BigInt32(1);
        }

        @Override
        public BigInt32 getTwo() {
            return new BigInt32(2);
        }
    }
    
    public BigInt32(String number) {
        BigIntDec val = new BigIntDec(number);
        DivisionResult<BigIntDec> divResult;
        BigIntDec rem;
        digits = new int[getRequiredSize(number)];
        int i = digits.length - 1;
        while(val.gt(BigIntDec.Factory.ZERO)) {
            divResult = val.div(BASE);
            rem = divResult.rest;
            val = divResult.quotient;
            digits[i] = Integer.parseInt(rem.toString());
            i--;
        }
    }

    public BigInt32(int[] digits) {
        this.digits = digits;
    }

    public BigInt32() {
        super();
    }

    @Override
    protected BigIntOperations getOps() {
        return operations;
    }

    public BigInt32(int number) {
        digits = new int[1];
        digits[0] = number;
    }

    public BigInt32(BigInt32 that) {
        this.digits = that.digits;
        this.sign = that.sign;
    }
    
    @Override
    public String toString() {
        // convert to a BASE 10 BigIntDec
        BigIntDec base10 = new BigIntDec(this);
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

    @Override
    public BigInt neg() {
        return new BigInt32(this).setSign(!sign);
    }

    @Override
    public BigInt abs() {
        return new BigInt32(this).setSign(true);
    }

    @Override
    public BigInt inc() {
        return add(Factory.ONE);
    }

    @Override
    public BigInt inc(int by) {
        return add(new BigInt32(by));
    }

    @Override
    public BigInt dec() {
        return sub(Factory.ONE);
    }

    @Override
    public BigInt dec(int by) {
        return sub(new BigInt32(by));
    }
}
