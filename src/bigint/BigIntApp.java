package bigint;

/**
 *
 * @author Jakob Pupke
 */
public class BigIntApp {

    public static void main(String[] args) {
        BigInt a = new BigInt(588);
        int e = 2;
        BigInt p = new BigInt(499);
        BigInt s = a.powModPrim(e, p);
    }
    
}
