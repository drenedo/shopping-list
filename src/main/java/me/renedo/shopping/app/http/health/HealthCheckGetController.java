package me.renedo.shopping.app.http.health;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
final class HealthCheckGetController {

    private static final Map<String, String> HEALTH;
    static {
        Map<String, String> map = new HashMap<>();
        map.put("status", "ok");
        HEALTH = Collections.unmodifiableMap(map);
    }

    @GetMapping
    public Map<String, String> health(){
        return HEALTH;
    }
}
