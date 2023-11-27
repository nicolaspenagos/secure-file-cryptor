package com.example.securefilecryptor.model;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

import com.example.securefilecryptor.exceptions.CipherErrorException;
import com.example.securefilecryptor.exceptions.CorruptedFileException;
import com.example.securefilecryptor.exceptions.ExceptionUtil;
import com.example.securefilecryptor.exceptions.LoadFileErrorException;
import org.apache.commons.io.FilenameUtils;

/**
 * The {@code FileEncryptor} class provides methods for encrypting and decrypting files using AES encryption.
 * It uses a combination of password-based key derivation, symmetric encryption, and file hashing for secure file handling.
 *
 * @author nicolaspenagos
 * @version 1.0
 */
public class FileEncryptor {

    // Constants for key derivation, encryption algorithm, buffer size, and file extension
    private final static int PBKDF2_ITERATIONS = 65536;
    private final static String PBKDF2_NAME = "PBKDF2WithHmacSHA256";
    private final static String ALGORITHM_NAME = "AES/CBC/PKCS5Padding";
    private final static int BUFFER_SIZE = 64;
    private final static String ENCRYPTED_EXTENSION = ".enc";

    // Singleton instance of FileEncryptor
    private static FileEncryptor instance;

    private FileEncryptor() {
    }

    /**
     * Gets the singleton instance of the {@code FileEncryptor}.
     *
     * @return The singleton instance of {@code FileEncryptor}.
     */
    public static FileEncryptor getInstance() {
        if (instance == null) {
            instance = new FileEncryptor();
        }
        return instance;
    }

    /**
     * Encrypts a file using AES encryption with a provided password.
     *
     * @param password The password used for encryption.
     * @param filePath The path to the file to be encrypted.
     * @throws LoadFileErrorException If there is an error loading the file.
     * @throws CipherErrorException   If there is an error in the encryption process.
     * @throws CorruptedFileException If the encrypted file is corrupted.
     */
    public void encryptFile(String password, String filePath) throws LoadFileErrorException, CipherErrorException, CorruptedFileException {
        try{

            String outputPath = getOutputPathFromInputPath(filePath, CryptoMode.ENCRYPT);

            FileInputStream inputStream = new FileInputStream(filePath);
            FileOutputStream outputStream = new FileOutputStream(outputPath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            String salt = generateSalt();
            SecretKey key = getKeyFromPassword(password, salt);
            IvParameterSpec iv = generateIV();
            byte[] hashSHA256 = calculateSHA256(filePath);

            objectOutputStream.writeObject(iv.getIV());
            objectOutputStream.writeObject(salt);
            objectOutputStream.writeObject(hashSHA256);

            cipherFile(key, iv, CryptoMode.ENCRYPT, inputStream, outputStream);

        } catch (IOException e) {
            throw new LoadFileErrorException();
        } catch (GeneralSecurityException e) {
            ExceptionUtil.handleSecurityException(e);
        }

    }

    /**
     * Decrypts a file using AES decryption with a provided password.
     *
     * @param password The password used for decryption.
     * @param filePath The path to the file to be decrypted.
     * @throws CorruptedFileException If the decrypted file is corrupted.
     * @throws CipherErrorException   If there is an error in the decryption process.
     * @throws LoadFileErrorException If there is an error loading the file.
     */
    public void decryptFile(String password, String filePath) throws CorruptedFileException, CipherErrorException, LoadFileErrorException {
        try{
            String outputPath = getOutputPathFromInputPath(filePath, CryptoMode.DECRYPT);

            FileInputStream inputStream = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            byte[] ivBytes = (byte[]) objectInputStream.readObject();
            String salt = (String) objectInputStream.readObject();
            byte[] originalHash = (byte[]) objectInputStream.readObject();

            SecretKey key = getKeyFromPassword(password, salt);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            FileOutputStream outputStream = new FileOutputStream(outputPath);
            cipherFile(key, iv, CryptoMode.DECRYPT, inputStream, outputStream);

            verifyHash(originalHash, calculateSHA256(outputPath));
        } catch (IOException | ClassNotFoundException e) {
            throw new LoadFileErrorException();
        } catch (GeneralSecurityException e) {
            ExceptionUtil.handleSecurityException(e);
        }

    }

    /**
     * Encrypts or decrypts a file using AES encryption/decryption with a provided key and initialization vector (IV).
     *
     * @param key          The secret key used for encryption/decryption.
     * @param iv           The initialization vector used for encryption/decryption.
     * @param mode         The cryptographic mode (ENCRYPT or DECRYPT).
     * @param inputStream  The input stream of the file to be processed.
     * @param outputStream The output stream to write the processed data.
     * @throws IOException                    If an I/O error occurs during file processing.
     * @throws NoSuchPaddingException         If the specified padding scheme is not available.
     * @throws InvalidAlgorithmParameterException If the algorithm parameters are invalid.
     * @throws InvalidKeyException            If the key is invalid.
     * @throws BadPaddingException            If the padding is invalid during decryption.
     * @throws IllegalBlockSizeException      If the block size is invalid.
     * @throws NoSuchAlgorithmException        If the specified algorithm is not available.
     */
    private void cipherFile(SecretKey key, IvParameterSpec iv, CryptoMode mode, InputStream inputStream, OutputStream outputStream) throws IOException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException {

        int cipherMode = (mode == CryptoMode.ENCRYPT) ? javax.crypto.Cipher.ENCRYPT_MODE : javax.crypto.Cipher.DECRYPT_MODE;

        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(ALGORITHM_NAME);
        cipher.init(cipherMode, key, iv);

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) {
                outputStream.write(output);
            }
        }

        byte[] outputBytes = cipher.doFinal();

        if (outputBytes != null) {
            outputStream.write(outputBytes);
        }

        inputStream.close();
        outputStream.close();
    }

    /**
     * Verifies the integrity of a file by comparing its original hash with the hash calculated after processing.
     *
     * @param originalHash The original hash of the file.
     * @param obtainedHash The hash obtained after processing the file.
     * @throws CorruptedFileException If the file integrity check fails.
     */
    private void verifyHash(byte[] originalHash, byte[] obtainedHash) throws CorruptedFileException {

        if(!Arrays.equals(originalHash, obtainedHash)){
            throw new CorruptedFileException();
        }
    }

    /**
     * Calculates the SHA-256 hash of a file.
     *
     * @param filePath The path to the file for which the hash is calculated.
     * @return The SHA-256 hash of the file.
     * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available.
     * @throws IOException              If an I/O error occurs during file processing.
     */
    private byte[] calculateSHA256(String filePath) throws NoSuchAlgorithmException, IOException {
        byte[] buffer= new byte[8192];
        int count;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath));
        while ((count = bis.read(buffer)) > 0) {
            digest.update(buffer, 0, count);
        }
        bis.close();

        byte[] hash = digest.digest();
        return hash;
    }

    /**
     * Derives a secret key from a password and salt using PBKDF2 key derivation.
     *
     * @param password The password from which the key is derived.
     * @param salt     The salt used in the key derivation.
     * @return The derived secret key.
     * @throws NoSuchAlgorithmException If the specified algorithm is not available.
     * @throws InvalidKeySpecException  If the key specification is invalid.
     */
    private SecretKey getKeyFromPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_NAME);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), PBKDF2_ITERATIONS, 256);
        SecretKey secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        return secretKey;
    }

    /**
     * Generates a random initialization vector (IV) for AES encryption.
     *
     * @return The generated IV.
     */
    private IvParameterSpec generateIV() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    /**
     * Generates a random salt for key derivation.
     *
     * @return The generated salt.
     */
    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16]; // You can choose the size of the salt
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Generates the output file path based on the input file path and the cryptographic mode.
     *
     * @param inputPath The input file path.
     * @param mode      The cryptographic mode (ENCRYPT or DECRYPT).
     * @return The output file path.
     */
    private String getOutputPathFromInputPath(String inputPath, CryptoMode mode){

        String[] pathParts = inputPath.split("/");
        String filename = pathParts[pathParts.length - 1];

        if (mode == CryptoMode.ENCRYPT) {
            filename += ENCRYPTED_EXTENSION;
        }
        if (mode == CryptoMode.DECRYPT) {
            filename = FilenameUtils.removeExtension(filename);
        }

        pathParts[pathParts.length - 1] = filename;
        String finalJoinedPath = String.join("/", pathParts);

        return finalJoinedPath;
    }

}
