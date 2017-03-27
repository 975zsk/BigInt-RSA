package prime;

import bigint.BigInt;
import bigint.BigIntFactory;

/**
 *
 * @author Jakob Pupke
 */
public class PrimeTesterEuler<T extends BigInt> extends PrimeTester<T> {

    PrimeTesterEuler(T n, BigIntFactory<T> fact) {
        super(n, fact);
        exponent = (T) nMinusOne.div(fact.getTwo()).getQuotient(); //(n -1) / 2
    }

    @Override
    protected boolean condition(T result) {
        return !( result.equals(factory.getOne()) || nMinusOne.equals(result) );
    }


    public static class Factory<E extends BigInt> implements TesterFactory<PrimeTesterEuler, E> {
        public PrimeTesterEuler build(E number, BigIntFactory<E> fact) {
            return new PrimeTesterEuler<>(number, fact);
        }
    }
}
