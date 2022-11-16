package me.renedo.shopping.item.application.retrieve;

import java.util.List;
import java.util.UUID;

import me.renedo.shopping.item.domain.Item;
import me.renedo.shopping.item.intraestructure.JooqItemRepository;
import me.renedo.shopping.shared.Service;

@Service
public class ItemRetriever {

    private final JooqItemRepository repository;

    public ItemRetriever(JooqItemRepository repository) {
        this.repository = repository;
    }

    public List<Item> retrieveListItems (UUID id) {
        return repository.findInList(id);
    }
}
