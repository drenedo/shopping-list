package me.renedo.shopping.item.application.delete;

import java.util.UUID;

import me.renedo.shopping.item.domain.ItemRepository;
import me.renedo.shopping.shared.Service;

@Service
public class ItemEraser {

    private final ItemRepository itemRepository;

    public ItemEraser(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void delete(UUID id){
        itemRepository.delete(id);
    }
}
