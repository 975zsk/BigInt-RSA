/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prime;

import bigint.BigInt;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author jacke
 */
public class App {
    
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        PrimeTestRunner<PrimeTesterEuler> tester = new PrimeTestRunner<>(new PrimeTesterEuler.Factory());
        
        BigInt prime = new BigInt("2409130781894986571956777721649968801511465915451196376269177305066867");
        boolean res = tester.isPrime(prime, 10);
    }
    
}
