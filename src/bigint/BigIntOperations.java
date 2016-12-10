/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bigint;

import static bigint.BigInt.BASE;

/**
 *
 * @author jacke
 */
public class BigIntOperations {
    
    private final int KARATSUBA_LIMIT = 2;
    
    public BigIntOperations() {}
    
    public BigInt add(BigInt x, BigInt y) {
        BigInt c = new BigInt();
        c.initializeWithSize(Math.max(x.digits.length, y.digits.length) + 1);
        int xIdx = x.digits.length - 1;
        int yIdx = y.digits.length - 1;
        int cIdx = c.digits.length - 1;
        int over = 0;
        int xSummand;
        int ySummand;
        int sum;
        
        while(cIdx >= 0) {
            if(xIdx < 0) {
                xSummand = 0;
            }
            else {
                xSummand = x.digits[xIdx];
            }
            if(yIdx < 0) {
                ySummand = 0;
            }
            else {
                ySummand = y.digits[yIdx];
            }
            sum = xSummand + ySummand + over;
            over = sum / BASE;
            c.digits[cIdx] = sum % BASE;
            xIdx--;
            yIdx--;
            cIdx--;
        }
        
        return c.resize();
    }
    
    public BigInt sub(BigInt x, BigInt y) {
        BigInt c = new BigInt();
        c.initializeWithSize(Math.max(x.digits.length, y.digits.length) + 1);
        
        int xIdx = x.digits.length - 1;
        int yIdx = y.digits.length - 1;
        int cIdx = c.digits.length - 1;
        
        int diff;
        int minuend;
        int subtrahend;
        
        while(cIdx > 0) {
            if(xIdx < 0) {
                minuend = 0;
            }
            else {
                minuend = x.digits[xIdx];
            }
            if(yIdx < 0) {
                subtrahend = 0;
            }
            else {
                subtrahend = y.digits[yIdx];
            }
            diff = minuend - subtrahend;
            if(diff < 0) {
                if(xIdx > 0 && x.digits[xIdx - 1] > 0) {
                    x.digits[xIdx - 1]--;
                    diff += BASE;
                }
            }
            c.digits[cIdx] = diff;
            xIdx--;
            yIdx--;
            cIdx--;
        }
        
        return c.resize();
    }
    
    public BigInt mul(BigInt x , BigInt y) {
        BigInt c;
        BigInt[] products = new BigInt[x.digits.length];
        int prod, k;
        int step = 0;
        int over = 0;
        for(int i = x.digits.length - 1; i >= 0; i--) {
            c = new BigInt();
            c.initializeWithSize(y.digits.length + 1 + step);
            k = c.digits.length - 1 - step;
            
            for(int j = y.digits.length - 1; j >= 0; j--) {
                prod = x.digits[i] * y.digits[j] + over;
                c.digits[k] = prod % BASE;
                over = prod / BASE;
                k--;
            }
            
            if(over != 0) {
                c.digits[0] = over;
                over = 0;
            }
            
            step++;
            products[i] = new BigInt(c.resize());
        }
        // requires Java 8;
        // Stream<BigInt> productsStream = Arrays.stream(products);
        // Optional<BigInt> res = productsStream.reduce(BigInt::add);
        
        // works in Java 7
        return reduceByAddition(products).resize();
    }
    
    public DivisionResult div(BigInt x, BigInt y) {
        if(y.isZero()) {
            throw new IllegalArgumentException("Division by zero is not allowed");
        }
        if(y.gt(x)) {
            return new DivisionResult(new BigInt(), x);
        }
        if(y.equals(x)) {
            return new DivisionResult(new BigInt("1"), new BigInt());
        }
        
        return new DivisionResult();
    }
    
    public BigInt karatsuba(BigInt x, BigInt y) {
        int size = Math.max(x.digits.length, y.digits.length);
        if(size <= KARATSUBA_LIMIT) {
            return mul(x, y);
        }
        
        int half = size / 2;
        int baseExponent = size - half;
        
        x.extendWithZeros(size);
        y.extendWithZeros(size);
        
        BigInt[] parts = getParts(x, y);
        
        BigInt xH = parts[0]; // xHigh
        BigInt xL = parts[1]; // xLow
        BigInt yH = parts[2]; // yHigh
        BigInt yL = parts[3]; // yLow
        
        BigInt k = xH.add(xL);
        BigInt l = yH.add(yL);
        
        BigInt a = karatsuba(xH, yH);
        BigInt d = karatsuba(xL, yL);
        BigInt e = karatsuba(k, l).sub(a).sub(d);
        
        BigInt res1 = a.shiftLeftBy(2*baseExponent);
        BigInt res2 = e.shiftLeftBy(baseExponent);
        
        return res1.add(res2).add(d);
        
    }
    
    public boolean equals(BigInt x, BigInt y) {
        if(x.digits.length != y.digits.length) {
            return false;
        }
        
        for(int i = 0; i < x.digits.length; i++) {
            if(x.digits[i] != y.digits[i]) {
                return false;
            }
        }
        
        return true;
    }
    
    // Returns true if x is greater than y
    public boolean gt(BigInt x, BigInt y) {
        if(x.digits.length > y.digits.length) {
            return true;
        }
        else if(x.digits.length < y.digits.length) {
            return false;
        }
        else {
            
            for(int i = 0; i < x.digits.length; i++) {
                if(x.digits[i] > y.digits[i]) {
                    return true;
                }
            }
            
            return false;
        }
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
    
    private BigInt reduceByAddition(BigInt[] bigInts) {
        if(bigInts.length == 0) {
            // This shouldn't ever be the case, but who knows, 
            // lets be super cautious
            return new BigInt();
        }
        BigInt x = bigInts[0];
        for(int i = 1; i < bigInts.length; i++) {
            x = x.add(bigInts[i]);
        }
        return x;
    }

    private BigInt[] getParts(BigInt x, BigInt y) {
        int j = 0;
        
        int size = x.digits.length;
        int halfL = size / 2;
        int halfR = size - halfL;
        
        int[] xH = new int[halfL];
        int[] xL = new int[halfR];
        int[] yH = new int[halfL];
        int[] yL = new int[halfR];
        
        int k = halfR - 1;
        int l = halfL - 1;
        for(int i = size - 1; i >= 0; i--) {
            if(i >= halfL) {
                xL[k] = x.digits[i];
                yL[k] = y.digits[i];
                k--;
            }
            else {
                xH[l] = x.digits[i];
                yH[l] = y.digits[i];
                l--;
            }
        }
        
        BigInt[] res = new BigInt[4];
        
        res[0] = new BigInt(xH);
        res[1] = new BigInt(xL);
        res[2] = new BigInt(yH);
        res[3] = new BigInt(yL);
        
        return res;
    }
    
}
