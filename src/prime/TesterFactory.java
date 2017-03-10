/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prime;

import bigint.BigIntFactory;

/**
 *
 * @author Jakob Pupke
 */
public interface TesterFactory<T, E> {
    T build(E number, BigIntFactory<E> fact);
}
