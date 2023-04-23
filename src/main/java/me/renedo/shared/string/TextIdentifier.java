package me.renedo.shared.string;

import java.text.Normalizer;

public class TextIdentifier {
    public static String getIdentifier(String text) {
        return Normalizer.normalize(text.toLowerCase(), Normalizer.Form.NFKD)
            .replaceAll("[\\., \\-_]", "")
            .replaceAll("\\p{M}", "");
    }
}
