package hexlet.code.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Page {
    private String title;
    private String flash;
    private String flashType; // Тип сообщения: success, error и т.д.

    public Page(String title) {
        this.title = title;
    }

    public Page(String title, String flash) {
        this.title = title;
        this.flash = flash;
    }

    public Page(String title, String flash, String flashType) {
        this.title = title;
        this.flash = flash;
        this.flashType = flashType;
    }
    public Page() {
    }
}
