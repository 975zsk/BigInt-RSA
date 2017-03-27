package prime;

import bigint.BigIntFactory;

/**
 *
 * @author Jakob Pupke
 */
public interface TesterFactory<T, E> {
    T build(E number, BigIntFactory<E> fact);
}
