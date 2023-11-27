package com.example.securefilecryptor.exceptions;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * The {@code ExceptionUtil} class provides utility methods for handling security-related exceptions
 * that may occur during file encryption or decryption.
 * It categorizes these exceptions and throws corresponding custom exceptions for better error handling.
 *
 * @author nicolaspenagos
 * @version 1.0
 */
public class ExceptionUtil {

    /**
     * Handles a {@code GeneralSecurityException} by categorizing and throwing custom exceptions.
     *
     * @param e The {@code GeneralSecurityException} to handle.
     * @throws CipherErrorException   If the exception indicates a cipher-related error.
     * @throws CorruptedFileException If the exception indicates a corrupted file error.
     */
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
