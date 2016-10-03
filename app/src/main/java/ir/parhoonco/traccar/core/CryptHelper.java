package ir.parhoonco.traccar.core;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Created by mao on 8/15/2016.
 */
public class CryptHelper {
    private static CryptHelper instance;
    private SecretKey key;

    public CryptHelper() {
        try {
            DESKeySpec keySpec = new DESKeySpec("efejhb234jjewhf".getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            key = keyFactory.generateSecret(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CryptHelper getInstance() {
        if (instance == null) {
            instance = new CryptHelper();
        }
        return instance;
    }

    public String encrypt(String input) {
        if (key != null) {

            try {
                byte[] cleartext = input.getBytes("UTF8");

                Cipher cipher = Cipher.getInstance("DES"); // cipher is not thread safe
                cipher.init(Cipher.ENCRYPT_MODE, key);
                return Base64.encodeToString(cipher.doFinal(cleartext), Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return input;
    }

    public String decrypt(String input) {
        byte[] encrypedPwdBytes = input.getBytes();

        try {
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainTextPwdBytes = (cipher.doFinal(encrypedPwdBytes));
            return Base64.encodeToString(plainTextPwdBytes , Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }
}
