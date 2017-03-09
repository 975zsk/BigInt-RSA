package bigint;

/**
 *
 * @author Jakob Pupke
 */
public class GcdLinComb<T extends BigInt> {
    T gcd;
    public T u;
    T v;
    
    GcdLinComb(T gcd, T u, T v) {
        this.gcd = gcd;
        this.u = u;
        this.v = v;
    }
}
