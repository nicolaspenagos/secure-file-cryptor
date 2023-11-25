module com.example.securefilecryptor {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.codec;
    requires org.bouncycastle.provider;


    opens com.example.securefilecryptor to javafx.fxml;
    exports com.example.securefilecryptor;
    exports com.example.securefilecryptor.model;
    opens com.example.securefilecryptor.model to javafx.fxml;
}