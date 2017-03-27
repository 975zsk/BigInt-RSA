package prime;

import bigint.BigInt32;

/**
 *
 * @author Jakob Pupke
 */
public interface TesterFactory<T> {
    T build(BigInt32 number);
}
