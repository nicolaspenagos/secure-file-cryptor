package com.example.securefilecryptor.exceptions;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class ExceptionUtil {
    public static void handleSecurityException(GeneralSecurityException e) throws CipherErrorException, CorruptedFileException {
        if (e instanceof NoSuchAlgorithmException || e instanceof InvalidKeySpecException ||
                e instanceof InvalidKeyException || e instanceof InvalidAlgorithmParameterException) {
            throw new CipherErrorException();
        } else if (e instanceof BadPaddingException || e instanceof NoSuchPaddingException ||
                e instanceof IllegalBlockSizeException) {
            throw new CorruptedFileException();
        }
    }

}
