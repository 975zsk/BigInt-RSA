package prime;

import bigint.BigIntDec;

/**
 *
 * @author Jakob Pupke
 */
public class PrimeTesterMillerRabin extends PrimeTester {
    
    BigIntDec s;

    public PrimeTesterMillerRabin(BigIntDec n) {
        super(n);
        // calculate d and s
        BigIntDec d = new BigIntDec(nMinusOne);
        s = BigIntDec.ZERO;
        while(d.isEven()) {
            d = d.div(BigIntDec.TWO).getQuotient();
            s = s.add(BigIntDec.ONE);
        }
        exponent = d;
    }

    @Override
    protected boolean condition(BigIntDec result) {
        if(result.equals(BigIntDec.ONE))
            return false;
        while(s.gt(BigIntDec.ZERO)) {
            if(result.equals(nMinusOne))
                return false;
            result = result.mul(BigIntDec.TWO).mod(n);
            if(result.equals(BigIntDec.ONE))
                return true;
            s = s.sub(BigIntDec.ONE);
        }
        return true;
    }
    
    public static class Factory implements TesterFactory<PrimeTesterMillerRabin> {
        public PrimeTesterMillerRabin build(BigIntDec number) {
            return new PrimeTesterMillerRabin(number);
        }
    }
    
}
