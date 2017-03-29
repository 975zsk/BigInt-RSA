package bigint;

/**
 *
 * @author Jakob Pupke
 * Base 2^32 implementation. Its not really 2^32 because in Java there are
 * no unsigned numbers. Integer.MAX_VALUE is 2^31 - 1 which is an odd number
 * so we have to substract 1 --> Integer.MAX_VALUE - 1 is the base
 */
public class BigInt32 extends BigInt {
    public static final int BASE = Integer.MAX_VALUE - 1;
    // This is only used for converting a string of digits to BigInt32
    static final BigIntDec XBASE = new BigIntDec(BASE);
    private static BigIntOperations<BigInt32> operations = new BigIntOperations<>(new BigInt32.Factory());

    public static class Factory implements BigIntFactory<BigInt32> {

        public static BigInt32 ONE = new BigInt32(1);
        public static BigInt32 TWO = new BigInt32(2);
        public static BigInt32 ZERO = new BigInt32();

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
        public BigInt32 build(long i) {
            return new BigInt32(i);
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
            return BigInt32.BASE;
        }

        @Override
        public int getGcdMaxLengthDiff() {
            return 1;
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
        int length = number.length();
        if (length >= 1 && number.charAt(0) == '-') {
            sign = false;
            number = number.substring(1);
        }
        else if (length == 0) {
            number = "0";
        }
        BigIntDec val = new BigIntDec(number);
        DivisionResult divResult;
        BigIntDec rem;
        digits = new int[getRequiredSize(number)];
        int i = digits.length - 1;
        while(val.gt(BigIntDec.Factory.ZERO)) {
            divResult = val.div(XBASE);
            rem = (BigIntDec) divResult.rest;
            val = (BigIntDec) divResult.quotient;
            digits[i] = Integer.parseInt(rem.toString());
            i--;
        }
    }

    public BigInt32(int number) {
        this((long) number);
    }


    public BigInt32(long number) {
        sign = number >= 0;
        long rest;
        digits = new int[getRequiredSize(Long.toString(number))];
        int i = digits.length - 1;
        while(number > 0) {
            rest = number % BigInt32.BASE;
            number = number / BigInt32.BASE;
            digits[i] = (int) rest;
            i--;
        }
    }

    public BigInt32(int[] digits) {
        super(digits);
    }

    public BigInt32() {
        super();
    }

    @Override
    protected BigIntOperations getOps() {
        return operations;
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
        return (int) Math.ceil((Math.log(10) / Math.log(BigInt32.BASE)) * length);
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
    public BigInt inc(int by) {
        return add(new BigInt32(by));
    }

    @Override
    public BigInt dec() {
        return sub(Factory.ONE);
    }
}
