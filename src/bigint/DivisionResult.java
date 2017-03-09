package bigint;

/**
 *
 * @author Jakob Pupke
 */
public class DivisionResult<T extends BigInt> {
    T quotient;
    T rest;
    
    DivisionResult(T quotient, T rest) {
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
