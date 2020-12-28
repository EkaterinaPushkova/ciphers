package mdk.tests;

import mdk.Crypto;
import org.junit.Test;

import java.security.GeneralSecurityException;

import static org.junit.Assert.assertEquals;

public class CipherTests {
    private static final String key = "test";

    private String encryptToBase64(String input) {
        byte[] bytes = new byte[0];
        try {
            bytes = Crypto.encrypt(input);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return Crypto.encode(bytes, "base64");
    }

    @Test
    public void testAES() {
        Crypto.init();
        Crypto.setCipher("AES");
        Crypto.setKey(key);
        assertEquals("v7JB4xqq3T3qTtvO5DBXSA==", encryptToBase64("test1"));
    }

    @Test
    public void testBlowfish() {
        Crypto.init();
        Crypto.setCipher("Blowfish");
        Crypto.setKey(key);
        assertEquals("jUHRUjQPpYX5gIy72czNew==", encryptToBase64("key=value"));
    }

    @Test
    public void testARCFOUR() {
        Crypto.init();
        Crypto.setCipher("ARCFOUR");
        Crypto.setKey(key);
        assertEquals("bQ7Ku/35Yg==", encryptToBase64("trututu"));
    }
}
