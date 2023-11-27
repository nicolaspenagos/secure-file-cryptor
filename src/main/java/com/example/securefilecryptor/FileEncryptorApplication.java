package com.example.securefilecryptor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class FileEncryptorApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {


        Font.loadFont(getClass().getResource("/fonts/Chivo-Regular.ttf").toExternalForm(), 10);

        FXMLLoader fxmlLoader = new FXMLLoader(FileEncryptorApplication.class.getResource("app-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        stage.setTitle("Secure File Cryptor");
        stage.setScene(scene);
        stage.setResizable(false);

        stage.show();
    }

    public static void main(String[] args) {
        launch();

        /*
        FileEncryptor fileEncryptor = FileEncryptor.getInstance();

        try {
          fileEncryptor.encryptFile("dummy_password", "/Users/nicolaspenagos/Desktop/image.jpeg");
          fileEncryptor.decryptFile("dummy_password", "/Users/nicolaspenagos/Desktop/image.jpeg.enc");
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    */
    }

}