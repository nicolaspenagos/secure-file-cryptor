module com.example.securefilecryptor {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.codec;
    requires org.bouncycastle.provider;
    requires org.apache.commons.io;

    exports com.example.securefilecryptor.model;
    opens com.example.securefilecryptor.model to javafx.fxml;
    exports com.example.securefilecryptor.controller;
    opens com.example.securefilecryptor.controller to javafx.fxml;
    exports com.example.securefilecryptor;
    opens com.example.securefilecryptor to javafx.fxml;


}