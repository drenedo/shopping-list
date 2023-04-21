package me.renedo.payment.receipt.application.retrieve;

import java.util.Optional;

import me.renedo.payment.receipt.domain.Classifier;
import me.renedo.payment.receipt.domain.ClassifierRepository;
import me.renedo.shared.Service;

@Service
public class ClassifierRetriever {
    private final ClassifierRepository repository;

    public ClassifierRetriever(ClassifierRepository repository) {
        this.repository = repository;
    }

    public Optional<Classifier> findClassifier(String text){
        return Optional.ofNullable(repository.findByText(text).orElseGet(() -> repository.findByStartsWithText(text).orElse(null)));
    }
}
