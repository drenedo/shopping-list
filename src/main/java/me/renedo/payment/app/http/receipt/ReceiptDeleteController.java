package me.renedo.payment.app.http.receipt;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import me.renedo.app.http.V1Controller;
import me.renedo.payment.receipt.application.delete.ReceiptEraser;
import me.renedo.shared.uuid.UUIDValidator;

@RestController
public class ReceiptDeleteController extends V1Controller {

    private final ReceiptEraser eraser;

    public ReceiptDeleteController(ReceiptEraser eraser) {
        this.eraser = eraser;
    }

    @DeleteMapping("/receipts/{id}")
    public void paginate(@PathVariable String id){
        eraser.delete(UUIDValidator.fromString(id));
    }
}
