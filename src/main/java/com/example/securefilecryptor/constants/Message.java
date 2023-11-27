package com.example.securefilecryptor.constants;

public enum Message {
    PLACEHOLDER_PATH("E.g. /Users/user/desktop"),
    FILE_PATH_ERROR("Your file path is empty!"),
    PASSWORD_DO_NOT_MATCH("Your passwords do not match"),
    SHORT_PASSWORD("Your password must be at least 8 characters long!"),
    CIPHER_ERROR("Error while ciphering"),
    LOAD_FILE_ERROR("The was an error loading your file"),
    ENCRYPTION_FINISHED("Your encryption is finished"),
    DECRYPTION_FINISHED("Your decryption is finished"),
    EMPTY_PASSWORD("One of your passwords is empty"),
    CORRUPTED_FILE("The file has been corrupted");

    private final String message;

    Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
