package bigint;

import static bigint.BigInt.BASE;
import static bigint.BigInt.ZERO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Jakob Pupke
 */
public class BigIntOperations {
    
    private final int KARATSUBA_LIMIT = 20;
    
    public BigIntOperations() {}
    
    public BigInt add(BigInt x, BigInt y) {
        if (x.isNeg() && y.isNeg()) {
            return add(x.neg(), y.neg()).neg();
        }
        if (x.isNeg() && y.isPos()) {
            BigInt absX = x.abs();
            if (absX.gt(y)) {
                return sub(x.neg(), y).neg();
            }
            if (absX.lt(y)) {
                return sub(y, x.neg());
            }
            return ZERO; // -100 + 100
        }
        if (x.isPos() && y.isNeg()) {
            BigInt absY = y.abs();
            if (x.gt(absY)) {
                return sub(x, y.neg());
            }
            if (x.lt(absY)) {
                return sub(y.neg(), x).neg();
            }
            return ZERO; // 100 + -100
        }
        
        // OK both BigInts are positive -> Normal addition
        
        BigInt c = new BigInt();
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
        
        return c.resize();
    }
    
    public BigInt sub(BigInt x, BigInt y) {
        if (x.isNeg() && y.isNeg()) {
            return add(x, y.neg());
        }
        if (x.isNeg() && y.isPos()) {
            return add(x.neg(), y).neg();
        }
        if (x.isPos() && y.isNeg()) {
            return add(x, y.neg());
        }
        
        // both positive
        
        if (x.lt(y)) {
            return sub(y, x).neg();
        }
        
        BigInt c = new BigInt();
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
        
        return c.resize();
    }
    
    public BigInt mul(BigInt x , BigInt y) {
        if (x.sign != y.sign) {
            return mul(x.setSign(true), y.setSign(true)).neg();
        }
        BigInt c;
        /* store intermediary results in this array.
           later they will be combined by addition */
        BigInt[] products = new BigInt[x.digits.length];
        int prod, k;
        int step = 0, carry = 0;
        for(int i = x.digits.length - 1; i >= 0; i--) {
            /*
            On each step make a new BigInt object and add it to
            the products array
            */
            c = new BigInt();
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
            products[i] = new BigInt(c.resize());
        }
        // requires Java 8;
        // Stream<BigInt> productsStream = Arrays.stream(products);
        // Optional<BigInt> res = productsStream.reduce(BigInt::add);
        
        // works in Java 7/6
        return Helper.reduceByAddition(products).resize();
    }
    
    public DivisionResult div(BigInt x, BigInt y) {
        return div(x, y, 1);
    }
    
    private DivisionResult div(BigInt x, BigInt y, int factor) {
        if (y.isZero()) {
            throw new IllegalArgumentException("Division by zero is not allowed");
        }
        
        if (x.sign != y.sign) {
            return div(x.abs(), y.abs(), factor).neg();
        }
        
        if (y.gt(x)) {
            return new DivisionResult(ZERO, x);
        }
        if (y.equals(x)) {
            return new DivisionResult(BigInt.ONE, ZERO);
        }
        if (y.equals(BigInt.ONE)) {
            return new DivisionResult(x, ZERO);
        }
        
        if (y.digits[0] < BASE / 2) {
            // So the Schätzfunktion (guess function!?) will work
            factor = BASE / (y.digits[0] + 1);
            BigInt d = new BigInt(factor);
            return div(x.mul(d), y.mul(d), factor);
        }
        
        List<Integer> guesses = new ArrayList<>();
        BigInt dividend;
        BigInt mulRes;
        BigInt rest = new BigInt();
        int indexR = y.digits.length;
        int[] digits = Arrays.copyOfRange(x.digits, 0, y.digits.length);
        boolean firstRun = true;
        int guess;
        
        while(indexR < x.digits.length || firstRun) {
            dividend = new BigInt(digits);
            if (dividend.lt(y)) {
                dividend.shiftLeftBy(1);
                dividend.digits[dividend.digits.length - 1] = x.digits[indexR];
                indexR++;
            }
            guess = Helper.guess(dividend, y);
            guesses.add(guess);
            mulRes = new BigInt(guess).mul(y);
            rest = dividend.sub(mulRes);
            digits = rest.digits;
            firstRun = false;
        }
        
        int ds[] = new int[guesses.size()];
        for(int i = 0; i < guesses.size(); i++) {
            int g = guesses.get(i);
            ds[i] = g;
        }
        
        BigInt res = new BigInt(ds);
        
        if (factor != 1) {
            /* dividend and divisor were multiplied by a factor,
               thus the rest needs to be divided by this factor */
            rest = rest.div(new BigInt(factor)).quotient;
        }
        return new DivisionResult(res, rest);
    }
    
    public BigInt mod(BigInt x, BigInt m) {
        return x.div(m).rest;
    }
    
    // https://courses.csail.mit.edu/6.006/spring11/exams/notes3-karatsuba
    public BigInt karatsuba(BigInt x, BigInt y) {
        int size = Math.max(x.digits.length, y.digits.length);
        if (size <= KARATSUBA_LIMIT) {
            return mul(x, y);
        }
        
        int half = size / 2;
        int baseExponent = size - half;
        
        x.extendWithZeros(size);
        y.extendWithZeros(size);
        
        BigInt[] parts = Helper.getParts(x, y);
        
        BigInt xH = parts[0]; // xHigh
        BigInt xL = parts[1]; // xLow
        BigInt yH = parts[2]; // yHigh
        BigInt yL = parts[3]; // yLow
        
        BigInt k = xH.add(xL);
        BigInt l = yH.add(yL);
        
        BigInt a = karatsuba(xH, yH);
        BigInt d = karatsuba(xL, yL);
        BigInt e1 = karatsuba(k, l);
        BigInt e2 = e1.sub(a);
        BigInt e = e2.sub(d);
        
        boolean g = a.isNeg() || d.isNeg() || e.isNeg();
        
        BigInt res1 = a.shiftLeftBy(2*baseExponent);
        BigInt res2 = e.shiftLeftBy(baseExponent);
        
        return res1.add(res2).add(d);   
    }
    
    private BigInt pow(BigInt x, int e, BigInt m, boolean withMod) {
        if (e < 0) throw new IllegalArgumentException("Negative exponents are not supported");
        if (e == 1) return x;
        if (e == 0) return BigInt.ONE;
        if (m.lte(ZERO)) throw new IllegalArgumentException("The modul must be positive, but was " + m.toString() + ".");
        
        BigInt res = BigInt.ONE;
        if (e == 0) {
            return res;
        }
        String bits = Integer.toBinaryString(e);
        for(int i = 0; i < bits.length(); i++) {
            res = res.mul(res);
            if(withMod) {
                res = res.mod(m);
            }
            if (bits.charAt(i) == '1') {
                res = res.mul(x);
                if(withMod) {
                    res = res.mod(m);
                }
            }
        }
        return res;
    }
    
    public BigInt pow(BigInt x, int e) {
        // The module will not be used in the case
        return pow(x, e, BigInt.ONE, false);
    }
    
    public BigInt powMod(BigInt x, int e, BigInt m) {
        return pow(x, e, m, true);
    }
    
    public BigInt powMod(BigInt a, BigInt n, BigInt m) {
        BigInt res = BigInt.ONE;
        BigInt t = new BigInt(a);
        while(n.gt(BigInt.ZERO)) {
            if(n.mod(BigInt.TWO).equals(BigInt.ONE)) {
                res = res.mul(t).mod(m);
            }
            t = t.mul(t).mod(m);
            n = n.div(BigInt.TWO).quotient;
        }
        return res;
    }
    
    public BigInt powModPrim(BigInt x, int e, BigInt p) {
        BigInt pLow = p.sub(BigInt.ONE);
        if(new BigInt(e).lt(pLow)) {
            // e < p - 1
            return powMod(x, e, p);
        }
        if(x.lt(p)) {
            e = Helper.toInt(new BigInt(e).mod(pLow));
            return powMod(x, e, p);
        }
        if(gcd(x, p).equals(BigInt.ONE)) {
            // gcd(x, p) == 1
            e = Helper.toInt(new BigInt(e).mod(pLow));
        }
        return powMod(x, e, p);
    }
    
    public BigInt gcd(BigInt x, BigInt y) {
        if (x.isZero() && y.isZero()) { throw new IllegalArgumentException("Both numbers must not be ZERO"); }
        if (x.isZero()) { return y; }
        if (y.isZero()) { return x; }
        if (x.isNeg()) { x.sign = true; }
        if (y.isNeg()) { y.sign = true; }
        
        // What is a good limit here??
        if(Math.abs(x.digits.length - y.digits.length) > 5) {
            if(x.lt(y)) {
                Helper.exchange(x, y);
            }
            x = x.mod(y);
            //return gcd(x.mod(y), y);
        }
        
        while(y.gt(ZERO)) {
            if (y.lt(x)) {
                Helper.exchange(x, y);
            }
            y = y.sub(x);
        }
        
        return x;
    }
    
    public GcdLinComb egcd(BigInt a, BigInt b) {        
        
        // TODO: check for valid a, b
        
        BigInt u = BigInt.ONE; BigInt v = new BigInt(0);
        BigInt s = new BigInt(0); BigInt t = BigInt.ONE;
        BigInt uO, vO, q;
        DivisionResult dv = a.div(b);
        q = dv.quotient;
        
        while(!b.isZero()) {
            uO = u;
            vO = v;
            
            u = new BigInt(s);
            v = new BigInt(t);
            
            s = uO.sub(q.mul(s));
            t = vO.sub(q.mul(t));
            
            a = b;
            b = dv.rest;
            
            if(!b.isZero()) {
                dv = a.div(b);
            }
            
            q = dv.quotient;
            
        }
        
        return new GcdLinComb(a, u, v);
    }
    
    public boolean equals(BigInt x, BigInt y) {
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
    public boolean gt(BigInt x, BigInt y) {
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
    
    public boolean lt(BigInt x, BigInt y) {
        return !equals(x, y) && !gt(x, y);
    }
    
    boolean gte(BigInt x, BigInt y) {
        return equals(x, y) || gt(x, y);
    }

    boolean lte(BigInt x, BigInt y) {
        return equals(x, y) || lt(x, y);
    }
    
}
