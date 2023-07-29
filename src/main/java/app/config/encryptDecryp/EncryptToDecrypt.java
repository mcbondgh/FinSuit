package app.config.encryptDecryp;

import org.mindrot.jbcrypt.BCrypt;

public class EncryptToDecrypt {

    public static String hashPlainText(String plainText) {
        String saltValue = BCrypt.gensalt();
        return BCrypt.hashpw(plainText, saltValue);
    }

    public static boolean verifyHashValue(String plainText, String hashValue) {
        return BCrypt.checkpw(plainText, hashValue);
    }
}
