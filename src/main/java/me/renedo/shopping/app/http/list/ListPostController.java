package me.renedo.shopping.app.http.list;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import me.renedo.app.http.V1Controller;
import me.renedo.shopping.list.application.update.ShoppingListUpdater;
import me.renedo.shared.uuid.UUIDValidator;
import me.renedo.shopping.status.domain.Status;

@RestController
public class ListPostController extends V1Controller {

    private final ShoppingListUpdater shoppingListUpdater;

    public ListPostController(ShoppingListUpdater shoppingListUpdater) {
        this.shoppingListUpdater = shoppingListUpdater;
    }


    @PostMapping("/lists/{id}/status/{status}")
    public ResponseEntity<String> createItem(@PathVariable String id, @PathVariable String status) {
        shoppingListUpdater.updateStatus(UUIDValidator.fromString(id), Status.valueOfId(status));
        return ResponseEntity.created(URI.create(id)).build();
    }
}
