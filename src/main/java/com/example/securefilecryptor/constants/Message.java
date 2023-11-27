package com.example.securefilecryptor.constants;

public enum Message {
    PLACEHOLDER_PATH("E.g. /Users/user/desktop"),
    FILE_PATH_ERROR("Your file path is empty!"),
    PASSWORD_DO_NOT_MATCH("Your passwords do not match"),
    SHORT_PASSWORD("Your password must be at least 8 characters long!"),
    DECRYPTION_ERROR("It was not possible to decipher your file"),
    DECRYPTION_SUCCESS("Your file was successfully deciphered"),
    ENCRYPTION_ERROR("It was not possible to cipher your file"),
    ENCRYPTION_SUCCESS("Your file was successfully ciphered"),
    EMPTY_PASSWORD("One of your passwords is empty");

    private final String message;

    Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
