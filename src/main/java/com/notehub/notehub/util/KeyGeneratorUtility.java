package com.notehub.notehub.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KeyGeneratorUtility {

    public static KeyPair generateRsaKey() {
        KeyPair keyPair;

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.genKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException();
        }

        return keyPair;
    }
}