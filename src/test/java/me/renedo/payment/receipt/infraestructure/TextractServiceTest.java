package me.renedo.payment.receipt.infraestructure;

import static java.util.Objects.requireNonNull;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import me.renedo.payment.receipt.domain.ocr.OcrRead;

class TextractServiceTest {

    TextractService service = new TextractService();

    @Test
    void get_text_from_lidl() throws URISyntaxException {
        OcrRead read = service.read(givenAbsoluteRouteFromFile("tickets/ticket_lidl.jpg"));

        assertThat(read.getSite(), is("LiDL"));
        assertThat(read.getCash(), is(true));
        assertThat(read.getTotal(), is(BigDecimal.valueOf(5.40)));
        assertThat(read.getLines(), Matchers.hasSize(2));
    }

    @Test
    void get_text_from_alcampo() throws URISyntaxException  {
        OcrRead read = service.read(givenAbsoluteRouteFromFile("tickets/ticket_alcampo.jpeg"));

        assertThat(read.getSite(), is("ALCAMPO BURGOS"));
        assertThat(read.getCash(), is(false));
        assertThat(read.getTotal(), is(BigDecimal.valueOf(35.21)));
        assertThat(read.getLines(), Matchers.hasSize(7));
    }

    @Test
    void get_text_from_galp() throws URISyntaxException  {
        OcrRead read = service.read(givenAbsoluteRouteFromFile("tickets/ticket_galp.jpeg"));

        assertThat(read.getSite(), is("galp"));
        assertThat(read.getCash(), nullValue());
        assertThat(read.getTotal(), is(BigDecimal.valueOf(50.01)));
        assertThat(read.getLines(), Matchers.hasSize(0));
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
        assertThat(read.getTotal(), is(BigDecimal.valueOf(9.8)));
    }

    private String givenAbsoluteRouteFromFile(String path) throws URISyntaxException {
        return Paths.get(requireNonNull(this.getClass().getClassLoader().getResource(path)).toURI()).toFile().getAbsolutePath();
    }

}
