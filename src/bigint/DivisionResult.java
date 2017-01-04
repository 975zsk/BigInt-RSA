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
public class DivisionResult {
    BigInt quotient;
    BigInt rest;
    
    public DivisionResult() {
        this.quotient = new BigInt();
        this.rest = new BigInt();
    }
    
    public DivisionResult(BigInt quotient, BigInt rest) {
        this.quotient = quotient;
        this.rest = rest;
    }

    DivisionResult neg() {
        quotient.sign = !quotient.sign;
        return this;
    }
}
