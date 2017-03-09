package bigint;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Jakob Pupke
 */
public class BigIntOperations<T extends BigInt> {

    private BigIntFactory<T> factory;
    private Helper<T> helper;
    private T ZERO;
    private T ONE;
    private T TWO;
    private int BASE;

    public BigIntOperations(BigIntFactory<T> fact) {
        factory = fact;
        helper = new Helper<>(factory);
        ZERO = fact.getZero();
        ONE = fact.getOne();
        TWO = fact.getTwo();
        BASE = fact.getBase();
    }
    
    public T add(T x, T y) {
        if (x.isNeg() && y.isNeg()) {
            return (T) add((T) x.neg(), (T) y.neg()).neg();
        }
        if (x.isNeg() && y.isPos()) {
            T absX = (T) x.abs();
            if (gt(absX, y)) {
                return (T) sub((T) x.neg(), y).neg();
            }
            if (lt(absX, y)) {
                return sub(y, (T) x.neg());
            }
            return ZERO; // -100 + 100
        }
        if (x.isPos() && y.isNeg()) {
            T absY = (T) y.abs();
            if (gt(x, absY)) {
                return sub(x, (T) y.neg());
            }
            if (lt(x, absY)) {
                return (T) sub((T) y.neg(), x).neg();
            }
            return ZERO; // 100 + -100
        }
        
        // OK both BigInts are positive -> Normal addition
        
        T c = factory.build();
        c.initializeWithSize(Math.max(x.digits.length, y.digits.length) + 1);
        int xIdx = x.digits.length - 1;
        int yIdx = y.digits.length - 1;
        int cIdx = c.digits.length - 1;
        int carry = 0;
        int xSummand;
        int ySummand;
        int sum;
        
        while(cIdx >= 0) {
            if (xIdx < 0) {
                xSummand = 0;
            }
            else {
                xSummand = x.digits[xIdx];
            }
            if (yIdx < 0) {
                ySummand = 0;
            }
            else {
                ySummand = y.digits[yIdx];
            }
            sum = xSummand + ySummand + carry;
            carry = sum / BASE;
            c.digits[cIdx] = sum % BASE;
            xIdx--;
            yIdx--;
            cIdx--;
        }
        
        return (T) c.resize();
    }

    T sub(T x, T y) {
        if (x.isNeg() && y.isNeg()) {
            return add(x, (T) y.neg());
        }
        if (x.isNeg() && y.isPos()) {
            return (T) add((T) x.neg(), y).neg();
        }
        if (x.isPos() && y.isNeg()) {
            return add(x, (T) y.neg());
        }
        
        // both positive
        
        if (lt(x, y)) {
            return (T) sub(y, x).neg();
        }
        
        T c = factory.build();
        c.initializeWithSize(Math.max(x.digits.length, y.digits.length) + 1);
        
        int xIdx = x.digits.length - 1;
        int yIdx = y.digits.length - 1;
        int cIdx = c.digits.length - 1;
        
        int diff;
        int minuend;
        int subtrahend;
        int carry = 0;
        
        while(cIdx > 0) {
            if (xIdx < 0) {
                minuend = 0;
            }
            else {
                minuend = x.digits[xIdx];
            }
            if (yIdx < 0) {
                subtrahend = 0;
            }
            else {
                subtrahend = y.digits[yIdx];
            }
            diff = minuend - subtrahend - carry;
            carry = 0;
            if (diff < 0) {
                carry = 1;
                diff += BASE;
            }
            c.digits[cIdx] = diff;
            xIdx--;
            yIdx--;
            cIdx--;
        }

        return (T) c.resize();
    }
    
    T mul(T x, T y) {
        if (x.sign != y.sign) {
            return (T) mul((T) x.setSign(true), (T) y.setSign(true)).neg();
        }
        T c;
        /* store intermediary results in this array.
           later they will be combined by addition */
        //T[] products = new T[x.digits.length];
        T[] products = (T[]) Array.newInstance(x.getClass(), x.digits.length);
        int prod, k;
        int step = 0, carry = 0;
        for(int i = x.digits.length - 1; i >= 0; i--) {
            /*
            On each step make a new BigIntDec object and add it to
            the products array
            */
            c = factory.build();
            c.initializeWithSize(y.digits.length + 1 + step);
            k = c.digits.length - 1 - step;
            
            for(int j = y.digits.length - 1; j >= 0; j--) {
                prod = x.digits[i] * y.digits[j] + carry;
                c.digits[k] = prod % BASE;
                carry = prod / BASE;
                k--;
            }
            
            if (carry != 0) {
                c.digits[0] = carry;
                carry = 0;
            }
            
            step++;
            products[i] = factory.build((T) c.resize());
        }
        // requires Java 8;
        // Stream<BigIntDec> productsStream = Arrays.stream(products);
        // Optional<BigIntDec> res = productsStream.reduce(BigIntDec::add);
        
        // works in Java 7/6
        return (T) helper.reduceByAddition(products).resize();
    }
    
    DivisionResult div(T x, T y) {
        return div(x, y, 1);
    }

    private DivisionResult div(T x, T y, int factor) {
        if (y.isZero()) {
            throw new IllegalArgumentException("Division by zero is not allowed");
        }
        
        if (x.sign != y.sign) {
            return div((T) x.abs(), (T) y.abs(), factor).neg();
        }
        
        if (gt(y, x)) {
            return new DivisionResult(ZERO, x);
        }
        if (y.equals(x)) {
            return new DivisionResult(ONE, ZERO);
        }
        if (y.equals(ONE)) {
            return new DivisionResult(x, ZERO);
        }
        
        if (y.digits[0] < BASE / 2) {
            // So the Schätzfunktion (guess function!?) will work
            factor = BASE / (y.digits[0] + 1);
            T d = factory.build(factor);
            return div(mul(x, d), mul(y, d), factor);
        }
        
        List<Integer> guesses = new ArrayList<>();
        T dividend;
        T mulRes;
        T rest = factory.build();
        int indexR = y.digits.length;
        int[] digits = Arrays.copyOfRange(x.digits, 0, y.digits.length);
        boolean firstRun = true;
        int guess;
        
        while(indexR < x.digits.length || firstRun) {
            dividend = factory.build(digits);
            if (lt(dividend, y)) {
                dividend.shiftLeftBy(1);
                dividend.digits[dividend.digits.length - 1] = x.digits[indexR];
                indexR++;
            }
            guess = helper.guess(dividend, y);
            guesses.add(guess);
            mulRes = (T) factory.build(guess).mul(y);
            rest = (T) dividend.sub(mulRes);
            digits = rest.digits;
            firstRun = false;
        }
        
        int ds[] = new int[guesses.size()];
        for(int i = 0; i < guesses.size(); i++) {
            int g = guesses.get(i);
            ds[i] = g;
        }
        
        T res = factory.build(ds);
        
        if (factor != 1) {
            /* dividend and divisor were multiplied by a factor,
               thus the rest needs to be divided by this factor */
            rest = (T) div(rest, factory.build(factor)).quotient;
        }
        return new DivisionResult(res, rest);
    }
    
    T mod(T x, T m) {
        return (T) div(x, m).rest;
    }
    
    // https://courses.csail.mit.edu/6.006/spring11/exams/notes3-karatsuba
    T karatsuba(T x, T y) {
        int size = Math.max(x.digits.length, y.digits.length);
        int KARATSUBA_LIMIT = 20;
        if (size <= KARATSUBA_LIMIT) {
            return mul(x, y);
        }
        
        int half = size / 2;
        int baseExponent = size - half;
        
        x.extendWithZeros(size);
        y.extendWithZeros(size);
        
        T[] parts = helper.getParts(x, y);
        
        T xH = parts[0]; // xHigh
        T xL = parts[1]; // xLow
        T yH = parts[2]; // yHigh
        T yL = parts[3]; // yLow
        
        T k = add(xH, xL);
        T l = add(yH, yL);
        
        T a = karatsuba(xH, yH);
        T d = karatsuba(xL, yL);
        T e1 = karatsuba(k, l);
        T e2 = sub(e1, a);
        T e = sub(e2, d);
        
        boolean g = a.isNeg() || d.isNeg() || e.isNeg();
        
        T res1 = (T) a.shiftLeftBy(2*baseExponent);
        T res2 = (T) e.shiftLeftBy(baseExponent);
        
        return add(add(res1, res2), d);
    }
    
    private T pow(T x, int e, T m, boolean withMod) {
        if (e < 0) throw new IllegalArgumentException("Negative exponents are not supported");
        if (e == 1) return x;
        if (e == 0) return ONE;
        if (lte(m, ZERO)) throw new IllegalArgumentException("The modul must be positive, but was " + m.toString() + ".");
        
        T res = ONE;
        String bits = Integer.toBinaryString(e);
        for(int i = 0; i < bits.length(); i++) {
            res = mul(res, res);
            if(withMod) {
                res = mod(res, m);
            }
            if (bits.charAt(i) == '1') {
                res = mul(res, x);
                if(withMod) {
                    res = mod(res, m);
                }
            }
        }
        return res;
    }
    
    T pow(T x, int e) {
        // The module will not be used in the case
        return pow(x, e, ONE, false);
    }
    
    T powMod(T x, int e, T m) {
        return pow(x, e, m, true);
    }
    
    T powMod(T a, T n, T m) {
        T res = ONE;
        T t = factory.build(a);
        while(gt(n, ZERO)) {
            if(mod(n, TWO).equals(ONE)) {
                res = mod(mul(res, t), m);
            }
            t = mod(mul(t, t), m);
            n = (T) div(n, TWO).quotient;
        }
        return res;
    }
    
    T powModPrim(T x, int e, T p) throws Exception {
        T pLow = sub(p, ONE);
        if(lt(factory.build(e), pLow)) {
            // e < p - 1
            return powMod(x, e, p);
        }
        if(x.lt(p)) {
            e = mod(factory.build(e), pLow).toInt();
            return powMod(x, e, p);
        }
        if(gcd(x, p).equals(ONE)) {
            // gcd(x, p) == 1
            e = factory.build(e).mod(pLow).toInt();
        }
        return powMod(x, e, p);
    }
    
    T gcd(T x, T y) {
        if (x.isZero() && y.isZero()) { throw new IllegalArgumentException("Both numbers must not be ZERO"); }
        if (x.isZero()) { return y; }
        if (y.isZero()) { return x; }
        if (x.isNeg()) { x.sign = true; }
        if (y.isNeg()) { y.sign = true; }
        
        // What is a good limit here??
        if(Math.abs(x.digits.length - y.digits.length) > 5) {
            if(x.lt(y)) {
                helper.exchange(x, y);
            }
            x = mod(x, y);
            //return gcd(x.mod(y), y);
        }
        
        while(y.gt(ZERO)) {
            if (y.lt(x)) {
                helper.exchange(x, y);
            }
            y = sub(y, x);
        }
        
        return x;
    }
    
    GcdLinComb egcd(T a, T b) {
        
        // TODO: check for valid a, b
        
        T u = ONE; T v = factory.build();
        T s = factory.build(); T t = ONE;
        T uO, vO, q;
        DivisionResult dv = a.div(b);
        q = (T) dv.quotient;
        
        while(!b.isZero()) {
            uO = u;
            vO = v;
            
            u = factory.build(s);
            v = factory.build(t);
            
            s = sub(uO, mul(q, s));
            t = sub(vO, mul(q, t));
            
            a = b;
            b = (T) dv.rest;
            
            if(!b.isZero()) {
                dv = a.div(b);
            }
            
            q = (T) dv.quotient;
            
        }
        
        return new GcdLinComb(a, u, v);
    }
    
    public boolean equals(T x, T y) {
        if (x.sign != y.sign || x.digits.length != y.digits.length) {
            return false;
        }
        
        for(int i = 0; i < x.digits.length; i++) {
            if (x.digits[i] != y.digits[i]) {
                return false;
            }
        }
        
        return true;
    }
    
    // Returns true if x is greater than y
    public boolean gt(T x, T y) {
        int xl = x.digits.length;
        int yl = y.digits.length;
        if (xl < yl) {
            return x.isNeg() && y.isNeg();
        }
        else if (xl > yl) {
            return x.isPos();
        }
        
        if (x.isPos() != y.isPos()) return x.isPos();
            
        for(int i = 0; i < xl; i++) {
            if (x.digits[i] == y.digits[i]) {
                continue;
            }
            return (x.digits[i] > y.digits[i] && x.isPos()) ||
                   (x.digits[i] < y.digits[i] && x.isNeg());
        }

        return false;
    }
    
    public boolean lt(T x, T y) {
        return !equals(x, y) && !gt(x, y);
    }
    
    boolean gte(T x, T y) {
        return equals(x, y) || gt(x, y);
    }

    boolean lte(T x, T y) {
        return equals(x, y) || lt(x, y);
    }
    
}
