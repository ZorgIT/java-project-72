package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import hexlet.code.models.Url;
import hexlet.code.repositories.UrlRepository;
import hexlet.code.repositories.UrlCheckRepository;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AppTest {
    private Javalin app;

    @BeforeEach
    public void setUp() throws IOException, SQLException {
        app = App.getApp();
        System.setProperty("JDBC_DATABASE_URL", "jdbc:h2:mem:testdb;" +
                "DB_CLOSE_DELAY=-1;MODE=PostgreSQL");
        System.setProperty("DB_USERNAME", "sa");
        System.setProperty("DB_PASSWORD", "");

        // Создаем таблицу перед каждым тестом
        try (Connection connection = Database.getDataSource().getConnection();
             Statement stmt = connection.createStatement()) {
            System.setProperty("JDBC_DATABASE_URL", "jdbc:h2:mem:testdb;" +
                    "DB_CLOSE_DELAY=-1;MODE=PostgreSQL");
            System.setProperty("DB_USERNAME", "sa");
            System.setProperty("DB_PASSWORD", "");
            stmt.execute("CREATE TABLE IF NOT EXISTS urls("
                    + "id BIGSERIAL PRIMARY KEY, "
                    + "name VARCHAR(255) NOT NULL UNIQUE, "
                    + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                    + "checked_at DATE DEFAULT NULL,"
                    + "response_code VARCHAR(255) NULL UNIQUE)");

            stmt.execute("CREATE TABLE IF NOT EXISTS url_checks ("
                    + "id BIGSERIAL PRIMARY KEY, "
                    + "url_id BIGINT REFERENCES urls(id), "
                    + "status_code INT, "
                    + "title TEXT, "
                    + "h1 TEXT, "
                    + "description TEXT, "
                    + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        }

        UrlRepository.removeAll();
        UrlCheckRepository.removeAll();  // Очищаем таблицу
    }

    @Test
    void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string())
                    .contains("Домашняя страница");
        });
    }

    @Test
    void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    void testCreateUrl() throws SQLException {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https://example.com";
            var response = client.post("/urls", requestBody);

            assertThat(response.code()).isEqualTo(200); // Проверяем редирект
            assertThat(response.body().string())
                    .contains("https://example.com");

            // Проверяем, что URL добавился в БД
            var urls = UrlRepository.findAll();
            assertThat(urls).hasSize(1);
            assertThat(urls.get(0).getName()).isEqualTo("https://example.com");
        });
    }

    @Test
    void testShowUrl() throws SQLException {
        var url = new Url("https://example.com");
        UrlRepository.save(url);

        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/" + url.getId());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string())
                    .contains("https://example.com");
        });
    }

    @Test
    void testUrlNotFound() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/999999");
            assertThat(response.code()).isEqualTo(404);
        });
    }
}
