package me.renedo.lexical.type.domain;

import java.util.Arrays;

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
        return Arrays.stream(Type.values())
            .filter(c -> value.charAt(0) == c.getId() || c.toString().equals(value))
            .findFirst().orElse(UNIT);
    }
}
