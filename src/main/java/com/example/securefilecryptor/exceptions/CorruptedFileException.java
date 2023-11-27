package com.example.securefilecryptor.exceptions;

import com.example.securefilecryptor.constants.Message;

public class CorruptedFileException extends Exception{
    public CorruptedFileException(){
        super(Message.CORRUPTED_FILE.getMessage());
    }
}
