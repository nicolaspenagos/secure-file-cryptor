# secure-file-cryptor

The project provides methods for encrypting and decrypting files using AES encryption. The `FileEncryptor` class employs a combination of password-based key derivation, symmetric encryption, and file hashing to ensure secure file handling. 

For encryption, the tool generates a random salt and derives a secret key using the PBKDF2 key derivation algorithm. It then generates a random initialization vector (IV) for AES encryption. The SHA-256 hash of the original file is calculated and stored along with the IV and salt in an output file. The actual file content is encrypted using the AES algorithm in CBC mode with PKCS5Padding.

For decryption, the tool reads the IV, salt, and original hash from the encrypted file. It derives the secret key from the provided password and salt, decrypts the file using the stored IV, and calculates the SHA-256 hash of the decrypted content. The integrity of the file is verified by comparing the original hash with the hash calculated after processing.

The class uses a singleton pattern to ensure a single instance is used throughout the application. It includes constants for key derivation iterations, algorithm names, buffer size, and the encrypted file extension. The cryptographic operations are handled in the `cipherFile` method, which utilizes the AES algorithm in CBC mode for both encryption and decryption.

The project also provides methods for generating random salt and IV, calculating SHA-256 hash, and obtaining the output file path based on the input file path and the cryptographic mode. It handles exceptions related to file loading, encryption/decryption, and file integrity verification.

![secure-file-cryptor (1)](https://github.com/nicolaspenagos/secure-file-cryptor/assets/47872252/4d78ba6d-c8da-45f1-9fd6-c9e6d54c14e3)
