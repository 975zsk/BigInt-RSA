package bigint;

/**
 *
 * @author Jakob Pupke
 */
public class GcdLinComb {
    BigInt gcd;
    public BigInt u;
    BigInt v;
    
    GcdLinComb(BigInt gcd, BigInt u, BigInt v) {
        this.gcd = gcd;
        this.u = u;
        this.v = v;
    }
}
