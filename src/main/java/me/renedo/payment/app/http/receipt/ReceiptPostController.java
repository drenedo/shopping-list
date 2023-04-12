package me.renedo.payment.app.http.receipt;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import me.renedo.app.http.V1Controller;
import me.renedo.payment.receipt.application.update.ReceiptUpdater;

@RestController
public class ReceiptPostController extends V1Controller {

    private final ReceiptUpdater updater;

    public ReceiptPostController(ReceiptUpdater updater) {
        this.updater = updater;
    }


    @PostMapping("/receipts/update")
    public void createItem(@RequestBody ReceiptUpdater.UpdateReceiptRequest receipt) {
        updater.update(receipt);
    }
}
