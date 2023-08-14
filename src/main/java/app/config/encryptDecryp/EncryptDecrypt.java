package app.config.encryptDecryp;

import javafx.beans.NamedArg;
import org.jetbrains.annotations.NotNull;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Base64;

public class EncryptDecrypt {

    public static String hashPlainText(String plainText) {
        String saltValue = BCrypt.gensalt();
        return BCrypt.hashpw(plainText, saltValue);
    }

    public static boolean verifyHashValue(String plainText, String hashValue) {
        return BCrypt.checkpw(plainText, hashValue);
    }



    /** THIS METHOD WHEN CALLED SHALL TAKE A USER'S INPUT AS AN ARGUMENT AND RETURN A HASHED
     * REPRESENTATIVE OF THE USER'S INPUT.
     */
    public static String encryptText(@NotNull @NamedArg("User's Password")String userInput) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(userInput.getBytes());
    }

    /**
     * THIS METHOD WHEN INVOKED SHALL TAKE TWO ARGUMENTS AND RETURN TRUE IF THEY MATCH ELSE FALSE.
     **/
    public static boolean validateText(@NamedArg("EncryptedPassword") String hashCode, @NamedArg("UserText")String plainText) {
        boolean flag = false;
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] hashedByte = decoder.decode(hashCode);
        String dehashString = new String(hashedByte);
        if(dehashString.equals(plainText)) {
            flag = true;
        }
        return flag;
    }

    //THIS METHOD WHEN CALLED SHALL DECODE AND RETURN THE PASSWORD IN PLAIN TEXT.
    public static String convertHashToText(@NamedArg("EncryptedPassword") String hashCode) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] hashedByte = decoder.decode(hashCode);
        return new String(hashedByte);
    }




}//end of class
