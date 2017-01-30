package prime;

import bigint.BigInt;

/**
 *
 * @author Jakob Pupke
 */
public class PrimeTesterEuler extends PrimeTester {

    public PrimeTesterEuler(BigInt n) {
        super(n);
        exponent = nMinusOne.div(BigInt.TWO).getQuotient();
    }

    @Override
    protected boolean condition(BigInt result) {
        return !( result.equals(BigInt.ONE) || nMinusOne.equals(result) );
    }
    
    public static class Factory implements TesterFactory<PrimeTesterEuler> {
        public PrimeTesterEuler build(BigInt number) {
            return new PrimeTesterEuler(number);
        }
    }
}
