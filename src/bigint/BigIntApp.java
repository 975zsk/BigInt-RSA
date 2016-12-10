/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bigint;

/**
 *
 * @author jacke
 */
public class BigIntApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BigInt x = new BigInt("12345678");
        BigInt y = new BigInt("87654321");
        BigInt c = x.karatsuba(y);
    }
    
}
