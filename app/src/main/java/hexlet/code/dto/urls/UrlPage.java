package hexlet.code.dto.urls;

import hexlet.code.dto.UrlCheck;
import hexlet.code.models.Page;
import hexlet.code.models.Url;
import lombok.Getter;

import java.util.List;


@Getter
public class UrlPage extends Page {
    private Url url;
    private List<UrlCheck> urlChecks;
    private String title;
    private String header;

    public UrlPage(Url url, List<UrlCheck> urlChecks, String title,
                   String header) {
        super(title);
        this.url = url;
        this.urlChecks = urlChecks;
        this.title = title;
        this.header = header;
    }

}
