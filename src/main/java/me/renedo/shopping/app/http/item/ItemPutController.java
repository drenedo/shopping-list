package me.renedo.shopping.app.http.item;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import me.renedo.shopping.app.http.V1Controller;
import me.renedo.shopping.item.application.create.ItemCreator;
import me.renedo.shared.uuid.UUIDValidator;

@RestController
final class ItemPutController extends V1Controller {

    private final ItemCreator itemCreator;

    public ItemPutController(ItemCreator itemCreator) {
        this.itemCreator = itemCreator;
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<String> createItem(@PathVariable String id, @RequestBody Item item) {
        itemCreator.create(new ItemCreator.CreateItemRequest(UUIDValidator.fromString(id), item.name(), item.amount(), item.unit(), item.brand(),
            UUIDValidator.fromString(item.shoppingListId)));
        return ResponseEntity.created(URI.create(id)).body(id);
    }


    record Item(String name, Integer amount, String unit, String brand, String shoppingListId) {

    }
}
