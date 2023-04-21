package me.renedo.payment.receipt.application.create;

import me.renedo.payment.receipt.domain.Category;
import me.renedo.payment.receipt.domain.Classifier;
import me.renedo.payment.receipt.domain.ClassifierRepository;
import me.renedo.shared.Service;

@Service
public class ClassifierCreator {
    private final ClassifierRepository repository;

    public ClassifierCreator(ClassifierRepository repository) {
        this.repository = repository;
    }

    public Classifier create(Classifier classifier){
        return repository.save(classifier);
    }

    public void saveClassifierIfIsPossible(String text, Category category) {
        if(category != null && repository.findByStartsWithText(text).isEmpty()){
            repository.save(new Classifier(text, category));
        }
    }
}
