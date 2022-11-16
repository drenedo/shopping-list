package me.renedo.shopping;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
public abstract class RequestTestCase{

    protected final static String PUT = "PUT";

    protected final static String DELETE = "DELETE";

    @Autowired
    private MockMvc mockMvc;
        protected void assertResponse(String endpoint, Integer expectedStatusCode, String expectedResponse) throws Exception {
        ResultMatcher response = expectedResponse.isEmpty() ? content().string("") : content().json(expectedResponse);

        mockMvc
            .perform(get(endpoint))
            .andExpect(status().is(expectedStatusCode))
            .andExpect(response);
    }


    protected void assertRequest(String method, String endpoint, Integer expectedStatusCode) throws Exception {
        mockMvc
            .perform(request(HttpMethod.valueOf(method), endpoint))
            .andExpect(status().is(expectedStatusCode))
            .andExpect(content().string(""));
    }

    protected void assertRequestWithBody(String method, String endpoint, String body, Integer expectedStatusCode) throws Exception {
        mockMvc
            .perform(request(HttpMethod.valueOf(method), endpoint).content(body).contentType(APPLICATION_JSON))
            .andExpect(status().is(expectedStatusCode));
    }


    protected String givenListOfShoppingList(List<String>ids) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        ids.forEach(id -> {
            try {
                jsonArray.put(new JSONObject().put("id", id));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
        return jsonArray.toString();
    }

    protected String givenShoppingList(String name, String description) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("description", description);
        return json.toString();
    }

    protected String givenItem(String name, String description, UUID listId, Integer amount, String unit) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("description", description);
        json.put("shoppingListId", listId.toString());
        json.put("amount", amount);
        json.put( "unit", unit);
        return json.toString();
    }

    protected UUID createList() throws Exception {
        UUID uuid = UUID.randomUUID();
        assertRequestWithBody(PUT, "/api/v1/lists/" + uuid, givenShoppingList("some-name", "some-description"), 201);
        return uuid;
    }
}
