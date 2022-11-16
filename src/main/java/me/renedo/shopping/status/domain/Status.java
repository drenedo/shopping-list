package me.renedo.shopping.status.domain;

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
        return switch (value){
            case "A" -> ACTIVE;
            case "C" -> CANCELED;
            default -> INACTIVE;
        };
    }
}
