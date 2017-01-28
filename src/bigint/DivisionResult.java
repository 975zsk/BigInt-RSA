package bigint;

/**
 *
 * @author Jakob Pupke
 */
public class DivisionResult {
    BigInt quotient;
    BigInt rest;
    
    public DivisionResult() {
        this.quotient = new BigInt();
        this.rest = new BigInt();
    }
    
    public DivisionResult(BigInt quotient, BigInt rest) {
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
