package hexlet.code.dto;

import hexlet.code.models.Page;
import lombok.Getter;

@Getter
public class MainPage extends Page {
    private Boolean visited;
    private String flashType; // Тип сообщения: success, info, error

    public Boolean isVisited() {
        return visited;
    }

    public MainPage(String title, String flash) {
        super(title, flash);
        this.visited = false; // Инициализация по умолчанию
    }

    public MainPage(String title, Boolean visited, String flash) {
        super(title, flash);
        this.visited = visited != null ? visited : false; // Защита от null
    }

    // Новый конструктор для передачи типа сообщения
    public MainPage(String title, String flash, String flashType) {
        super(title, flash);
        this.flashType = flashType;
        this.visited = false; // Инициализация по умолчанию
    }
}