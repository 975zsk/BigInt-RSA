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
public class SecretKey {
    BigInt p;
    BigInt q;
    BigInt d;
    BigInt n;
    
    public SecretKey(BigInt p, BigInt q, BigInt d, BigInt n) {
        this.p = p;
        this.q = q;
        this.d = d;
        this.n = n;
    }
}
