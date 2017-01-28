package prime;

import bigint.BigInt;

/**
 *
 * @author Jakob Pupke
 */
public class PrimeTesterFermat extends PrimeTester {

    public PrimeTesterFermat(BigInt n) {
        super(n);
        exponent = nMinusOne;
    }

    @Override
    protected boolean condition(BigInt result) {
        return !result.equals(BigInt.ONE);
    }
    
    public static class Factory implements TesterFactory<PrimeTesterFermat> {
        public PrimeTesterFermat build(BigInt number) {
            return new PrimeTesterFermat(number);
        }
    }

}
