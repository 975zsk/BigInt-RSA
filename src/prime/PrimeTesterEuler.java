package prime;

import bigint.BigIntDec;

/**
 *
 * @author Jakob Pupke
 */
public class PrimeTesterEuler extends PrimeTester {

    public PrimeTesterEuler(BigIntDec n) {
        super(n);
        exponent = nMinusOne.div(BigIntDec.TWO).getQuotient();
    }

    @Override
    protected boolean condition(BigIntDec result) {
        return !( result.equals(BigIntDec.ONE) || nMinusOne.equals(result) );
    }
    
    public static class Factory implements TesterFactory<PrimeTesterEuler> {
        public PrimeTesterEuler build(BigIntDec number) {
            return new PrimeTesterEuler(number);
        }
    }
}
