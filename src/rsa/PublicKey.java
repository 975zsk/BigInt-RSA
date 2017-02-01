/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rsa;

import bigint.BigInt;

/**
 *
 * @author jacke
 */
public class PublicKey {
    BigInt e;
    BigInt n;
    
    public PublicKey(BigInt e, BigInt n) {
        this.e = e;
        this.n = n;
    }
}
