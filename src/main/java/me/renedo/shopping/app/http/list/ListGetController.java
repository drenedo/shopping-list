package me.renedo.shopping.app.http.list;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.renedo.shopping.app.http.V1Controller;
import me.renedo.shopping.app.http.error.exception.NotFoundException;
import me.renedo.shopping.item.domain.Item;
import me.renedo.shopping.list.application.retrieve.ShoppingListRetriever;
import me.renedo.shopping.list.domain.ShoppingList;
import me.renedo.shopping.shared.uuid.UUIDValidator;

@RestController
public class ListGetController extends V1Controller {

    private final ShoppingListRetriever retriever;

    public ListGetController(ShoppingListRetriever retriever) {
        this.retriever = retriever;
    }

    @GetMapping("/lists/size/{size}")
    private List<ShoppingListResponse> paginate(@PathVariable Integer size, @RequestParam("date-time") Optional<LocalDateTime> dateTime) {
        return retriever.rerievePaginated(dateTime.orElse(null), size).stream()
            .map(ShoppingListResponse::new).collect(Collectors.toList());
    }

    @GetMapping("/lists/{id}")
    private ResponseEntity<ShoppingListResponse> getList(@PathVariable String id) {
        return retriever.rerieve(UUIDValidator.fromString(id))
            .map(ShoppingListResponse::new)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new NotFoundException("list not found"));
    }

    record ShoppingListResponse(UUID id, String name, String description, List<ItemResponse> items) {
        ShoppingListResponse(ShoppingList list) {
            this(list.getId(), list.getName(), list.getDescription(), toItemResponses(list.getItems()));
        }

        private static List<ItemResponse> toItemResponses(List<Item> items){
            if(items == null){
                return null;
            }
            return items.stream()
                .map(ItemResponse::new)
                .collect(Collectors.toList());
        }
    }

    record ItemResponse(UUID id, String name, Integer amount, String unit) {
        ItemResponse(Item item) {
            this(item.getId(), item.getName(), item.getAmount(), item.getUnit());
        }
    }
}
