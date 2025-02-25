package hexlet.code.dto;

import hexlet.code.models.Page;
import lombok.Getter;

/**
 * Класс MainPage не предназначен для наследования.
 */
public final class MainPage extends Page {
    @Getter
    private final Boolean visited;
    @Getter
    private final String flashType; // Тип сообщения: success, info, error

    /**
     * Конструктор.
     *
     * @param title Заголовок страницы
     * @param flash Flash-сообщение
     */
    public MainPage(String title, String flash) {
        super(title, flash);
        this.visited = false; // Инициализация по умолчанию
        this.flashType = null;
    }

    /**
     * Конструктор.
     *
     * @param title   Заголовок
     * @param visited Флаг посещения
     * @param flash   Flash-сообщение
     */
    public MainPage(String title, Boolean visited, String flash) {
        super(title, flash);
        this.visited = visited != null ? visited : false; // Защита от null
        this.flashType = null;
    }

    /**
     * Конструктор с указанием flash типа.
     *
     * @param title     Заголовок
     * @param flash     Текст flash-сообщения
     * @param flashType Тип flash-сообщения (success, error и т.д.)
     */
    public MainPage(String title, String flash, String flashType) {
        super(title, flash);
        this.flashType = flashType;
        this.visited = false; // Инициализация по умолчанию
    }
}
