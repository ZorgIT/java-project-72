package hexlet.code.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class  Page {
    private String title;
    private String flash;

    public Page(String title) {
        this.title = title;
    }

    public Page(String title, String flash) {
        this.title = title;
        this.flash = flash;
    }

    public Page() {
    }
}
