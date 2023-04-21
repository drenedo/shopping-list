package me.renedo.payment.receipt.application.create;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import me.renedo.payment.line.domain.LineRepository;
import me.renedo.payment.receipt.application.retrieve.ClassifierRetriever;
import me.renedo.payment.receipt.domain.Category;
import me.renedo.payment.receipt.domain.Classifier;
import me.renedo.payment.receipt.domain.Receipt;
import me.renedo.payment.receipt.domain.ReceiptRepository;
import me.renedo.payment.receipt.domain.ocr.OcrRead;
import me.renedo.payment.receipt.domain.ocr.OcrService;
import me.renedo.shared.Service;

@Service
public class ReceiptCreator {

    private final OcrService ocrService;

    private final ReceiptRepository repository;

    private final ClassifierRetriever classifierRetriever;
    private final ClassifierCreator classifierCreator;

    private final LineRepository lineRepository;

    public ReceiptCreator(OcrService ocrService, ReceiptRepository repository, ClassifierRetriever classifierRetriever,
        ClassifierCreator classifierCreator, LineRepository lineRepository) {
        this.ocrService = ocrService;
        this.repository = repository;
        this.classifierRetriever = classifierRetriever;
        this.classifierCreator = classifierCreator;
        this.lineRepository = lineRepository;
    }

    public Receipt create(CreateReceiptRequest request){
        OcrRead read = ocrService.read(request.path());
        Category category = getCategory(read.getSite());
        Receipt receipt = read.toReceipt(request.id(), request.list(), category);
        repository.save(receipt.getReceiptWithCorrectLinesIfIsPossible());
        //FIXME only one insert not n
        receipt.getLines().forEach(l -> lineRepository.save(l, receipt.getId()));
        return receipt;
    }

    public Receipt create(UUID id, String site, Double amount, Boolean cash, String category){
        classifierCreator.saveClassifierIfIsPossible(site, Category.valueOfId(category));
        Receipt receipt = new Receipt(id, null, site, BigDecimal.valueOf(amount), site.toUpperCase(), null, null, cash,
            category == null ? getCategory(site) : Category.valueOfId(category));
        repository.save(receipt);
        return receipt;
    }

    public record CreateReceiptRequest(String path, UUID id, UUID list) {

    }

    private Category getCategory(String text) {
        if(text != null){
            Optional<Classifier> classifier = classifierRetriever.findClassifier(text);
            if(classifier.isPresent()){
                return classifier.get().getCategory();
            }
        }
        return null;
    }
}
