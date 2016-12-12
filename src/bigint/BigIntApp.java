package bigint;

/**
 *
 * @author Jakob Pupke
 */
public class BigIntApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        BigInt x = new BigInt("10000");
        BigInt y = new BigInt("49");
        BigInt c = x.sub(y);
    }
    
}
