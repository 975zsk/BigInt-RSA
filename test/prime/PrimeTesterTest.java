package prime;

import bigint.BigInt;
import bigint.BigIntFactory;
import org.junit.Ignore;

import java.util.concurrent.ExecutionException;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 *
 * @author Jakob Pupke
 * @param <T>
 */
@Ignore("All PrimesTester*Test classes inherit from this class.")
public abstract class PrimeTesterTest<T extends PrimeTester, E extends BigInt> {
    
    PrimeTestRunner<T, E> tester;
    private final BigIntFactory<E> bigIntFactory;
    static int ROUNDS = 50;
    
    public PrimeTesterTest(TesterFactory<T, E> testerFact, BigIntFactory<E> bigintFact) {
        this.tester = new PrimeTestRunner<>(testerFact, bigintFact);
        bigIntFactory = bigintFact;
    }
    
    public void runIsPrimeTest() throws ExecutionException {

        // PRIMES

        E prime = bigIntFactory.build("2409130781894986571956777721649968801511465915451196376269177305066867");
        assertTrue(tester.isPrime(prime, ROUNDS));

        prime = bigIntFactory.build("9547848065153773335707495885453566120069130270246768806790708393909999");
        assertTrue(tester.isPrime(prime, ROUNDS));

        prime = bigIntFactory.build(2);
        assertTrue(tester.isPrime(prime, ROUNDS));

        prime = bigIntFactory.build(5);
        assertTrue(tester.isPrime(prime, ROUNDS));

        prime = bigIntFactory.build(7);
        assertTrue(tester.isPrime(prime, ROUNDS));

        prime = bigIntFactory.build(13);
        assertTrue(tester.isPrime(prime, ROUNDS));

        prime = bigIntFactory.build(79);
        assertTrue(tester.isPrime(prime, ROUNDS));

        // NOT PRIMES

        prime = bigIntFactory.build("30024369469850064594387327432754385943969493248284238828538521");
        assertFalse(tester.isPrime(prime, ROUNDS));

        prime = bigIntFactory.build("45065795679459567865734568458668435765995697569421");
        assertFalse(tester.isPrime(prime, ROUNDS));

        prime = bigIntFactory.build("32498435834865326945234875183518437534758431");
        assertFalse(tester.isPrime(prime, ROUNDS));

        prime = bigIntFactory.build("21346587698797674564778007867511111");
        assertFalse(tester.isPrime(prime, ROUNDS));

        prime = bigIntFactory.build("7665795679597579578956967957");
        assertFalse(tester.isPrime(prime, ROUNDS));

        prime = bigIntFactory.build("457568756845876866786");
        assertFalse(tester.isPrime(prime, ROUNDS));

        // PRIMES

        prime = bigIntFactory.build("564819669946735512444543556507");
        assertTrue(tester.isPrime(prime, ROUNDS));

        // 300 digits
        prime = bigIntFactory.build("203956878356401977405765866929034577280193993314348263094772646453283062722701277632936616063144088173312372882677123879538709400158306567338328279154499698366071906766440037074217117805690872792848149112022286332144876183376326512083574821647933992961249917319836219304274280243803104015000563790123");
        assertTrue(tester.isPrime(prime, ROUNDS));
    }
}
