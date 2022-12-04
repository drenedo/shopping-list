package me.renedo.shopping.app.http.list;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import me.renedo.app.http.V1Controller;
import me.renedo.shopping.list.application.delete.ShoppingListEraser;
import me.renedo.shared.uuid.UUIDValidator;

@RestController
public class ListDeleteController extends V1Controller {

    private final ShoppingListEraser eraser;

    public ListDeleteController(ShoppingListEraser shoppingListEraser) {
        this.eraser = shoppingListEraser;
    }

    @DeleteMapping("/lists/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteItem(@PathVariable String id) {
        eraser.delete(UUIDValidator.fromString(id));
    }
}
