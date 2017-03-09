/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prime;

import bigint.BigIntDec;

/**
 *
 * @author jacke
 */
public interface TesterFactory<T> {
    T build(BigIntDec number);
}
