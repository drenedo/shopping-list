package me.renedo.payment.receipt.application.update;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import me.renedo.payment.line.domain.Line;
import me.renedo.payment.line.domain.LineRepository;
import me.renedo.payment.receipt.domain.Category;
import me.renedo.payment.receipt.domain.Receipt;
import me.renedo.payment.receipt.domain.ReceiptRepository;
import me.renedo.shared.Service;
import me.renedo.shared.exception.NotFoundException;

@Service
public class ReceiptUpdater {

    private final ReceiptRepository receiptRepository;

    private final LineRepository lineRepository;

    public ReceiptUpdater(ReceiptRepository receiptRepository, LineRepository lineRepository) {
        this.receiptRepository = receiptRepository;
        this.lineRepository = lineRepository;
    }

    public void update(UpdateReceiptRequest request) {
        Receipt receipt = request.toReceipt();
        updateReceipt(receipt.getReceiptWithCorrectLinesIfIsPossible());
        List<UUID> lines = lineRepository.findInReceipt(receipt.getId()).stream().map(Line::getId).toList();
        deleteLines(receipt, lines);
        updateLines(receipt, lines);
        createNewLines(receipt, lines);
    }

    public record UpdateReceiptRequest(UUID id, BigDecimal total, String site, String text, List<UpdateLineRequest> lines, Boolean cash,
                                       String category) {
        public Receipt toReceipt() {
            var totalVerified = lines.isEmpty() ? total : lines.stream().map(UpdateLineRequest::total).reduce(BigDecimal.ZERO, BigDecimal::add);
            return new Receipt(id, null, text, totalVerified, site, lines.stream().map(UpdateLineRequest::toLine).toList(), null, cash,
                Category.valueOfId(category));
        }
    }

    public record UpdateLineRequest(UUID id, BigDecimal total, String name) {
        public Line toLine() {
            return new Line(id, null, null, name, total, null, null);
        }
    }

    private void createNewLines(Receipt receipt, List<UUID> lines) {
        List<Line> toCreate = receipt.getLines().stream().filter(l -> !lines.contains(l.getId())).toList();
        toCreate.forEach(l -> lineRepository.save(l, receipt.getId()));
    }

    private void updateLines(Receipt receipt, List<UUID> lines) {
        List<Line> toUpdate = receipt.getLines().stream().filter(l -> lines.contains(l.getId())).toList();
        toUpdate.forEach(lineRepository::update);
    }

    private void deleteLines(Receipt receipt, List<UUID> lines) {
        List<UUID> ids = receipt.getLines().stream().map(Line::getId).toList();
        List<UUID> toDelete = lines.stream().filter(id -> !ids.contains(id)).toList();
        lineRepository.delete(toDelete);
    }

    private void updateReceipt(Receipt receipt) {
        if (!receiptRepository.update(receipt)) {
            throw new NotFoundException("Receipt not found");
        }
    }
}
