package me.renedo.lexical.app.http.term;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import me.renedo.app.http.V1Controller;
import me.renedo.lexical.term.aplication.retrieve.TermRetriever;
import me.renedo.lexical.term.domain.Term;
import me.renedo.lexical.type.domain.Type;

@RestController
public class TermGetController extends V1Controller {

    private final TermRetriever termRetriever;

    public TermGetController(TermRetriever termRetriever) {
        this.termRetriever = termRetriever;
    }

    @GetMapping("/terms/search/{text}/type/{type}")
    public List<TermResponse> paginate(@PathVariable String text, @PathVariable String type) {
        return termRetriever.search(text, Type.valueOfId(type)).stream().map(TermResponse::new).toList();
    }

    record TermResponse(String name) {
        TermResponse(Term term) {
            this(term.getName());
        }
    }

}
