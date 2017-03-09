package bigint;

/**
 *
 * @author Jakob Pupke
 */
public class DivisionResult {
    BigInt quotient;
    BigInt rest;
    
    DivisionResult(BigInt quotient, BigInt rest) {
        this.quotient = quotient;
        this.rest = rest;
    }

    DivisionResult neg() {
        quotient.sign = !quotient.sign;
        return this;
    }
    
    public BigInt getQuotient() {
        return quotient;
    }
}
