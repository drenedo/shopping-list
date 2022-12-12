package me.renedo.lexical.app.http.term;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import me.renedo.NoSecurityRequestTestCase;

class TermGetControllerTest extends NoSecurityRequestTestCase {

    @Test
    void get_terms_no_result() throws Exception {
        assertResponse("/api/v1/terms/search/hola/type/PRODUCT", 200,
            givenTerms(List.of()));
    }

    @Test
    void get_terms_with_results() throws Exception {
        assertResponse("/api/v1/terms/search/ite/type/PRODUCT", 200,
            givenTerms(List.of("item", "itelo")));
    }

    private String givenTerms(List<String>names) {
        JSONArray jsonArray = new JSONArray();
        names.forEach(name -> {
            try {
                jsonArray.put(new JSONObject().put("name", name));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
        return jsonArray.toString();
    }
}
