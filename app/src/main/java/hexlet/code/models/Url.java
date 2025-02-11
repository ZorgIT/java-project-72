package hexlet.code.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Url {
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    public Url(Long id, String name, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public Url(String name, LocalDateTime createdAt) {
        this(null, name, createdAt);
    }

    public Url(String name) {
        this.name = name;
    }
}
