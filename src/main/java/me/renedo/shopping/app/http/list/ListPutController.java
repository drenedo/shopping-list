package me.renedo.shopping.app.http.list;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import me.renedo.shopping.app.http.V1Controller;
import me.renedo.shopping.item.application.create.ItemCreator.CreateItemRequest;
import me.renedo.shopping.list.application.create.ShoppingListCreator;
import me.renedo.shared.uuid.UUIDValidator;

@RestController
public final class ListPutController extends V1Controller {

    private final ShoppingListCreator creator;


    public ListPutController(ShoppingListCreator creator) {
        this.creator = creator;
    }

    @PutMapping("/lists/{id}")
    public ResponseEntity<String> createList(@PathVariable String id, @RequestBody ShoppingList list) {
        UUID uuid = UUIDValidator.fromString(id);
        creator.create(new ShoppingListCreator.CreateShoppingListRequest(uuid, LocalDateTime.now(), list.name(),
            list.description(), toItems(list.items(), uuid)));
        return ResponseEntity.created(URI.create(id)).body(id);
    }

    private List<CreateItemRequest> toItems( List<Item> items, UUID uuid){
        if(items == null) {
            return List.of();
        } else {
            return items.stream()
                .map(i -> new CreateItemRequest(i.id(), i.name(), i.amount(), i.unit(), i.brand(), uuid))
                .toList();
        }
    }

    record ShoppingList(String name, String description, List<Item> items) {

    }

    record Item(UUID id, String name, Integer amount, String unit, String brand) {

    }
}
