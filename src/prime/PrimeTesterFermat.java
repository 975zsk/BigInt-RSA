package prime;

import bigint.BigIntDec;

/**
 *
 * @author Jakob Pupke
 */
public class PrimeTesterFermat extends PrimeTester {

    public PrimeTesterFermat(BigIntDec n) {
        super(n);
        exponent = nMinusOne;
    }

    @Override
    protected boolean condition(BigIntDec result) {
        return !result.equals(BigIntDec.ONE);
    }
    
    public static class Factory implements TesterFactory<PrimeTesterFermat> {
        public PrimeTesterFermat build(BigIntDec number) {
            return new PrimeTesterFermat(number);
        }
    }

}
