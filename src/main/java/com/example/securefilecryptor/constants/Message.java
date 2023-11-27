package com.example.securefilecryptor.constants;

/**
 * The {@code Message} enum provides predefined messages for various scenarios in the application.
 * These messages are used for informing users about specific situations or errors.
 * Each message has a corresponding constant that can be accessed using the getMessage() method.
 *
 * Example usage:
 * <pre>{@code
 * String filePathError = Message.FILE_PATH_ERROR.getMessage();
 * }</pre>
 *
 * @author nicolaspenagos
 * @version 1.0
 */
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

    /**
     * Constructs a new {@code Message} with the provided message.
     *
     * @param message The message associated with the constant.
     */
    Message(String message) {
        this.message = message;
    }

    /**
     * Gets the message associated with the constant.
     *
     * @return The message.
     */
    public String getMessage() {
        return message;
    }
}
