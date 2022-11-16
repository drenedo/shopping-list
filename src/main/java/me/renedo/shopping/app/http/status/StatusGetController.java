package me.renedo.shopping.app.http.status;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import me.renedo.shopping.app.http.V1Controller;
import me.renedo.shopping.status.application.StatusResolver;
import me.renedo.shopping.status.domain.Status;

@RestController
public class StatusGetController  extends V1Controller {

    private final StatusResolver resolver;

    public StatusGetController(StatusResolver resolver) {
        this.resolver = resolver;
    }

    @GetMapping("/statuses/")
    public List<StatusResponse> createList() {
        return resolver.getAllStatus().stream().map(StatusResponse::new).collect(toList());
    }

    record StatusResponse(String name, char id) {

        StatusResponse(Status status) {
            this(status.name(), status.getId());
        }
    }
}
