package me.renedo.payment.line.application.retrieve;

import java.util.List;

import me.renedo.payment.line.domain.LinePrice;
import me.renedo.payment.line.domain.LineRepository;
import me.renedo.shared.Service;

@Service
public class LineRetriever {
    private final LineRepository lineRepository;

    public LineRetriever(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public List<LinePrice> search(String text) {
        return lineRepository.search(text);
    }
}
