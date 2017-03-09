package bigint;

public interface BigIntFactory<T> {
    T build();
    T build(T that);
    T build(int integer);
    T build(int[] digits);
    T build(String number);
    T getZero();
    T getOne();
    T getTwo();
    // This would be 10 for Decimal and 16 for Hexadecimal, 2^32 for BigInt32
    int getBase();
}
