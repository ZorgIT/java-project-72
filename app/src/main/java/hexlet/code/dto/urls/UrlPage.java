package hexlet.code.dto.urls;

import hexlet.code.models.Page;
import hexlet.code.models.Url;


public class UrlPage extends Page {
    private Url url;
    private String title;
    private String header;

    public UrlPage(Url url, String title, String header) {
        super(title);
        this.url = url;
        this.title = title;
        this.header = header;
    }

}
