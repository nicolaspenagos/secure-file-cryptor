package com.example.securefilecryptor.exceptions;

import com.example.securefilecryptor.constants.Message;

/**
 * The {@code LoadFileErrorException} class is an exception that is thrown when an error occurs
 * while attempting to load a file during file encryption or decryption.
 * It provides an error message obtained from predefined constants in the {@code Message} class.
 *
 * @author nicolaspenagos
 * @version 1.0
 */
public class LoadFileErrorException extends Exception{
    /**
     * Constructs a new {@code LoadFileErrorException} with the predefined error message.
     */
    public LoadFileErrorException(){
        super(Message.LOAD_FILE_ERROR.getMessage());
    }
}
