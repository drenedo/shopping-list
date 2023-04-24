package me.renedo.payment.app.http.statistic;

import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import me.renedo.NoSecurityRequestTestCase;

class StatisticGetControllerTest extends NoSecurityRequestTestCase {

    @Test
    void statistic_site() throws Exception {
        JSONArray list = new JSONArray(List.of(givenSiteStatistic("ALCAMPO 1", 30.5D), givenSiteStatistic("ALCAMPO 3", 61D), givenSiteStatistic("ALCAMPO 4", 30.5D)));

        String from = LocalDateTime.of(2023, 1, 1, 0, 0).toString();
        String to = LocalDateTime.of(2023, 3, 1, 0, 0).toString();
                  assertResponse("/api/v1/statistic/site/"+ from + "/" + to, 200, list.toString());
    }

    @Test
    void statistic_category() throws Exception {

        JSONArray list = new JSONArray(List.of( givenCategoryStatistic("CLOTHES", 'C', 30.5D), givenCategoryStatistic("ENTERTAINMENT", 'E', 30.5D), givenCategoryStatistic("FOOD", 'F', 30.5D), givenCategoryStatistic("HOUSE", 'H', 30.5D)));

        String from = LocalDateTime.of(2023, 1, 1, 0, 0).toString();
        String to = LocalDateTime.of(2023, 3, 1, 0, 0).toString();
        assertResponse("/api/v1/statistic/category/"+ from + "/" + to, 200, list.toString());
    }

    protected JSONObject givenSiteStatistic(String site, double total) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("site", site);
        json.put("total", total);
        return json;
    }

    protected JSONObject givenCategoryStatistic(String cat, char id, double total) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("category", cat);
        json.put("id", String.valueOf(id));
        json.put("total", total);
        return json;
    }
}
