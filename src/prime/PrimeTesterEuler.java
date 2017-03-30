package prime;

import bigint.BigInt32;

/**
 *
 * @author Jakob Pupke
 */
public class PrimeTesterEuler extends PrimeTester {

    PrimeTesterEuler(BigInt32 n) {
        super(n);
        exponent = (BigInt32) nMinusOne.div(BigInt32.Factory.TWO).getQuotient(); //(n - 1) / 2
    }

    @Override
    protected boolean condition(BigInt32 result) {
        return !( result.equals(BigInt32.Factory.ONE) || nMinusOne.equals(result) );
    }


    public static class Factory implements TesterFactory<PrimeTesterEuler> {
        public PrimeTesterEuler build(BigInt32 number) {
            return new PrimeTesterEuler(number);
        }
    }
}
