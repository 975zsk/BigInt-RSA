package bigint;

/**
 *
 * @author Jakob Pupke
 */
public class DivisionResult {
    BigIntDec quotient;
    BigIntDec rest;
    
    public DivisionResult() {
        this.quotient = new BigIntDec();
        this.rest = new BigIntDec();
    }
    
    public DivisionResult(BigIntDec quotient, BigIntDec rest) {
        this.quotient = quotient;
        this.rest = rest;
    }

    DivisionResult neg() {
        quotient.sign = !quotient.sign;
        return this;
    }
    
    public BigIntDec getQuotient() {
        return quotient;
    }
}
