package hexlet.code.dto.urls;

import hexlet.code.models.Page;
import hexlet.code.models.Url;
import lombok.Getter;

import java.util.List;

@Getter
public class UrlsPage extends Page {
    private List<Url> urls;
    private String title;
    private String header;

    public UrlsPage(List<Url> urls, String title, String header) {
        super(title);
        this.urls = urls;
        this.title = title;
        this.header = header;
    }

}
