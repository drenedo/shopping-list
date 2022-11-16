package me.renedo.shopping.item.intraestructure;

import static me.renedo.shopping.shared.jooq.tables.Item.ITEM;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import me.renedo.shopping.item.domain.Item;
import me.renedo.shopping.item.domain.ItemRepository;
import me.renedo.shopping.shared.jooq.tables.records.ItemRecord;
import me.renedo.shopping.status.domain.Status;

@Repository
public class JooqItemRepository implements ItemRepository {

    private final DSLContext context;

    public JooqItemRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public Item save(Item item) {
        ItemRecord itemRecord = context.newRecord(ITEM);
        itemRecord.setId(item.getId());
        itemRecord.setName(item.getName());
        itemRecord.setAmount(item.getAmount());
        itemRecord.setUnit(item.getUnit());
        itemRecord.setList(item.getListId());
        itemRecord.setStatus(String.valueOf(item.getStatus().getId()));
        itemRecord.store();
        return toItem(itemRecord);
    }

    @Override
    public void delete(UUID id){
        context.deleteFrom(ITEM)
            .where(ITEM.ID.eq(id))
            .execute();
    }

    @Override
    public List<Item> findInList(UUID id) {
        return context.selectFrom(ITEM)
            .where(ITEM.LIST.eq(id))
            .fetch()
            .map(this::toItem);
    }

    @Override
    public Optional<Item> findById(UUID id) {
        return context.selectFrom(ITEM)
            .where(ITEM.ID.eq(id))
            .fetchOptional()
            .map(this::toItem);
    }

    private Item toItem(ItemRecord itemRecord) {
        return new Item(itemRecord.getId(), itemRecord.getName(), itemRecord.getAmount(), itemRecord.getUnit(), itemRecord.getList(),
            Status.valueOfId(itemRecord.getStatus()));
    }
}
