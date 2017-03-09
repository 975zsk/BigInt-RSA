package prime;

import bigint.BigInt;
import bigint.BigIntFactory;

/**
 *
 * @author Jakob Pupke
 */
public class PrimeTesterMillerRabin<T extends BigInt> extends PrimeTester<T> {
    
    T s;

    public PrimeTesterMillerRabin(T n, BigIntFactory<T> fact) {
        super(n, fact);
        // calculate d and s
        T d = fact.build(nMinusOne);
        s = fact.getZero();
        while(d.isEven()) {
            d = (T) d.div(fact.getTwo()).getQuotient();
            s = (T) s.add(fact.getOne());
        }
        exponent = d;
    }

    @Override
    protected boolean condition(T result) {
        if(result.equals(factory.getOne()))
            return false;
        while(s.isPos()) {
            if(result.equals(nMinusOne))
                return false;
            result = (T) result.mul(factory.getTwo()).mod(n);
            if(result.equals(factory.getOne()))
                return true;
            s = (T) s.dec();
        }
        return true;
    }
    
    public static class Factory<E extends BigInt> implements TesterFactory<PrimeTesterMillerRabin, E> {
        public PrimeTesterMillerRabin build(E number, BigIntFactory<E> fact) {
            return new PrimeTesterMillerRabin(number, fact);
        }
    }
    
}
