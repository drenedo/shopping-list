package me.renedo.shopping.status.domain;

import java.util.Arrays;

public enum Status {
    ACTIVE('A'), CANCELED('C'), INACTIVE('I');

    private final char id;

    Status(char id) {
        this.id = id;
    }

    public char getId() {
        return id;
    }

    public static Status valueOfId(String value){
        return Arrays.stream(Status.values())
            .filter(c -> value.charAt(0) == c.getId() || c.toString().equals(value))
            .findFirst().orElse(INACTIVE);
    }
}
