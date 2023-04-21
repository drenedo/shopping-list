package me.renedo.payment.receipt.domain;

import java.util.Optional;

public interface ClassifierRepository {
    Classifier save(Classifier classifier);

    Optional<Classifier> findByText(String text);

    Optional<Classifier> findByStartsWithText(String text);
}
