package com.example.securefilecryptor.exceptions;

import com.example.securefilecryptor.constants.Message;

public class CipherErrorException extends Exception{
    public CipherErrorException(){
        super(Message.CIPHER_ERROR.getMessage());
    }
}
