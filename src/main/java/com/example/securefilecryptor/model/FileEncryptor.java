package com.example.securefilecryptor.model;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import org.apache.commons.io.FilenameUtils;

public class FileEncryptor {

    private final static int PBKDF2_ITERATIONS = 65536;
    private final static String PBKDF2_NAME = "PBKDF2WithHmacSHA256";
    private final static String ALGORITHM_NAME = "AES/CBC/PKCS5Padding";
    private final static int BUFFER_SIZE = 64;
    private final static String ENCRYPTED_EXTENSION = ".enc";

    private static FileEncryptor instance;

    private FileEncryptor() {
    }

    public static FileEncryptor getInstance() {
        if (instance == null) {
            instance = new FileEncryptor();
        }
        return instance;
    }

    public void test(){
        try {
            encryptFile("SecretMsg.txt", "/Users/nicolaspenagos/Desktop/SecretMsg.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
    public void encryptFile(String password, String filePath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

        String outputPath = getOutputPathFromInputPath(filePath, CryptoMode.ENCRYPT);

        FileInputStream inputStream = new FileInputStream(filePath);
        FileOutputStream outputStream = new FileOutputStream(outputPath);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        String salt = generateSalt();
        SecretKey key = getKeyFromPassword(password, salt);
        IvParameterSpec iv = generateIV();

        objectOutputStream.writeObject(iv.getIV());
        objectOutputStream.writeObject(salt);

        cipherFile(key, iv, CryptoMode.ENCRYPT, inputStream, outputStream);
    }

    public void decryptFile(String password, String filePath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, ClassNotFoundException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

        String outputPath = getOutputPathFromInputPath(filePath, CryptoMode.DECRYPT);

        FileInputStream inputStream = new FileInputStream(filePath);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        byte[] ivBytes = (byte[]) objectInputStream.readObject();
        String salt = (String) objectInputStream.readObject();

        SecretKey key = getKeyFromPassword(password, salt);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        FileOutputStream outputStream = new FileOutputStream(outputPath);
        cipherFile(key, iv, CryptoMode.DECRYPT, inputStream, outputStream);

    }

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

    private SecretKey getKeyFromPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_NAME);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), PBKDF2_ITERATIONS, 256);
        SecretKey secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        return secretKey;
    }

    private IvParameterSpec generateIV() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16]; // You can choose the size of the salt
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

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
