package bigint;

/**
 *
 * @author Jakob Pupke
 */
public class DivisionResult<T extends BigInt> {
    T quotient;
    T rest;
    
    public DivisionResult(BigIntFactory<T> fact) {
        this.quotient = fact.build();
        this.rest = fact.build();
    }
    
    public DivisionResult(T quotient, T rest) {
        this.quotient = quotient;
        this.rest = rest;
    }

    DivisionResult neg() {
        quotient.sign = !quotient.sign;
        return this;
    }
    
    public T getQuotient() {
        return quotient;
    }
}
