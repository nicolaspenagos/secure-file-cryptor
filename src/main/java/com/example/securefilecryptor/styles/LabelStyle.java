package com.example.securefilecryptor.styles;

public enum LabelStyle {
    INPUT_LABEL_DEFAULT("-fx-translate-y: 12; -fx-translate-x: 10; -fx-text-fill: #979797;"),
    INPUT_LABEL_FILLED("-fx-translate-y: 12; -fx-translate-x: 10; -fx-text-fill: #161616;"),
    ERROR_LABEL("-fx-font-family: 'CamingoCode'; -fx-font-size: 12; -fx-text-fill:#E64A4A;"),
    SUCESS_LABEL("-fx-font-family: 'CamingoCode'; -fx-font-size: 12; -fx-text-fill:#64C238;"),
    INVISIBLE_LABEL("-fx-text-fill:white");

    private final String style;
    LabelStyle(String style) {
        this.style = style;
    }
    public String getStyle() {
        return style;
    }
}