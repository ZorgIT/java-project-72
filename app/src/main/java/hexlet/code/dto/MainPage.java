package hexlet.code.dto;

import hexlet.code.models.Page;
import lombok.Getter;

@Getter
public class MainPage extends Page {
    private Boolean visited;

    public Boolean isVisited() {
        return visited;
    }

    public MainPage(String title, String flash) {
        super(title, flash);
    }

    public MainPage(String title, Boolean visited, String flash) {
        super(title, flash);
        this.visited = visited;
    }
}
