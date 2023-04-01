package me.renedo.payment.app.http.receipt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Base64;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import me.renedo.app.http.V1Controller;
import me.renedo.payment.receipt.application.create.ReceiptCreator;
import me.renedo.payment.receipt.domain.Receipt;
import me.renedo.shared.uuid.UUIDValidator;

@RestController
public class ReceiptPutController extends V1Controller {

    private final ReceiptCreator receiptCreator;

    public ReceiptPutController(ReceiptCreator receiptCreator) {
        this.receiptCreator = receiptCreator;
    }

    @PutMapping("/receipt/{id}")
    public ResponseEntity<Receipt> createItem(@PathVariable String id, @RequestBody Image image) throws IOException {
        File imageFile = createImageFile(image);
        Receipt receipt;
        try {
            receipt = receiptCreator.create(new ReceiptCreator.CreateReceiptRequest(imageFile.getPath(), UUIDValidator.fromString(id), null));
        } finally {
            deleteImageFile(imageFile);
        }
        return ResponseEntity.created(URI.create(id)).body(receipt);
    }

    private static void deleteImageFile(File file){
        file.delete();
    }

    private static File createImageFile(Image image) throws IOException {
        byte[] imageByte = image.content();
        File imageFile = File.createTempFile(LocalDateTime.now().toString(), image.name());
        new FileOutputStream(imageFile).write(imageByte);
        return imageFile;
    }

    record Image(String image, String name) {
        public byte[] content(){
            return Base64.getDecoder().decode(image);
        }
    }
}
