package hexlet.code;

import hexlet.code.models.Url;
import hexlet.code.repositories.UrlRepository;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    private static final UrlRepository URL_REPOSITORY = new UrlRepository(Database.getDataSource());

    public static void main(String[] args) {
        var app = getApp();
        URL_REPOSITORY.createTable();
        // Регистрируем shutdown hook для закрытия DataSource
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Закрытие пула соединений...");
            Database.closeDataSource();
        }));
        app.start(getPort());
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("DB_PORT", "7070");
        return Integer.valueOf(port);
    }

    public static Javalin getApp() {
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte());
        });

        app.get("/", ctx -> {
            LOGGER.info("Received request to root ®");
            ctx.result("All works fine");

        });

        app.get("/testdb", ctx -> {
            try (Connection connection = Database.getDataSource().getConnection()) {
                PreparedStatement stmt = connection
                        .prepareStatement("SELECT * FROM urls");
                ResultSet rs = stmt.executeQuery();

                // Обработка результата и вывод данных клиенту

                Url newUrl = new Url("https://newsite.com", LocalDateTime.now());
                URL_REPOSITORY.save(newUrl);
                newUrl = new Url("https://newsite2.com", LocalDateTime.now());
                URL_REPOSITORY.save(newUrl);

                ctx.result( URL_REPOSITORY.findAll().toString() + "\n"+
                        "Запрос " +
                        "выполнен "
                        + "успешно//bd connection ok");
            } catch (Exception e) {
                ctx.status(500).result("Ошибка при работе с базой "
                        + "данных//DB error");
            }


        });


        return app;
    }
}
