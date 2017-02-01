/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rsa;

import rsa.PublicKey;
import rsa.SecretKey;

/**
 *
 * @author Jakob Pupke
 */
public class Keys {
    SecretKey secretKey;
    PublicKey publicKey;
    
    public Keys(SecretKey s, PublicKey p) {
        secretKey = s;
        publicKey = p;
    }
}
