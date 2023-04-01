package me.renedo.shopping.list.infraestructure;

import static me.renedo.shopping.shared.jooq.tables.ShoppingList.SHOPPING_LIST;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import me.renedo.shopping.list.domain.ShoppingList;
import me.renedo.shopping.list.domain.ShoppingListRepository;
import me.renedo.shopping.shared.jooq.tables.records.ShoppingListRecord;
import me.renedo.shopping.status.domain.Status;

@Repository
public class JooqShoppingListRepository implements ShoppingListRepository {

    private final DSLContext context;

    public JooqShoppingListRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public ShoppingList save(ShoppingList list) {
        context.insertInto(SHOPPING_LIST,
                SHOPPING_LIST.ID, SHOPPING_LIST.NAME, SHOPPING_LIST.DESCRIPTION, SHOPPING_LIST.DATETIME, SHOPPING_LIST.STATUS)
            .values(list.getId(), list.getName(), list.getDescription(), list.getDateTime(), String.valueOf(list.getStatus().getId()))
            .execute();
        return list;
    }

    @Override
    public List<ShoppingList> findAllPaginate(LocalDateTime time, int pageSize){
        LocalDateTime notNullTime = time == null ? LocalDateTime.now() : time;
        return context.selectFrom(SHOPPING_LIST)
            .orderBy(SHOPPING_LIST.DATETIME.desc())
            .seek(notNullTime)
            .limit(pageSize)
            .fetch()
            .map(this::toShoppingList);
    }

    @Override
    public Optional<ShoppingList> findById(UUID id) {
        return context.selectFrom(SHOPPING_LIST)
            .where(SHOPPING_LIST.ID.eq(id))
            .fetchOptional()
            .map(this::toShoppingList);
    }

    @Override
    public void delete(UUID id) {
        //TODO return int
        context.deleteFrom(SHOPPING_LIST)
            .where(SHOPPING_LIST.ID.eq(id))
            .execute();
    }

    @Override
    public void updateStatus(UUID id, Status status) {
        context.update(SHOPPING_LIST)
            .set(SHOPPING_LIST.STATUS, String.valueOf(status.getId()))
            .where(SHOPPING_LIST.ID.eq(id)).execute();
    }

    private ShoppingList toShoppingList(ShoppingListRecord slRecord) {
        return new ShoppingList(slRecord.getId(), slRecord.getDatetime(), slRecord.getName(), slRecord.getDescription(), null,
            Status.valueOfId(slRecord.getStatus()));
    }
}
