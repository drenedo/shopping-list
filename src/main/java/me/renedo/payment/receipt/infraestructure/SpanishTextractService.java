package me.renedo.payment.receipt.infraestructure;

import static java.util.Optional.ofNullable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
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
public class SpanishTextractService implements OcrService {

    private final TextractClient textractClient;

    public SpanishTextractService() {
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
        List<String> lines = convertBlocksToLines(analyzeDocument.blocks().stream().filter(b -> b.blockType().equals(BlockType.LINE)).toList());

        Optional<String> totalLine = lines.stream().filter(this::isTotalLine).findFirst();
        BigDecimal total = getTotal(totalLine, lines);
        List<String> previousLines = totalLine.map(b -> lines.subList(0, lines.indexOf(b))).orElse(lines);
        return new OcrRead(getSite(lines), total, getOcrLines(previousLines, total), null, getIsInCash(lines),
            String.join("\n", previousLines));
    }

    protected List<String> convertBlocksToLines(List<Block> blocks) {
        if (blocks == null || blocks.isEmpty()) {
            return List.of();
        }
        List<String> lines = new ArrayList<>();
        Map<Float, String> parts = new TreeMap();
        Block firstBlock = blocks.get(0);
        parts.put(firstBlock.geometry().boundingBox().left(), firstBlock.text());
        for (Block block : blocks.subList(1, blocks.size())) {
            if (Math.abs(roundTop(firstBlock) - roundTop(block)) < 0.017) {
                parts.put(block.geometry().boundingBox().left(), block.text());
            } else {
                firstBlock = block;
                lines.add(String.join(" ", parts.values()));
                parts = new TreeMap();
                parts.put(block.geometry().boundingBox().left(), block.text());
            }
        }
        lines.add(parts.values().stream().collect(Collectors.joining(" ")));
        return lines;
    }

    protected List<OcrLine> getOcrLines(List<String> lines, BigDecimal total) {
        List<OcrLine> complexLines = lines.stream()
            .filter(b -> b.split(" ").length > 2 && b.matches(".*\\d?\\d?[\\.,/]\\d\\d?.?.?") &&
                Arrays.stream(b.split(" ")).filter(w -> w.matches("\\d?\\d?[\\.,/]\\d\\d?")).count() <= 2)
            .filter(l -> total != null && ofNullable(getAmountOfLine(l)).map(a -> a.compareTo(total) < 0).orElse(false))
            .map(l -> toOcrLineWithPrevious(lines, l))
            .toList();
        List<OcrLine> simpleLines = lines.stream()
            .filter(b -> b.matches("\\d?\\d?[\\.,/]\\d\\d?.?.?") &&
                ofNullable(getAmountOfLine(b)).map(a -> a.compareTo(total) < 0).orElse(false))
            .map(b -> toOcrLine(lines, b))
            .toList();
        return Stream.concat(complexLines.stream(), simpleLines.stream()).toList();
    }

    private Double roundTop(Block block) {
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return Double.valueOf(df.format(block.geometry().boundingBox().top()));
    }

    private Boolean getIsInCash(List<String> lines) {
        String lowerText = String.join("\n", lines).toLowerCase();
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

    private BigDecimal getAmountOfLine(String line) {
        BigDecimal amount = getRealAmountOfLine(line);
        return amount != null ? amount : extractAmountFromComplexLine(line);
    }

    private BigDecimal extractAmountFromComplexLine(String line) {
        String[] words = line.replaceAll("[\\.,_/]", " ").split(" ");
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

    private BigDecimal getRealAmountOfLine(String line) {
        try {
            return BigDecimal.valueOf(Double.parseDouble(line.replaceAll(",", ".")));
        } catch (NumberFormatException nfe) {
            return null;
        }
    }


    private BigDecimal getTotal(Optional<String> totalLine, List<String> blocks) {
        BigDecimal total = totalLine.map(this::getAmountOfLine).orElse(null);
        if ((total == null || total.doubleValue() <= 0) && totalLine.isPresent()) {
            BigDecimal nextLine = getNextLine(blocks, totalLine.get()).map(this::getAmountOfLine).orElse(BigDecimal.ZERO);
            if (nextLine.doubleValue() <= 0) {
                return getPreviousLine(blocks, totalLine.get()).map(this::getAmountOfLine).orElse(null);
            } else {
                return nextLine;
            }
        } else {
            return total;
        }
    }

    private Optional<String> getNextLine(List<String> lines, String totalLine) {
        return getPosition(lines, lines.indexOf(totalLine) + 1);
    }

    private Optional<String> getPreviousLine(List<String> lines, String totalLine) {
        return getPosition(lines, lines.indexOf(totalLine) - 1);
    }

    private Optional<String> getPosition(List<String> lines, int position) {
        return position < lines.size() ? Optional.of(lines.get(position)) : Optional.empty();
    }

    private boolean isTotalLine(String line) {
        String upperLine = line.toUpperCase();
        return upperLine.matches(".{0,5}?IMPORTE.*") || upperLine.matches(".{0,7}?TOTA?L?.*") ||
            upperLine.matches(".{0,5}?TARJETA.*")
            || upperLine.matches(".{0,5}?EFECTIVO.*") || upperLine.matches(".{0,5}?CONTADO,*");
    }

    private OcrLine toOcrLine(List<String> lines, String line) {
        Optional<String> previousLine = getPreviousLine(lines, line);
        Optional<String> nextLine = getNextLine(lines, line);
        String name = previousLine.orElse(null);
        BigDecimal total = getAmountOfLine(line);
        return nextLine.map(l -> getLineWithKg(l, total, name)
            .orElseGet(() -> getOcrLine(line, previousLine, name, total)))
            .orElseGet(() -> getOcrLine(line, previousLine, name, total));
    }

    private static OcrLine getOcrLine(String line, Optional<String> previousLine, String name, BigDecimal total) {
        return new OcrLine(name, null, null, total, previousLine.orElse("") + " " + line);
    }

    private OcrLine toOcrLineWithPrevious(List<String> lines, String line) {
        Optional<String> previousLine = getPreviousLine(lines, line);
        Optional<String> nextLine = getNextLine(lines, line);
        BigDecimal total = getAmountOfLine(line);
        String name = getName(line);
        boolean isCorrectName = name.matches(".*\\D{4}.*");
        String preName = isCorrectName ? name : previousLine.orElse(name);
        Double number = isCorrectName ? getNumberOfItems(line) : previousLine.map(this::getNumberOfItems).orElseGet(() -> getNumberOfItems(line));
        String strNumber = String.valueOf(number != null ? number.intValue() : null);
        String finalName =
            preName.startsWith(strNumber) && preName.length() > strNumber.length() ? preName.substring(strNumber.length() + 1) : preName;
        return getLineWithKg(previousLine.orElse(line), total, finalName)
            .orElseGet(() -> nextLine.flatMap(s -> getLineWithKg(s, total, finalName))
                .orElseGet(() -> getLineWithKg(line, total, finalName).orElseGet(() -> new OcrLine(finalName, number, null, total, line))));
    }

    private Optional<OcrLine> getLineWithKg(String line, BigDecimal total, String finalName) {
        if (hasKg(line)) {
            Double kgsAmount = getAmountByKg(line, total);
            BigDecimal price = getPriceByKg(line, total);
            if (price != null && kgsAmount != null && price.multiply(BigDecimal.valueOf(kgsAmount)).setScale(2, RoundingMode.HALF_UP).equals(total)) {
                return Optional.of(new OcrLine(finalName, kgsAmount, price, total, finalName + " " + total + " " + line));
            }
        }
        return Optional.empty();
    }

    private boolean hasKg(String line) {
        String lower = line.toLowerCase();
        return lower.contains(" kg ") || lower.contains("/kg");
    }

    private Double getAmountByKg(String line, BigDecimal price) {
        String[] kgs = line.split("kg");
        for (String kg : kgs) {
            Double value = getDoubleFromString(kg);
            if (value != null && !value.equals(price.doubleValue())) {
                return value;
            }
        }
        return null;
    }

    private BigDecimal getPriceByKg(String line, BigDecimal price) {
        String[] kgs = line.split("kg");
        for (int i = kgs.length - 1; i >= 0; i--) {
            Double value = getDoubleFromString(kgs[i]);
            if (value != null && !value.equals(price.doubleValue())) {
                return BigDecimal.valueOf(value);
            }
        }
        return null;
    }

    private static Double getDoubleFromString(String kg) {
        try {
            String valueWithDot = kg.replaceAll(",", ".");
            String cleanString = valueWithDot.replaceAll("[^\\d\\.]", "");
            return Double.valueOf(cleanString);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    private Double getNumberOfItems(String line) {
        String[] words = line.split(" ");
        return words.length > 0 && words[0].matches("\\d\\d?") ? Double.valueOf(words[0]) : null;
    }

    private String getName(String line) {
        StringBuilder name = new StringBuilder();
        String[] words = line.split(" ");
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

    private String getSite(List<String> lines) {
        return lines.stream().findFirst().orElse(null);
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
