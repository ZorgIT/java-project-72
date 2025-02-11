package hexlet.code.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SiteCheck {
    private Long id;
    private int responseCode;
    private String title;
    private String header;
    private String description;
    private String lastCheck;

    public SiteCheck(Long id, int responseCode, String title, String header, String description, String lastCheck) {
        this.id = id;
        this.responseCode = responseCode;
        this.title = title;
        this.header = header;
        this.description = description;
        this.lastCheck = lastCheck;
    }

    public SiteCheck(int responseCode, String title, String header, String description, String lastCheck) {
        this.responseCode = responseCode;
        this.title = title;
        this.header = header;
        this.description = description;
        this.lastCheck = lastCheck;
    }
}
