package mdk;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.*;

import static mdk.Main.log;

public class Crypto {
    static Cipher cipher;
    static Map<String, String> ciphers = new TreeMap<>();
    private static boolean initialized = false;

    public static void init() {
        if (initialized) return;
        initialized = true;
        log("Finding available ciphers");
        Provider[] ps = Security.getProviders();
        for (Provider p : ps) {
            Set<Provider.Service> svcs = p.getServices();
            if (svcs.size() > 0) {
                for (Provider.Service svc : svcs) {
                    if (svc.getAlgorithm().length() < 10) {
                        ciphers.put(svc.getAlgorithm(), p.getName());
                    }
                }
            }
        }
        log("Some encryption algorithms may need custom provider installed");
        log("Supported ones are: AES, Blowfish, ARCFOUR");
    }

    public static String encode(byte[] bytes, String mode) {
        switch (mode.toLowerCase()) {
        case "base64": {
            return Base64.getEncoder().encodeToString(bytes);
        }
        case "bytes": {
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                result.append(b);
                result.append(" ");
            }
            return result.toString();
        }
        case "binary": {
            StringBuilder binary = new StringBuilder();
            for (byte b : bytes) {
                int val = b;
                for (int i = 0; i < 8; i++) {
                    binary.append((val & 128) == 0 ? 0 : 1);
                    val <<= 1;
                }
                binary.append(' ');
            }
            return binary.toString();
        }
        default: {
            return "<invalid encoding mode>";
        }
        }
    }

    public static byte[] encrypt(String input) throws GeneralSecurityException {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
    }

    public static void setCipher(String algName) {
        if (ciphers.containsKey(algName)) {
            try {
                cipher = Cipher.getInstance(algName, ciphers.get(algName));
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException e) {
                log(e.getLocalizedMessage());
            }
        } else {
            log("Unknown cipher: " + algName);
        }
    }

    private static SecretKeySpec secretKey;

    public static void setKey(String k) {
        try {
            byte[] key = k.getBytes(StandardCharsets.UTF_8);
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, cipher.getAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            log(e.getLocalizedMessage());
        }
    }
}
