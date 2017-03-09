/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rsa;

import bigint.BigIntDec;

/**
 *
 * @author jacke
 */
public class SecretKey {
    BigIntDec p;
    BigIntDec q;
    BigIntDec d;
    BigIntDec n;
    
    public SecretKey(BigIntDec p, BigIntDec q, BigIntDec d, BigIntDec n) {
        this.p = p;
        this.q = q;
        this.d = d;
        this.n = n;
    }
}
