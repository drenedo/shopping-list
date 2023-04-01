package me.renedo.shopping.list.application.retrieve;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import me.renedo.shopping.item.application.retrieve.ItemRetriever;
import me.renedo.shopping.list.domain.ShoppingList;
import me.renedo.shopping.list.domain.ShoppingListRepository;
import me.renedo.shared.Service;

@Service
public class ShoppingListRetriever {

    private final ShoppingListRepository repository;

    private final ItemRetriever itemRetriever;

    public ShoppingListRetriever(ShoppingListRepository repository, ItemRetriever itemRetriever) {
        this.repository = repository;
        this.itemRetriever = itemRetriever;
    }

    public Optional<ShoppingList> rerieve(UUID id){
        //TODO make a Join
        return repository.findById(id)
            .map(list -> new ShoppingList(list, itemRetriever.retrieveListItems(list.getId())));
    }

    public List<ShoppingList> retrievePaginated(LocalDateTime localDateTime, int pageSize){
        return repository.findAllPaginate(localDateTime, pageSize);
    }
}
