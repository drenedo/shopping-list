package me.renedo.shared.date;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class ISOFormatter {

    private static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public static String format(LocalDateTime localDateTime){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN);
        return simpleDateFormat.format(Timestamp.valueOf(localDateTime));
    }
}
