package me.renedo.payment.receipt.infraestructure;

import static java.util.Objects.requireNonNull;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

import me.renedo.payment.receipt.domain.ocr.OcrLine;
import me.renedo.payment.receipt.domain.ocr.OcrRead;
import software.amazon.awssdk.services.textract.model.Block;
import software.amazon.awssdk.services.textract.model.BoundingBox;
import software.amazon.awssdk.services.textract.model.Geometry;

class TextractServiceTest {

    TextractService service = new TextractService();

    @Test
    void get_text_from_lidl() throws URISyntaxException {
        OcrRead read = service.read(givenAbsoluteRouteFromFile("tickets/ticket_lidl.jpg"));

        assertThat(read.getSite(), is("LiDL"));
        assertThat(read.getCash(), is(true));
        assertThat(read.getTotal().doubleValue(), is(5.4D));
        assertThat(read.getLines(), hasSize(2));
    }

    @Test
    void get_text_from_alcampo() throws URISyntaxException  {
        OcrRead read = service.read(givenAbsoluteRouteFromFile("tickets/ticket_alcampo.jpeg"));

        assertThat(read.getSite(), is("ALCAMPO BURGOS"));
        assertThat(read.getCash(), is(false));
        assertThat(read.getTotal(), is(BigDecimal.valueOf(35.21)));
        assertThat(read.getLines(), hasSize(7));
    }

    @Test
    void get_text_from_galp() throws URISyntaxException  {
        OcrRead read = service.read(givenAbsoluteRouteFromFile("tickets/ticket_galp.jpeg"));

        assertThat(read.getSite(), is("galp"));
        assertThat(read.getCash(), nullValue());
        assertThat(read.getTotal(), is(BigDecimal.valueOf(50.01)));
        assertThat(read.getLines(), hasSize(0));
    }

    @Test
    void get_text_from_heredero() throws URISyntaxException  {
        OcrRead read = service.read(givenAbsoluteRouteFromFile("tickets/ticket_heredero.jpg"));

        assertThat(read.getSite(), is("FRUTAS HEREDERO"));
        assertThat(read.getCash(), is(true));
        assertThat(read.getTotal(), is(BigDecimal.valueOf(3.89)));
    }

    @Test
    void get_text_from_mercadona() throws URISyntaxException  {
        OcrRead read = service.read(givenAbsoluteRouteFromFile("tickets/ticket_mercadona.jpg"));

        assertThat(read.getSite(), is("MERCADONA,S.A"));
        assertThat(read.getCash(), is(false));
        assertThat(read.getTotal().doubleValue(), is(9.8D));
    }

    @Test
    void get_text_from_mercadona_two() throws URISyntaxException  {
        OcrRead read = service.read(givenAbsoluteRouteFromFile("tickets/ticket_mercadona2.jpeg"));

        assertThat(read.getSite(), is("MERCADONA,S.A"));
        assertThat(read.getCash(), is(true));
        assertThat(read.getTotal().doubleValue(), is(36.94D));
        assertThat(read.getLines().size(), is(13));
    }

    @Test
    void convert_lines_very_simple(){
        List<Block> blocks = List.of(givenBlock("2 TORTITAS DE MAIZ", 0.2463711F, 0.023053715F, 0.25928405F, 0.3581335F),
            givenBlock("1,10", 0.052206714F, 0.02148097F, 0.6167878F, 0.36386478F),
            givenBlock("2,20", 0.05454748F, 0.020790301F, 0.7343915F, 0.36573297F));

        List<String> lines = service.convertBlocksToLines(blocks);

        assertThat(lines, hasSize(1));
        assertThat(lines.get(0), is("2 TORTITAS DE MAIZ 1,10 2,20"));
    }

    @Test
    void convert_lines_two_lines(){
        List<Block> blocks = List.of(givenBlock("2 TORTITAS DE MAIZ", 0.2463711F, 0.023053715F, 0.25928405F, 0.3581335F),
            givenBlock("1,10", 0.052206714F, 0.02148097F, 0.6167878F, 0.36386478F),
            givenBlock("2,20", 0.05454748F, 0.020790301F, 0.7343915F, 0.36573297F),
            givenBlock("PATATAS", 0.05454748F, 0.020790301F, 0.7343915F, 0.39573297F));

        List<String> lines = service.convertBlocksToLines(blocks);

        assertThat(lines, hasSize(2));
        assertThat(lines.get(0), is("2 TORTITAS DE MAIZ 1,10 2,20"));
        assertThat(lines.get(1), is("PATATAS"));
    }

    @Test
    void convert_no_lines(){
        assertThat(service.convertBlocksToLines(null), empty());
        assertThat(service.convertBlocksToLines(List.of()), empty());
    }

    @Test
    void test_ocr_lines(){
        List<String> strLines = List.of("MERCADONA,S.A",
        "A-46103834",
        "C/ JUAN RAMON JIMENEZ 5",
        "09007 BURGOS",
        "TELÉFONO: 947577401",
        "04/04/2023 19:23 OP: 232629",
        "FACTURA SIMPLIFICADA: 4346-019-253219",
        "Descripción P. Unit Imp.(€)",
        "2 TORTITAS DE MAIZ 1,10 2,20",
        "3 COPOS DE ESPELTA 0% 1,80 5,40",
        "1 PECHUGA CERTIFICADA 4.55",
        "1 JAMON C. CALIDAD EXTR 1,95",
        "1 TOMATE MALLA 2 KG. 3,78",
        "1 SEPIA LIMPIA 7,82",
        "1 FRESÓN 1 KG. 2,55",
        "1 QUESO TIERNO 3,12",
        "1 MANGO",
        "0,686 kg 2,70 €/kg 1,85",
        "1 BERENJENA",
        "0,416 kg 2,29 €/kg 0,95",
        "1 PIMIENTO ROJO",
        "0,784 kg 2,99 €/kg 2,34",
        "1 PIMIENTO FREIR",
        "0,062 kg 2,49 €/kg 0.15",
        "1 PEPINO",
        "0,156 kg 1,79 €/kg 0,28");

        List<OcrLine> lines = service.getOcrLines(strLines, BigDecimal.valueOf(200D));

        assertThat(lines, hasSize(13));
        assertThat(lines.get(0).getName(), is("TORTITAS DE MAIZ"));
        assertThat(lines.get(0).getAmount(), is(2));
        assertThat(lines.get(0).getTotal().doubleValue(), is(2.2D));
        assertThat(lines.get(1).getName(), is("COPOS DE ESPELTA 0%"));
        assertThat(lines.get(1).getAmount(), is(3));
        assertThat(lines.get(1).getTotal().doubleValue(), is(5.4D));
        assertThat(lines.get(2).getName(), is("PECHUGA CERTIFICADA"));
        assertThat(lines.get(2).getAmount(), is(1));
        assertThat(lines.get(2).getTotal().doubleValue(), is(4.55D));
        assertThat(lines.get(3).getName(), is("JAMON C. CALIDAD EXTR"));
        assertThat(lines.get(3).getAmount(), is(1));
        assertThat(lines.get(3).getTotal().doubleValue(), is(1.95D));
        assertThat(lines.get(4).getName(), is("TOMATE MALLA 2 KG."));
        assertThat(lines.get(4).getAmount(), is(1));
        assertThat(lines.get(5).getName(), is("SEPIA LIMPIA"));
        assertThat(lines.get(5).getAmount(), is(1));
        assertThat(lines.get(6).getName(), is("FRESÓN 1 KG."));
        assertThat(lines.get(6).getAmount(), is(1));
        assertThat(lines.get(7).getName(), is("QUESO TIERNO"));
        assertThat(lines.get(7).getAmount(), is(1));
        assertThat(lines.get(8).getName(), is("MANGO"));
        assertThat(lines.get(8).getAmount(), is(1));
        assertThat(lines.get(9).getName(), is("BERENJENA"));
        assertThat(lines.get(9).getAmount(), is(1));
        assertThat(lines.get(10).getName(), is("PIMIENTO ROJO"));
        assertThat(lines.get(10).getAmount(), is(1));
        assertThat(lines.get(11).getName(), is("PIMIENTO FREIR"));
        assertThat(lines.get(11).getAmount(), is(1));
        assertThat(lines.get(12).getName(), is("PEPINO"));
        assertThat(lines.get(12).getAmount(), is(1));
    }

    private Block givenBlock(String text, float width, float height, float left, float top) {
         return Block.builder().blockType("LINE")
             .text(text)
            .geometry(Geometry.builder()
                .boundingBox(BoundingBox.builder().width(width).left(left).top(top).height(height).build())
                .build()).build();
    }

    private String givenAbsoluteRouteFromFile(String path) throws URISyntaxException {
        return Paths.get(requireNonNull(this.getClass().getClassLoader().getResource(path)).toURI()).toFile().getAbsolutePath();
    }

}
