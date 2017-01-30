package prime;

import bigint.BigInt;

/**
 *
 * @author Jakob Pupke
 */
public class PrimeTesterMillerRabin extends PrimeTester {
    
    BigInt s;

    public PrimeTesterMillerRabin(BigInt n) {
        super(n);
        // calculate d and s
        BigInt d = new BigInt(nMinusOne);
        s = BigInt.ZERO;
        while(d.isEven()) {
            d = d.div(BigInt.TWO).getQuotient();
            s = s.add(BigInt.ONE);
        }
        exponent = d;
    }

    @Override
    protected boolean condition(BigInt result) {
        if(result.equals(BigInt.ONE))
            return false;
        while(s.gt(BigInt.ZERO)) {
            if(result.equals(nMinusOne))
                return false;
            result = result.mul(BigInt.TWO).mod(n);
            if(result.equals(BigInt.ONE))
                return true;
            s = s.sub(BigInt.ONE);
        }
        return true;
    }
    
    public static class Factory implements TesterFactory<PrimeTesterMillerRabin> {
        public PrimeTesterMillerRabin build(BigInt number) {
            return new PrimeTesterMillerRabin(number);
        }
    }
    
}
