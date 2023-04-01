package me.renedo.payment.receipt.application.delete;

import java.util.List;
import java.util.UUID;

import me.renedo.shared.exception.NotFoundException;
import me.renedo.payment.line.domain.Line;
import me.renedo.payment.line.domain.LineRepository;
import me.renedo.payment.receipt.domain.ReceiptRepository;
import me.renedo.shared.Service;

@Service
public class ReceiptEraser {

    private final ReceiptRepository repository;

    private final LineRepository lineRepository;

    public ReceiptEraser(ReceiptRepository repository, LineRepository lineRepository) {
        this.repository = repository;
        this.lineRepository = lineRepository;
    }

    public void delete(UUID id) {
        List<Line> inReceipt = lineRepository.findInReceipt(id);
        int linesDeleted = lineRepository.delete(inReceipt.stream().map(Line::getId).toList());
        if(linesDeleted != inReceipt.size()){
            throw new RuntimeException("Error deleteting lines");
        }
        int deleted = repository.delete(id);
        if(deleted <= 0){
            throw new NotFoundException("No receipt found");
        }
    }
}
