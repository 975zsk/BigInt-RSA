package prime;

import bigint.BigInt;
import java.util.concurrent.ExecutionException;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;

/**
 *
 * @author Jakob Pupke
 * @param <T>
 */
@Ignore("All PrimesTester*Test classes inherit from this class.")
public abstract class PrimeTesterTest<T extends PrimeTester> {
    
    PrimeTestRunner<T> tester;
    static int ROUNDS = 50;
    
    public PrimeTesterTest(TesterFactory<T> fact) {
        this.tester = new PrimeTestRunner<>(fact);
    }
    
    public void runIsPrimeTest() throws ExecutionException {
        
        // PRIMES
        
        BigInt prime = new BigInt("2409130781894986571956777721649968801511465915451196376269177305066867");
        assertTrue(tester.isPrime(prime, ROUNDS));
        
        prime = new BigInt("9547848065153773335707495885453566120069130270246768806790708393909999");
        assertTrue(tester.isPrime(prime, ROUNDS));
        
        prime = new BigInt(2);
        assertTrue(tester.isPrime(prime, ROUNDS));
        
        prime = new BigInt(5);
        assertTrue(tester.isPrime(prime, ROUNDS));
        
        prime = new BigInt(7);
        assertTrue(tester.isPrime(prime, ROUNDS));
        
        prime = new BigInt(13);
        assertTrue(tester.isPrime(prime, ROUNDS));
        
        prime = new BigInt(79);
        assertTrue(tester.isPrime(prime, ROUNDS));
        
        // NOT PRIMES
        
        prime = new BigInt("30024369469850064594387327432754385943969493248284238828538521");
        assertFalse(tester.isPrime(prime, ROUNDS));
        
        prime = new BigInt("45065795679459567865734568458668435765995697569421");
        assertFalse(tester.isPrime(prime, ROUNDS));
        
        prime = new BigInt("32498435834865326945234875183518437534758431");
        assertFalse(tester.isPrime(prime, ROUNDS));
        
        prime = new BigInt("21346587698797674564778007867511111");
        assertFalse(tester.isPrime(prime, ROUNDS));
        
        prime = new BigInt("7665795679597579578956967957");
        assertFalse(tester.isPrime(prime, ROUNDS));
        
        prime = new BigInt("457568756845876866786");
        assertFalse(tester.isPrime(prime, ROUNDS));
        
        // PRIMES
        
        prime = new BigInt("3413655180212895498178781318649304374323280987088119887692331010323414684827125197672300150960027799");
        assertTrue(tester.isPrime(prime, ROUNDS));
        
        prime = new BigInt("5092943506518719889812416360081347678591043664240116855478706414621684398967230150244531492607706873");
        assertTrue(tester.isPrime(prime, ROUNDS));
    }
}
