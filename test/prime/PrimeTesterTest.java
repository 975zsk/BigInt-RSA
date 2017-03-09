package prime;

import bigint.BigIntDec;
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
        
        BigIntDec prime = new BigIntDec("2409130781894986571956777721649968801511465915451196376269177305066867");
        assertTrue(tester.isPrime(prime, ROUNDS));
        
        prime = new BigIntDec("9547848065153773335707495885453566120069130270246768806790708393909999");
        assertTrue(tester.isPrime(prime, ROUNDS));
        
        prime = new BigIntDec(2);
        assertTrue(tester.isPrime(prime, ROUNDS));
        
        prime = new BigIntDec(5);
        assertTrue(tester.isPrime(prime, ROUNDS));
        
        prime = new BigIntDec(7);
        assertTrue(tester.isPrime(prime, ROUNDS));
        
        prime = new BigIntDec(13);
        assertTrue(tester.isPrime(prime, ROUNDS));
        
        prime = new BigIntDec(79);
        assertTrue(tester.isPrime(prime, ROUNDS));
        
        // NOT PRIMES
        
        prime = new BigIntDec("30024369469850064594387327432754385943969493248284238828538521");
        assertFalse(tester.isPrime(prime, ROUNDS));
        
        prime = new BigIntDec("45065795679459567865734568458668435765995697569421");
        assertFalse(tester.isPrime(prime, ROUNDS));
        
        prime = new BigIntDec("32498435834865326945234875183518437534758431");
        assertFalse(tester.isPrime(prime, ROUNDS));
        
        prime = new BigIntDec("21346587698797674564778007867511111");
        assertFalse(tester.isPrime(prime, ROUNDS));
        
        prime = new BigIntDec("7665795679597579578956967957");
        assertFalse(tester.isPrime(prime, ROUNDS));
        
        prime = new BigIntDec("457568756845876866786");
        assertFalse(tester.isPrime(prime, ROUNDS));
        
        // PRIMES
        
        prime = new BigIntDec("564819669946735512444543556507");
        assertTrue(tester.isPrime(prime, ROUNDS));
        
        prime = new BigIntDec("5521712099665906221540423207019333379125265462121169655563495403888449493493629943498064604536961775110765377745550377067893607246020694972959780839151452457728855382113555867743022746090187341871655890805971735385789993");
        assertTrue(tester.isPrime(prime, ROUNDS));
    }
}
