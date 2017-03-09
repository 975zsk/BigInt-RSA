package bigint;

import static bigint.BigIntDec.BASE;
import static bigint.BigIntDec.ZERO;
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
    
    public BigIntDec add(BigIntDec x, BigIntDec y) {
        if (x.isNeg() && y.isNeg()) {
            return add(x.neg(), y.neg()).neg();
        }
        if (x.isNeg() && y.isPos()) {
            BigIntDec absX = x.abs();
            if (absX.gt(y)) {
                return sub(x.neg(), y).neg();
            }
            if (absX.lt(y)) {
                return sub(y, x.neg());
            }
            return ZERO; // -100 + 100
        }
        if (x.isPos() && y.isNeg()) {
            BigIntDec absY = y.abs();
            if (x.gt(absY)) {
                return sub(x, y.neg());
            }
            if (x.lt(absY)) {
                return sub(y.neg(), x).neg();
            }
            return ZERO; // 100 + -100
        }
        
        // OK both BigInts are positive -> Normal addition
        
        BigIntDec c = new BigIntDec();
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
    
    public BigIntDec sub(BigIntDec x, BigIntDec y) {
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
        
        BigIntDec c = new BigIntDec();
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
    
    public BigIntDec mul(BigIntDec x , BigIntDec y) {
        if (x.sign != y.sign) {
            return mul(x.setSign(true), y.setSign(true)).neg();
        }
        BigIntDec c;
        /* store intermediary results in this array.
           later they will be combined by addition */
        BigIntDec[] products = new BigIntDec[x.digits.length];
        int prod, k;
        int step = 0, carry = 0;
        for(int i = x.digits.length - 1; i >= 0; i--) {
            /*
            On each step make a new BigIntDec object and add it to
            the products array
            */
            c = new BigIntDec();
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
            products[i] = new BigIntDec(c.resize());
        }
        // requires Java 8;
        // Stream<BigIntDec> productsStream = Arrays.stream(products);
        // Optional<BigIntDec> res = productsStream.reduce(BigIntDec::add);
        
        // works in Java 7/6
        return Helper.reduceByAddition(products).resize();
    }
    
    public DivisionResult div(BigIntDec x, BigIntDec y) {
        return div(x, y, 1);
    }
    
    private DivisionResult div(BigIntDec x, BigIntDec y, int factor) {
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
            return new DivisionResult(BigIntDec.ONE, ZERO);
        }
        if (y.equals(BigIntDec.ONE)) {
            return new DivisionResult(x, ZERO);
        }
        
        if (y.digits[0] < BASE / 2) {
            // So the Schätzfunktion (guess function!?) will work
            factor = BASE / (y.digits[0] + 1);
            BigIntDec d = new BigIntDec(factor);
            return div(x.mul(d), y.mul(d), factor);
        }
        
        List<Integer> guesses = new ArrayList<>();
        BigIntDec dividend;
        BigIntDec mulRes;
        BigIntDec rest = new BigIntDec();
        int indexR = y.digits.length;
        int[] digits = Arrays.copyOfRange(x.digits, 0, y.digits.length);
        boolean firstRun = true;
        int guess;
        
        while(indexR < x.digits.length || firstRun) {
            dividend = new BigIntDec(digits);
            if (dividend.lt(y)) {
                dividend.shiftLeftBy(1);
                dividend.digits[dividend.digits.length - 1] = x.digits[indexR];
                indexR++;
            }
            guess = Helper.guess(dividend, y);
            guesses.add(guess);
            mulRes = new BigIntDec(guess).mul(y);
            rest = dividend.sub(mulRes);
            digits = rest.digits;
            firstRun = false;
        }
        
        int ds[] = new int[guesses.size()];
        for(int i = 0; i < guesses.size(); i++) {
            int g = guesses.get(i);
            ds[i] = g;
        }
        
        BigIntDec res = new BigIntDec(ds);
        
        if (factor != 1) {
            /* dividend and divisor were multiplied by a factor,
               thus the rest needs to be divided by this factor */
            rest = rest.div(new BigIntDec(factor)).quotient;
        }
        return new DivisionResult(res, rest);
    }
    
    public BigIntDec mod(BigIntDec x, BigIntDec m) {
        return x.div(m).rest;
    }
    
    // https://courses.csail.mit.edu/6.006/spring11/exams/notes3-karatsuba
    public BigIntDec karatsuba(BigIntDec x, BigIntDec y) {
        int size = Math.max(x.digits.length, y.digits.length);
        if (size <= KARATSUBA_LIMIT) {
            return mul(x, y);
        }
        
        int half = size / 2;
        int baseExponent = size - half;
        
        x.extendWithZeros(size);
        y.extendWithZeros(size);
        
        BigIntDec[] parts = Helper.getParts(x, y);
        
        BigIntDec xH = parts[0]; // xHigh
        BigIntDec xL = parts[1]; // xLow
        BigIntDec yH = parts[2]; // yHigh
        BigIntDec yL = parts[3]; // yLow
        
        BigIntDec k = xH.add(xL);
        BigIntDec l = yH.add(yL);
        
        BigIntDec a = karatsuba(xH, yH);
        BigIntDec d = karatsuba(xL, yL);
        BigIntDec e1 = karatsuba(k, l);
        BigIntDec e2 = e1.sub(a);
        BigIntDec e = e2.sub(d);
        
        boolean g = a.isNeg() || d.isNeg() || e.isNeg();
        
        BigIntDec res1 = a.shiftLeftBy(2*baseExponent);
        BigIntDec res2 = e.shiftLeftBy(baseExponent);
        
        return res1.add(res2).add(d);   
    }
    
    private BigIntDec pow(BigIntDec x, int e, BigIntDec m, boolean withMod) {
        if (e < 0) throw new IllegalArgumentException("Negative exponents are not supported");
        if (e == 1) return x;
        if (e == 0) return BigIntDec.ONE;
        if (m.lte(ZERO)) throw new IllegalArgumentException("The modul must be positive, but was " + m.toString() + ".");
        
        BigIntDec res = BigIntDec.ONE;
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
    
    public BigIntDec pow(BigIntDec x, int e) {
        // The module will not be used in the case
        return pow(x, e, BigIntDec.ONE, false);
    }
    
    public BigIntDec powMod(BigIntDec x, int e, BigIntDec m) {
        return pow(x, e, m, true);
    }
    
    public BigIntDec powMod(BigIntDec a, BigIntDec n, BigIntDec m) {
        BigIntDec res = BigIntDec.ONE;
        BigIntDec t = new BigIntDec(a);
        while(n.gt(BigIntDec.ZERO)) {
            if(n.mod(BigIntDec.TWO).equals(BigIntDec.ONE)) {
                res = res.mul(t).mod(m);
            }
            t = t.mul(t).mod(m);
            n = n.div(BigIntDec.TWO).quotient;
        }
        return res;
    }
    
    public BigIntDec powModPrim(BigIntDec x, int e, BigIntDec p) throws Exception {
        BigIntDec pLow = p.sub(BigIntDec.ONE);
        if(new BigIntDec(e).lt(pLow)) {
            // e < p - 1
            return powMod(x, e, p);
        }
        if(x.lt(p)) {
            e = new BigIntDec(e).mod(pLow).toInt();
            return powMod(x, e, p);
        }
        if(gcd(x, p).equals(BigIntDec.ONE)) {
            // gcd(x, p) == 1
            e = new BigIntDec(e).mod(pLow).toInt();
        }
        return powMod(x, e, p);
    }
    
    public BigIntDec gcd(BigIntDec x, BigIntDec y) {
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
    
    public GcdLinComb egcd(BigIntDec a, BigIntDec b) {
        
        // TODO: check for valid a, b
        
        BigIntDec u = BigIntDec.ONE; BigIntDec v = new BigIntDec(0);
        BigIntDec s = new BigIntDec(0); BigIntDec t = BigIntDec.ONE;
        BigIntDec uO, vO, q;
        DivisionResult dv = a.div(b);
        q = dv.quotient;
        
        while(!b.isZero()) {
            uO = u;
            vO = v;
            
            u = new BigIntDec(s);
            v = new BigIntDec(t);
            
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
    
    public boolean equals(BigIntDec x, BigIntDec y) {
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
    public boolean gt(BigIntDec x, BigIntDec y) {
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
    
    public boolean lt(BigIntDec x, BigIntDec y) {
        return !equals(x, y) && !gt(x, y);
    }
    
    boolean gte(BigIntDec x, BigIntDec y) {
        return equals(x, y) || gt(x, y);
    }

    boolean lte(BigIntDec x, BigIntDec y) {
        return equals(x, y) || lt(x, y);
    }
    
}
