package me.renedo.lexical.term.aplication.create;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.event.EventListener;

import me.renedo.lexical.term.domain.Term;
import me.renedo.lexical.term.domain.TermRepository;
import me.renedo.lexical.type.domain.Type;
import me.renedo.shared.Service;
import me.renedo.shopping.item.domain.event.ItemCreatedEvent;

@Service
public class TermCreator {
    private static final Logger logger = LogManager.getLogger();

    private final TermRepository termRepository;

    public TermCreator(TermRepository termRepository) {
        this.termRepository = termRepository;
    }

    @EventListener
    public void on(ItemCreatedEvent itemCreatedEvent){
        logger.info("Item created: {}", itemCreatedEvent);
        createTermOrSumIfExits(itemCreatedEvent.getName(), Type.PRODUCT);
        createTermOrSumIfExits(itemCreatedEvent.getUnit(), Type.UNIT);
        createTermOrSumIfExits(itemCreatedEvent.getBrand(), Type.BRAND);
    }

    private void createTermOrSumIfExits(String term, Type type) {
        if (term != null && !term.isEmpty()){
            termRepository.find(term, type).ifPresentOrElse(this::sumToTerm, () -> createItemTerm(term, type));
        }
    }

    private void createItemTerm(String name, Type type) {
        termRepository.save(new Term(name, type));
    }

    private void sumToTerm(Term term) {
        if(termRepository.update(term.getId(), term.getUpdated(), term.getTimes() + 1) == 0){
            //We try again to update the value because optimistic locking
            if(retrySumToTerm(term) == 0){
                logger.error("Error on update term {}", term);
            }
        }
    }

    private int retrySumToTerm(Term failedTerm) {
        return termRepository.find(failedTerm.getId())
            .map(term -> termRepository.update(term.getId(), term.getUpdated(), term.getTimes() + 1))
            .orElse(0);
    }
}
