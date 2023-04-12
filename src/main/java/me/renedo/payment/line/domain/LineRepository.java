package me.renedo.payment.line.domain;

import java.util.List;
import java.util.UUID;

public interface LineRepository {

    Line save(Line line, UUID receipt);

    boolean update(Line line);

    List<Line> findInReceipt(UUID receiptId);

    int delete(List<UUID> ids);
}
