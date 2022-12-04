package me.renedo.lexical.term.aplication.retrieve;

import java.util.List;

import me.renedo.lexical.term.domain.Term;
import me.renedo.lexical.term.domain.TermRepository;
import me.renedo.lexical.type.domain.Type;
import me.renedo.shared.Service;

@Service
public class TermRetriever {

    private final TermRepository termRepository;

    public TermRetriever(TermRepository termRepository) {
        this.termRepository = termRepository;
    }

    public List<Term> search(String text, Type type){
        return termRepository.search(text, type);
    }
}
