package bigint;

/**
 *
 * @author Jakob Pupke
 */
public class GcdLinComb<T extends BigInt> {
    public T gcd;
    public T u;
    public T v;
    
    public GcdLinComb(T gcd, T u, T v) {
        this.gcd = gcd;
        this.u = u;
        this.v = v;
    }
}
