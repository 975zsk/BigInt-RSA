package prime;

import bigint.BigInt;
import bigint.BigIntFactory;

/**
 *
 * @author Jakob Pupke
 */
public class PrimeTesterFermat<T extends BigInt> extends PrimeTester<T> {

    public PrimeTesterFermat(T n, BigIntFactory<T> fact) {
        super(n, fact);
        exponent = nMinusOne;
    }

    @Override
    protected boolean condition(T result) {
        return !result.equals(factory.getOne());
    }
    
    public static class Factory<E extends BigInt> implements TesterFactory<PrimeTesterFermat, E> {
        public PrimeTesterFermat build(E number, BigIntFactory<E> fact) {
            return new PrimeTesterFermat<>(number, fact);
        }
    }

}
