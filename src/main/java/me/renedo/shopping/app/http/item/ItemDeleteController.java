package me.renedo.shopping.app.http.item;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import me.renedo.shopping.app.http.V1Controller;
import me.renedo.shopping.item.application.delete.ItemEraser;
import me.renedo.shopping.shared.uuid.UUIDValidator;

@RestController
public class ItemDeleteController extends V1Controller {

    private final ItemEraser eraser;

    public ItemDeleteController(ItemEraser itemCreator) {
        this.eraser = itemCreator;
    }

    @DeleteMapping("/items/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void createItem(@PathVariable String id) {
        eraser.delete(UUIDValidator.fromString(id));
    }
}
