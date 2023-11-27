package com.example.securefilecryptor.exceptions;

import com.example.securefilecryptor.constants.Message;

public class LoadFileErrorException extends Exception{
    public LoadFileErrorException(){
        super(Message.LOAD_FILE_ERROR.getMessage());
    }
}
