package me.renedo.payment.app.http.line;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import me.renedo.app.http.V1Controller;
import me.renedo.payment.line.application.retrieve.LineRetriever;
import me.renedo.payment.line.domain.LinePrice;
import me.renedo.shared.date.ISOFormatter;

@RestController
public class LineGetController extends V1Controller {

    private final LineRetriever retriever;


    public LineGetController(LineRetriever retriever) {
        this.retriever = retriever;
    }

    @GetMapping("/line/search/{text}")
    public List<LinePriceResponse> search(@PathVariable String text) {
        return retriever.search(text).stream()
            .map(LinePriceResponse::new)
            .toList();
    }

    public record LinePriceResponse(UUID id, String name, String site, BigDecimal total, String created) {
        public LinePriceResponse(LinePrice line) {
            this(line.getId(), line.getName(), line.getSite(), line.getTotal(), ISOFormatter.format(line.getCreated()));
        }
    }
}
