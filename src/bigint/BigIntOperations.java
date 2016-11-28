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
            
            for(int j = 0; j < y.digits.length; j++) {
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
    
}
