package hexlet.code.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UrlCheck {
    private Long id;
    private long urlId;
    private int statusCode;
    private String title;
    private String h1;
    private String description;
    private String createdAt;

    public UrlCheck(Long id, long urlId, int statusCode, String title, String h1, String description, String createdAt) {
        this.id = id;
        this.urlId = urlId;
        this.statusCode = statusCode;
        this.title = title;
        this.h1 = h1;
        this.description = description;
        this.createdAt = createdAt;
    }

    public UrlCheck(Long id, int statusCode, String title, String h1, String description, String createdAt) {
        this.id = id;
        this.statusCode = statusCode;
        this.title = title;
        this.h1 = h1;
        this.description = description;
        this.createdAt = createdAt;
    }

    public UrlCheck(int statusCode, String title, String h1, String description, String createdAt) {
        this.statusCode = statusCode;
        this.title = title;
        this.h1 = h1;
        this.description = description;
        this.createdAt = createdAt;
    }
}
