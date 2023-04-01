package me.renedo.payment.receipt.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReceiptRepository {

    Optional<Receipt> findById(UUID id);

    Receipt save(Receipt receipt);

    List<Receipt> findAllPaginate(LocalDateTime time, int pageSize);

    int delete(UUID id);
}
