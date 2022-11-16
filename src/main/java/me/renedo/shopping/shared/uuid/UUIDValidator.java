package me.renedo.shopping.shared.uuid;

import java.util.UUID;

public final class UUIDValidator {

    public static UUID fromString(String text){
        if(text == null) {
            throw new NotAcceptableUUIDException("UUID could not be null");
        }
        try {
            return UUID.fromString(text);
        }catch (IllegalArgumentException iae){
            throw new NotAcceptableUUIDException(iae.getMessage());
        }
    }
}
