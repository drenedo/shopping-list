package me.renedo.payment.receipt.infraestructure;

import static java.util.Optional.ofNullable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import me.renedo.payment.receipt.domain.ocr.OcrLine;
import me.renedo.payment.receipt.domain.ocr.OcrRead;
import me.renedo.payment.receipt.domain.ocr.OcrService;
import me.renedo.shared.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.AnalyzeDocumentRequest;
import software.amazon.awssdk.services.textract.model.AnalyzeDocumentResponse;
import software.amazon.awssdk.services.textract.model.Block;
import software.amazon.awssdk.services.textract.model.BlockType;
import software.amazon.awssdk.services.textract.model.Document;
import software.amazon.awssdk.services.textract.model.FeatureType;

@Service
public class TextractService implements OcrService {

    private final TextractClient textractClient;

    public TextractService() {
        textractClient = TextractClient.builder()
            .region(Region.EU_WEST_1)
            .credentialsProvider(ProfileCredentialsProvider.create())
            .build();
    }

    @Override
    public OcrRead read(String path) {
        Document myDoc = getDocument(path);

        List<FeatureType> featureTypes = new ArrayList();
        featureTypes.add(FeatureType.FORMS);

        AnalyzeDocumentRequest analyzeDocumentRequest = AnalyzeDocumentRequest.builder()
            .featureTypes(featureTypes)
            .document(myDoc)
            .build();

        AnalyzeDocumentResponse analyzeDocument = textractClient.analyzeDocument(analyzeDocumentRequest);
        List<Block> blocks = analyzeDocument.blocks().stream().filter(b -> b.blockType().equals(BlockType.LINE)).toList();

        Optional<Block> totalLine = blocks.stream().filter(this::isTotalLine).findFirst();
        BigDecimal total = getTotal(totalLine, blocks);
        List<Block> previousBlocks = totalLine.map(b -> blocks.subList(0,blocks.indexOf(b))).orElse(blocks);
        return new OcrRead(getSite(blocks), total, getOcrLines(previousBlocks, total), null, getIsInCash(blocks),
            previousBlocks.stream().map(Block::text).collect(Collectors.joining("\n")));
    }


    private Boolean getIsInCash(List<Block> blocks) {
        String lowerText = blocks.stream().map(Block::text).collect(Collectors.joining("\n")).toLowerCase();
        if ((lowerText.contains("efectivo") || lowerText.contains("entregado") || lowerText.contains("contado")) &&
            (!lowerText.contains("tarjeta") && !lowerText.contains("bancaria"))) {
            return true;
        } else if ((lowerText.contains("tarjeta") || lowerText.contains("bancaria")) &&
            (!lowerText.contains("efectivo") && !lowerText.contains("entregado") && !lowerText.contains("contado"))) {
            return false;
        } else {
            return null;
        }
    }

    private BigDecimal getAmountOfLine(Block block) {
        BigDecimal amount = getRealAmountOfLine(block);
        return amount != null ? amount : extractAmountFromComplexBlock(block);
    }

    private BigDecimal extractAmountFromComplexBlock(Block block) {
        String[] words = block.text().replaceAll("[\\.,_/]", " ").split(" ");
        String last = words.length >= 1 ? words[words.length - 1] : null;
        String previous = words.length >= 2 ? words[words.length - 2] : null;
        String prePrevious = words.length >= 3 ? words[words.length - 3] : null;
        if (last != null && previous != null && last.matches("\\d\\d")) {
            return new BigDecimal((previous.matches("\\d?\\d") ? previous : 0) + "." + last);
        } else if (prePrevious != null && previous != null && previous.matches("\\d\\d")) {
            return new BigDecimal((prePrevious.matches("\\d?\\d") ? prePrevious : 0) + "." + previous);
        } else {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal getRealAmountOfLine(Block block) {
        try {
            return BigDecimal.valueOf(Double.parseDouble(block.text().replaceAll(",", ".")));
        } catch (NumberFormatException nfe) {
            return null;
        }
    }


    private BigDecimal getTotal(Optional<Block> totalLine, List<Block> blocks) {
        BigDecimal total = totalLine.map(this::getAmountOfLine).orElse(null);
        if ((total == null || total.doubleValue() <= 0) && totalLine.isPresent()) {
            BigDecimal nextLine =  getNextLine(blocks, totalLine.get()).map(this::getAmountOfLine).orElse(null);
            if(nextLine.doubleValue() <=0){
                return getPreviousLine(blocks, totalLine.get()).map(this::getAmountOfLine).orElse(null);
            } else {
                return nextLine;
            }
        } else {
            return total;
        }
    }

    private Optional<Block> getNextLine(List<Block> blocks, Block totalLine) {
        int position = blocks.indexOf(totalLine) + 1;
        return position < blocks.size() ? Optional.of(blocks.get(position)) : Optional.empty();
    }

    private Optional<Block> getPreviousLine(List<Block> blocks, Block totalLine) {
        int position = blocks.indexOf(totalLine) - 1;
        return position < blocks.size() ? Optional.of(blocks.get(position)) : Optional.empty();
    }

    private boolean isTotalLine(Block block) {
        String line = block.text().toUpperCase();
        return line.matches(".{0,5}?IMPORTE.*") || line.matches(".{0,7}?TOTA?L?.*") ||
            line.matches(".{0,5}?TARJETA.*")
            || line.matches(".{0,5}?EFECTIVO.*") || line.matches(".{0,5}?CONTADO,*");
    }

    private List<OcrLine> getOcrLines(List<Block> blocks, BigDecimal total) {
        List<OcrLine> complexLines = blocks.stream()
            .filter(b -> b.text().split(" ").length > 2 && b.text().matches(".*\\d?\\d?[\\.\\,/]\\d\\d?.?.?") &&
                Arrays.stream(b.text().split(" ")).filter(w -> w.matches("\\d?\\d?[\\.\\,/]\\d\\d?")).count() <= 2)
            .filter(l -> total != null && ofNullable(getAmountOfLine(l)).map(a -> a.compareTo(total) < 0).orElse(false))
            .map(this::toOcrLine)
            .toList();
        List<OcrLine> simpleLines = blocks.stream()
            .filter(b -> b.text().matches("\\d?\\d?[\\.\\,/]\\d\\d?.?.?") &&
                ofNullable(getAmountOfLine(b)).map(a -> a.compareTo(total) < 0).orElse(false))
            .map(b -> toOcrLine(getPreviousLine(blocks, b), b))
            .toList();
        return Stream.concat(complexLines.stream(), simpleLines.stream()).toList();

    }

    private OcrLine toOcrLine(Optional<Block> previousLine, Block b) {
        return new OcrLine(previousLine.map(Block::text).orElse(null), null, null, getAmountOfLine(b),
            previousLine.map(Block::text).orElse("") + " " + b.text());
    }

    private OcrLine toOcrLine(Block block) {
        BigDecimal amount = getAmountOfLine(block);
        return new OcrLine(getName(block), null, null, amount, block.text());
    }

    private String getName(Block block) {
        StringBuilder name = new StringBuilder();
        String[] words = block.text().split(" ");
        for (String word : words) {
            if (!word.matches("\\d?\\d.?\\d\\d?.?.?")) {
                name.append(word);
                name.append(" ");
            } else {
                break;
            }
        }
        if (name.toString().split(" ")[0].matches("\\d?\\d") && name.toString().length() > 2) {
            return name.substring(2, name.length() > 0 ? name.length() - 1 : 0);
        } else {
            return name.substring(0, name.length() > 0 ? name.length() - 1 : 0);
        }
    }

    private String getSite(List<Block> blocks) {
        return blocks.stream().findFirst().map(Block::text).orElse(null);
    }

    private static Document getDocument(String path) {
        try {
            InputStream sourceStream = new FileInputStream(path);
            SdkBytes sourceBytes = SdkBytes.fromInputStream(sourceStream);
            return Document.builder()
                .bytes(sourceBytes)
                .build();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
