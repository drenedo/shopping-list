package me.renedo.lexical.type.domain;

public enum Type {
    PRODUCT('P'), BRAND('B'), UNIT('U');

    private final char id;

    Type(char id) {
        this.id = id;
    }

    public char getId() {
        return id;
    }

    public static Type valueOfId(String value){
        return switch (value){
            case "P", "PRODUCT" -> PRODUCT;
            case "B", "BRAND" -> BRAND;
            default -> UNIT;
        };
    }
}
