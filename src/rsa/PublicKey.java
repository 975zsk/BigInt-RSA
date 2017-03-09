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
public class PublicKey {
    BigIntDec e;
    BigIntDec n;
    
    public PublicKey(BigIntDec e, BigIntDec n) {
        this.e = e;
        this.n = n;
    }
}
