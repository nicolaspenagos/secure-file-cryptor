package com.example.securefilecryptor.styles;

public enum ButtonStyle {
    DEFAULT("-fx-background-color: #0D62FE; -fx-background-radius: 0; -fx-pref-height: 40; -fx-translate-x: 5; -fx-pref-width: 85; -fx-text-fill: #fff; -fx-cursor: hand; -fx-font-family: CamingoCode;"),
    HOVER("-fx-background-color: #0448C4; -fx-background-radius: 0; -fx-pref-height: 40; -fx-translate-x: 5; -fx-pref-width: 85; -fx-text-fill: #fff; -fx-cursor: hand; -fx-font-family: CamingoCode;");


    private final String style;

    ButtonStyle(String style) {
        this.style = style;
    }

    public String getStyle() {
        return style;
    }
}
