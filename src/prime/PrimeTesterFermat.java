package prime;

import bigint.BigInt;

import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Jakob Pupke
 */
public class PrimeTesterFermat extends PrimeTester {

    @Override
    protected BigInt getExponent(BigInt number) {
        return nMinusOne;
    }

    @Override
    protected boolean condition(BigInt result) {
        return !result.equals(BigInt.ONE);
    }

}