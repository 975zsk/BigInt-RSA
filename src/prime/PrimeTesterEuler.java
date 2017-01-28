package prime;

import bigint.BigInt;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Jakob Pupke
 */
public class PrimeTesterEuler extends PrimeTester {

    @Override
    protected BigInt getExponent(BigInt number) {
        return nMinusOne.div(BigInt.TWO).getQuotient();
    }

    @Override
    protected boolean condition(BigInt result) {
        return !( result.equals(BigInt.ONE) || nMinusOne.equals(result) );
    }

        
}
