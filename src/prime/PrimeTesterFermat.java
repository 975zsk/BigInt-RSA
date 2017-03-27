package prime;

import bigint.BigInt32;

/**
 *
 * @author Jakob Pupke
 */
public class PrimeTesterFermat extends PrimeTester {

    PrimeTesterFermat(BigInt32 n) {
        super(n);
        exponent = nMinusOne;
    }

    @Override
    protected boolean condition(BigInt32 result) {
        return !result.equals(BigInt32.Factory.ONE);
    }
    
    public static class Factory implements TesterFactory<PrimeTesterFermat> {
        public PrimeTesterFermat build(BigInt32 number) {
            return new PrimeTesterFermat(number);
        }
    }

}
