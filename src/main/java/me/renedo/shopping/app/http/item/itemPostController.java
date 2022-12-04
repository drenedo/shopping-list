package me.renedo.shopping.app.http.item;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import me.renedo.shopping.app.http.V1Controller;
import me.renedo.shopping.item.application.update.ItemUpdater;
import me.renedo.shared.uuid.UUIDValidator;
import me.renedo.shopping.status.domain.Status;

@RestController
public class itemPostController extends V1Controller {

    private final ItemUpdater itemUpdater;

    public itemPostController(ItemUpdater itemUpdater) {
        this.itemUpdater = itemUpdater;
    }

    @PostMapping("/items/{id}/status/{status}")
    public ResponseEntity<String> createItem(@PathVariable String id, @PathVariable String status) {
        itemUpdater.updateStatus(UUIDValidator.fromString(id), Status.valueOfId(status));
        return ResponseEntity.created(URI.create(id)).build();
    }
}
