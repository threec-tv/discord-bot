package co.uk.cloudam.streaming.template.config;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Properties;

@RestController
@RequestMapping("/healthcheck")
@PreAuthorize("isAnonymous()")
public class HealthCheckController {

    private static final String VERSION = "version";

    @GetMapping
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }

    @GetMapping("/version")
    public Map<String, String> version() {
        return Map.of("version", getVersionNumber());
    }

    public static String getVersionNumber() {
        Properties properties = new Properties();
        try {
            properties.load(HealthCheckController.class.getClassLoader().getResourceAsStream("version.properties"));
        } catch (Exception ignored) {
        }
        return (String) properties.getOrDefault(HealthCheckController.VERSION, "VunknownB" + LocalDateTime.now().toString().substring(0, 16));
    }
}
