package com.example.securefilecryptor.controller;

import com.example.securefilecryptor.constants.Message;
import com.example.securefilecryptor.model.FileEncryptor;
import com.example.securefilecryptor.styles.ButtonStyle;
import com.example.securefilecryptor.styles.LabelStyle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class AppController {

    @FXML
    private Label pathLabel;
    @FXML
    private Button loadBtn;
    @FXML
    private Button actionBtn;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passwordVerificationField;
    @FXML
    private Label msgLabel;

    private boolean isDecrypting;

    private String filePath;
    private String password;
    private String passwordVerification;

    private FileEncryptor fileEncryptor;

    public void initialize() {
        isDecrypting = false;

        loadBtn.setOnMouseEntered(e->loadBtn.setStyle(ButtonStyle.HOVER.getStyle()));
        loadBtn.setOnMouseExited(e->loadBtn.setStyle(ButtonStyle.DEFAULT.getStyle()));
        actionBtn.setOnMouseEntered(e->actionBtn.setStyle(ButtonStyle.HOVER.getStyle()));
        actionBtn.setOnMouseExited(e->actionBtn.setStyle(ButtonStyle.DEFAULT.getStyle()));

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            password = newValue;
        });
        passwordVerificationField.textProperty().addListener((observable, oldValue, newValue) -> {
            passwordVerification = newValue;
        });

        fileEncryptor = FileEncryptor.getInstance();

        //System.out.println(font);
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
    private void performAction(){
        hideMsg();

        //fileEncryptor.test();
        if(!validateForm()){
            return;
        }

        try{
            if(isDecrypting){

            }else{
                fileEncryptor.encryptFile(password, filePath);
                showMsg(Message.ENCRYPTION_SUCCESS.getMessage(), false);
            }
        }catch (Exception e){
            String errorMsg = isDecrypting?Message.DECRYPTION_ERROR.getMessage():Message.ENCRYPTION_ERROR.getMessage();
            System.out.println(e.getMessage());
            showMsg(errorMsg, true);
        }finally {
            clearForm();
        }

    }

    private boolean validateForm(){
        if(filePath==null||filePath.equals("")){
            showMsg(Message.FILE_PATH_ERROR.getMessage(), true);
            return false;
        }
        if(password==null|| passwordVerification ==null||password.equals("")|| passwordVerification.equals("")){
            showMsg(Message.EMPTY_PASSWORD.getMessage(), true);
            return false;
        }
        if(password.length()<8){
            showMsg(Message.SHORT_PASSWORD.getMessage(), true);
            return false;
        }
        if(!password.equals(passwordVerification)){
            showMsg(Message.PASSWORD_DO_NOT_MATCH.getMessage(), true);
            return false;
        }
        return true;
    }

    private void showMsg(String msgVal, boolean isError){
        String newStyle = isError?LabelStyle.ERROR_LABEL.getStyle() : LabelStyle.SUCESS_LABEL.getStyle();
        msgLabel.setStyle(newStyle);
        msgLabel.setText(msgVal);
    }
    private void hideMsg(){
        msgLabel.setStyle(LabelStyle.INVISIBLE_LABEL.getStyle());
    }

    private void clearForm(){
        filePath = null;
        pathLabel.setText(Message.PLACEHOLDER_PATH.getMessage());
        pathLabel.setStyle(LabelStyle.INPUT_LABEL_DEFAULT.getStyle());
        password = null;
        passwordField.setText("");
        passwordVerificationField.setText("");
        passwordVerification = null;
    }
}