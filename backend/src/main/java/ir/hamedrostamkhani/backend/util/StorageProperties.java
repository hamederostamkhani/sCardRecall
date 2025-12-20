package ir.hamedrostamkhani.backend.util;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.nio.file.Path;
import java.util.List;

@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "app.storage")
public class StorageProperties {
    @NotNull
    private Path path;
    @Min(1024) // Min 1KB
    private long maxSize;
    @NotEmpty
    private List<String> allowedTypes;
}
