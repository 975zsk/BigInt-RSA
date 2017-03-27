package prime;

import bigint.BigInt;
import bigint.BigInt32;
import bigint.BigIntFactory;

/**
 *
 * @author Jakob Pupke
 */
public class PrimeTesterMillerRabin extends PrimeTester {

    BigInt32 s;

    public PrimeTesterMillerRabin(BigInt32 n) {
        super(n);
        // calculate d and s
        BigInt32 d = new BigInt32(nMinusOne); // copy
        s = BigInt32.Factory.ZERO;
        while(d.isEven()) {
            d = (BigInt32) d.div(BigInt32.Factory.TWO).getQuotient();
            s = (BigInt32) s.add(BigInt32.Factory.ONE);
        }
        exponent = d;
    }

    @Override
    protected boolean condition(BigInt32 result) {
        if(result.equals(BigInt32.Factory.ONE))
            return false;
        while(s.isPos()) {
            if(result.equals(nMinusOne))
                return false;
            result = (BigInt32) result.mul(BigInt32.Factory.TWO).mod(n);
            if(result.equals(BigInt32.Factory.ONE))
                return true;
            s = (BigInt32) s.dec();
        }
        return true;
    }
    
    public static class Factory implements TesterFactory<PrimeTesterMillerRabin> {
        public PrimeTesterMillerRabin build(BigInt32 number) {
            return new PrimeTesterMillerRabin(number);
        }
    }
    
}
