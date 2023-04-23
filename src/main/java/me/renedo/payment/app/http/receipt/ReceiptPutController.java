package me.renedo.payment.app.http.receipt;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.twelvemonkeys.contrib.exif.EXIFUtilities;
import me.renedo.app.http.V1Controller;
import me.renedo.payment.app.http.receipt.record.ReceiptDetailResponse;
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
    public ResponseEntity<ReceiptDetailResponse> createItem(@PathVariable String id, @RequestBody Image image) throws IOException {
        File imageFile = createImageFile(image);
        Receipt receipt;
        try {
            receipt = receiptCreator.create(new ReceiptCreator.CreateReceiptRequest(imageFile.getPath(), UUIDValidator.fromString(id), null));
        } finally {
            deleteImageFile(imageFile);
        }
        return ResponseEntity.created(URI.create(id)).body(new ReceiptDetailResponse(receipt));
    }

    @PutMapping("/receipt/simple/{id}")
    public ResponseEntity<ReceiptDetailResponse> createItem(@PathVariable String id, @RequestBody SimpleReceiptRequest receipt) {
        return ResponseEntity.created(URI.create(id))
            .body(new ReceiptDetailResponse(receiptCreator.create(UUIDValidator.fromString(id), receipt.site, receipt.total(), receipt.cash, receipt.category)));
    }

    private static void deleteImageFile(File file){
        file.delete();
    }

    private static File createImageFile(Image image) throws IOException {
        byte[] imageByte = image.content();
        BufferedImage bufferedImage = scale(getBufferedImage(imageByte));
        File imageFile = File.createTempFile(LocalDateTime.now().toString(), image.name());
        ImageIO.write(bufferedImage, "jpg", imageFile);
        return imageFile;
    }

    private static BufferedImage scale(BufferedImage source){
        int w = source.getWidth();
        int h = source.getHeight();
        if(w > 1200){
            BufferedImage scaleImage = new BufferedImage(1200, h * 1200 / w, source.getType());
            Graphics2D g = scaleImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(source, 0, 0, 1200, h * 1200 / w, 0, 0, w, h, null);
            g.dispose();
            return scaleImage;
        } else {
            return source;
        }
    }

    private static BufferedImage getBufferedImage(byte[] imageByte) throws IOException {
        return (BufferedImage) EXIFUtilities.readWithOrientation(new ByteArrayInputStream(imageByte)).getRenderedImage();
    }

    record Image(String image, String name) {
        public byte[] content(){
            return Base64.getDecoder().decode(image);
        }
    }

    record SimpleReceiptRequest(String site, Double total, Boolean cash, String category) {

    }
}
