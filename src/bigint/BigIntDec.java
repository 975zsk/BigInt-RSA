package bigint;

/**
 * 
 * @author Jakob Pupke
 * Base 10 implementation.
 */
public class BigIntDec extends BigInt {

    /*
    true is positive (default)
    false is negative
    */
    private static BigIntOperations<BigIntDec> operations = new BigIntOperations<>(new BigIntDec.Factory());

    public static class Factory implements BigIntFactory<BigIntDec> {

        public static BigIntDec ZERO = new BigIntDec();
        public static BigIntDec ONE = new BigIntDec(1);
        public static BigIntDec TWO = new BigIntDec(2);

        @Override
        public BigIntDec build() {
            return new BigIntDec();
        }

        @Override
        public BigIntDec build(BigIntDec that) {
            return new BigIntDec(that);
        }

        @Override
        public BigIntDec build(int integer) {
            return new BigIntDec(integer);
        }

        @Override
        public BigIntDec build(long i) {
            return new BigIntDec(i);
        }

        @Override
        public BigIntDec build(int[] digits) {
            return new BigIntDec(digits);
        }

        @Override
        public BigIntDec build(String number) {
            return new BigIntDec(number);
        }

        @Override
        public int getBase() {
            return 10;
        }

        @Override
        public BigIntDec getZero() {
            return new BigIntDec();
        }

        @Override
        public BigIntDec getOne() {
            return new BigIntDec(1);
        }

        @Override
        public BigIntDec getTwo() {
            return new BigIntDec(2);
        }

        @Override
        public int getGcdMaxLengthDiff() {
            return 5;
        }
    }

    public BigIntDec() {
        super();
    }

    @Override
    protected BigIntOperations getOps() {
        return operations;
    }

    public BigIntDec(int integer) {
        sign = integer >= 0;
        String[] stringDigits = Integer.toString(Math.abs(integer)).split("");
        digits = new int[stringDigits.length];
        for(int i = 0; i < digits.length; i++) {
            digits[i] = Integer.parseInt(stringDigits[i]);
        }
    }
    
    public BigIntDec(BigInt32 int32) {
        sign = int32.sign;
        BigIntDec val = new BigIntDec(0);
        int i, j;
        j = 0;
        for(i = int32.digits.length - 1; i >= 0; i--) {
            BigIntDec factor = (BigIntDec) BigInt32.XBASE.pow(j);
            val = (BigIntDec) val.add(new BigIntDec(int32.digits[i]).mul(factor));
            j++;
        }
        digits = val.digits;
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
    
    @Override
    public String toString() {
        String s = "";
        if (isNeg()) s = s + "-";

        for (int digit : digits) {
            s = s + digit;
        }
        
        return s;
    }
    
    /*
     * Returns a new BigIntDec that is negated
     */
    @Override
    public BigInt neg() {
        return new BigIntDec(this).setSign(!sign);
    }
    
    /*
    Returns a new positive BigIntDec.
    */
    @Override
    public BigInt abs() {
        return new BigIntDec(this).setSign(true);
    }

    @Override
    public BigInt inc(int by) {
        return add(new BigIntDec(by));
    }

    @Override
    public BigInt dec() {
        return sub(Factory.ONE);
    }

}
