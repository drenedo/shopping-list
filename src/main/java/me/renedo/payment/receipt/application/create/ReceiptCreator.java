package me.renedo.payment.receipt.application.create;

import java.math.BigDecimal;
import java.util.UUID;

import me.renedo.payment.line.domain.LineRepository;
import me.renedo.payment.receipt.domain.Receipt;
import me.renedo.payment.receipt.domain.ReceiptRepository;
import me.renedo.payment.receipt.domain.ocr.OcrRead;
import me.renedo.payment.receipt.domain.ocr.OcrService;
import me.renedo.shared.Service;

@Service
public class ReceiptCreator {

    private final OcrService ocrService;

    private final ReceiptRepository repository;

    private final LineRepository lineRepository;

    public ReceiptCreator(OcrService ocrService, ReceiptRepository repository, LineRepository lineRepository) {
        this.ocrService = ocrService;
        this.repository = repository;
        this.lineRepository = lineRepository;
    }

    public Receipt create(CreateReceiptRequest request){
        OcrRead read = ocrService.read(request.path());
        Receipt receipt = read.toReceipt(request.id(), request.list());
        repository.save(receipt.getReceiptWithCorrectLinesIfIsPossible());
        //FIXME only one insert not n
        receipt.getLines().forEach(l -> lineRepository.save(l, receipt.getId()));
        return receipt;
    }

    public Receipt create(UUID id, String site, Double amount){
        Receipt receipt = new Receipt(id, null, site, BigDecimal.valueOf(amount), site.toUpperCase(), null, null, null);
        repository.save(receipt);
        return receipt;
    }

    public record CreateReceiptRequest(String path, UUID id, UUID list) {

    }
}
