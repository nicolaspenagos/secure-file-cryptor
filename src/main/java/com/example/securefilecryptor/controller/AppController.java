package com.example.securefilecryptor.controller;

import com.example.securefilecryptor.constants.Message;
import com.example.securefilecryptor.model.FileEncryptor;
import com.example.securefilecryptor.styles.ButtonStyle;
import com.example.securefilecryptor.styles.LabelStyle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class AppController {

    @FXML
    private Label pathLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label msgLabel;
    @FXML
    private Label loadFileLabel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passwordVerificationField;
    @FXML
    private ImageView imageView;
    @FXML
    private Button changeModeBtn;
    @FXML
    private Button loadBtn;
    @FXML
    private Button actionBtn;
    @FXML
    private VBox verificationBox;

    private Image lockImg;
    private Image unlockImg;

    private boolean isDecrypting;
    private String filePath;
    private String password;
    private String passwordVerification;

    private FileEncryptor fileEncryptor;

    public void initialize() {

        isDecrypting = false;

        lockImg = new Image(getClass().getResourceAsStream("/images/lock.png"));
        unlockImg = new Image(getClass().getResourceAsStream("/images/unlock.png"));
        imageView.setImage(lockImg);

        Font.loadFont(getClass().getResource("/fonts/CamingoCode-Regular.ttf").toExternalForm(), 10);

        loadBtn.setOnMouseEntered(e -> loadBtn.setStyle(ButtonStyle.HOVER.getStyle()));
        loadBtn.setOnMouseExited(e -> loadBtn.setStyle(ButtonStyle.DEFAULT.getStyle()));
        actionBtn.setOnMouseEntered(e -> actionBtn.setStyle(ButtonStyle.HOVER.getStyle()));
        actionBtn.setOnMouseExited(e -> actionBtn.setStyle(ButtonStyle.DEFAULT.getStyle()));
        changeModeBtn.setOnMouseEntered(e -> changeModeBtn.setStyle(ButtonStyle.ALT_HOVER.getStyle()));
        changeModeBtn.setOnMouseExited(e -> changeModeBtn.setStyle(ButtonStyle.ALT_DEFAULT.getStyle()));

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            password = newValue;
        });
        passwordVerificationField.textProperty().addListener((observable, oldValue, newValue) -> {
            passwordVerification = newValue;
        });

        fileEncryptor = FileEncryptor.getInstance();

    }

    @FXML
    private void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            String path = selectedFile.getAbsolutePath();
            pathLabel.setText(path);
            pathLabel.setStyle(LabelStyle.INPUT_LABEL_FILLED.getStyle());
            filePath = path;
        } else {
            pathLabel.setText(Message.PLACEHOLDER_PATH.getMessage());
            pathLabel.setStyle(LabelStyle.INPUT_LABEL_DEFAULT.getStyle());
            filePath = null;
        }
    }

    @FXML
    private void performAction() {
        hideMsg();

        if (!validateForm()) {
            return;
        }
        String errorMsg;
        try {
            if (isDecrypting) {
                fileEncryptor.decryptFile(password, filePath);
                showMsg(Message.DECRYPTION_FINISHED.getMessage(), false);
            } else {
                fileEncryptor.encryptFile(password, filePath);
                showMsg(Message.ENCRYPTION_FINISHED.getMessage(), false);
            }
        } catch (Exception e) {
            errorMsg = e.getMessage();
            showMsg(errorMsg, true);
        } finally {
            clearForm();
        }
    }

    @FXML
    private void changeMode() {
        isDecrypting = !isDecrypting;
        clearForm();
        updateGUI();
    }

    private void updateGUI() {
        hideMsg();
        if (isDecrypting) {
            titleLabel.setText("DECRYPTING");
            imageView.setImage(unlockImg);
            changeModeBtn.setText("Encrypt →");
            actionBtn.setText("Decrypt");
            loadFileLabel.setText("Select the file to decrypt:");
            verificationBox.setVisible(false);
        } else {
            titleLabel.setText("ENCRYPTING");
            imageView.setImage(lockImg);
            changeModeBtn.setText("Decrypt →");
            actionBtn.setText("Encrypt");
            loadFileLabel.setText("Select the file to encrypt:");
            verificationBox.setVisible(true);
        }
    }

    private boolean validateForm() {
        if (filePath == null || filePath.equals("")) {
            showMsg(Message.FILE_PATH_ERROR.getMessage(), true);
            return false;
        }

        if (password.length() < 8) {
            showMsg(Message.SHORT_PASSWORD.getMessage(), true);
            return false;
        }

        if (password == null || password.equals("")) {
            showMsg(Message.EMPTY_PASSWORD.getMessage(), true);
            return false;
        }

        if (!isDecrypting) {
            if (passwordVerification == null || passwordVerification.equals("")) {
                showMsg(Message.EMPTY_PASSWORD.getMessage(), true);
                return false;
            }

            if (!password.equals(passwordVerification)) {
                showMsg(Message.PASSWORD_DO_NOT_MATCH.getMessage(), true);
                return false;
            }
        }
        return true;
    }

    private void showMsg(String msgVal, boolean isError) {
        String newStyle = isError ? LabelStyle.ERROR_LABEL.getStyle() : LabelStyle.SUCESS_LABEL.getStyle();
        msgLabel.setStyle(newStyle);
        msgLabel.setText(msgVal);
    }

    private void hideMsg() {
        msgLabel.setStyle(LabelStyle.INVISIBLE_LABEL.getStyle());
    }

    private void clearForm() {
        filePath = null;
        pathLabel.setText(Message.PLACEHOLDER_PATH.getMessage());
        pathLabel.setStyle(LabelStyle.INPUT_LABEL_DEFAULT.getStyle());
        password = null;
        passwordField.setText("");
        passwordVerificationField.setText("");
        passwordVerification = null;
    }
}