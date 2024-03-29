package me.renedo.payment.receipt.domain;

import java.util.UUID;

import me.renedo.shared.string.TextIdentifier;

public class Classifier {
    private final UUID id;

    private final String text;

    private final Category category;

    public Classifier(String text, Category category) {
        this(UUID.randomUUID(), text, category);
    }

    public Classifier(UUID id, String text, Category category) {
        String cleanText = cleanNotDescriptiveCharacteres(text);
        this.id = id;
        this.text = cleanText;
        this.category = category;
    }

    public UUID getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Category getCategory() {
        return category;
    }

    public static String cleanNotDescriptiveCharacteres(String text) {
        return TextIdentifier.getIdentifier(text);
    }
}
