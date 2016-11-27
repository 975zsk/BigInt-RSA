/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bigint;

import static bigint.BigInt.BASE;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 *
 * @author jacke
 */
public class BigIntOperations {
    
    public BigIntOperations() {}
    
    /**
     * ADD
     * @param x the left operand
     * @param y the right operand
     * @return a new BigInt that is the sum of x and y
     */
    public BigInt add(BigInt x, BigInt y) {
        BigInt c = bootstrapFrom(x, y);
        int over = 0;
        
        for(int i = x.size - 1; i >= 0; i--) {
            int sum = x.digits[i] + y.digits[i] + over;
            over = sum / BASE;
            c.digits[i] = sum % BASE;
        }
        c.resetSpart();
        return c;
    }
    
    public BigInt sub(BigInt x, BigInt y) {
        BigInt c = bootstrapFrom(x, y);
        for(int i = x.size - 1; i >= 0; i--) {
            int diff = x.digits[i] - y.digits[i];
            if(diff < 0) {
                if(i > 0 && x.digits[i - 1] > 0) {
                    x.digits[i - 1]--;
                    diff = BASE + x.digits[i] - y.digits[i];
                }
            }
            c.digits[i] = diff;
        }
        c.resetSpart();
        return c;
    }
    
    public BigInt mul(BigInt x , BigInt y) {
        BigInt c = bootstrapFrom(x, y);
        ArrayList<BigInt> products = new ArrayList<>();
        final int xStartIndex = x.getIndexOfFirstSignificantDigit();
        final int yStartIndex = y.getIndexOfFirstSignificantDigit();
        int prod;
        int over = 0;
        int l = 0;
        int k = x.size - 1;
        return c;
    }
    
    public boolean equals(BigInt x, BigInt y) {
        if(x.spart != y.spart) {
            return false;
        }
        int xStartIndex = x.getIndexOfFirstSignificantDigit();
        int yStartIndex = y.getIndexOfFirstSignificantDigit();
        
        while (xStartIndex < x.size && yStartIndex < y.size) {
            if(x.digits[xStartIndex] != y.digits[yStartIndex]) {
                return false;
            }
            xStartIndex++;
            yStartIndex++;
        }
        
        return true;
    }
    
    // Returns true if x is greater than y
    public boolean gt(BigInt x, BigInt y) {
        if(x.spart > y.spart) {
            return true;
        }
        else if(x.spart < y.spart) {
            return false;
        }
        else {
            int xStartIndex = x.getIndexOfFirstSignificantDigit();
            int yStartIndex = y.getIndexOfFirstSignificantDigit();
            
            while (xStartIndex < x.size && yStartIndex < y.size) {
                if(x.digits[xStartIndex] > y.digits[yStartIndex]) {
                    return true;
                }
                xStartIndex++;
                yStartIndex++;
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
    
    private void setEqualSizes(BigInt x, BigInt y) {
        if(x.size == y.size) {
            return;
        }
        
        int[] newDigits;
        int j, resizeTo;
        BigInt toBeResized, theGreaterOne;
        
        if(x.size < y.size) {
            toBeResized = x;
            resizeTo = y.size;
        }
        else {
            toBeResized = y;
            resizeTo = x.size;
        }
        
        newDigits = new int[resizeTo];
        j = toBeResized.size - 1;
        
        for(int i = newDigits.length - 1; j >= toBeResized.spart; i--) {
            newDigits[i] = toBeResized.digits[j];
            j--;
        }
        
        toBeResized.digits = newDigits;
        toBeResized.size = newDigits.length;
    }
    
    private BigInt bootstrapFrom(BigInt x, BigInt y) {
        BigInt c = new BigInt();
        setEqualSizes(x, y);
        c.initializeWithSize(x.size);
        return c;
    }
    
}
