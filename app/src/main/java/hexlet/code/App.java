package hexlet.code;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.controllers.UrlController;
import hexlet.code.dto.MainPage;
import hexlet.code.repositories.UrlCheckRepository;
import hexlet.code.repositories.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.javalin.rendering.template.TemplateUtil.model;

public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    private static final UrlRepository URL_REPOSITORY = new UrlRepository(Database.getDataSource());
    private static final UrlCheckRepository URL_CHECK_REPOSITORY =
            new UrlCheckRepository(Database.getDataSource());

    public static void main(String[] args) {
        var app = getApp();
        URL_REPOSITORY.createTable();
        URL_CHECK_REPOSITORY.createTable();
        // Регистрируем shutdown hook для закрытия DataSource
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Закрытие пула соединений...//close connection pull");
            Database.closeDataSource();
        }));
        app.start(getPort());
    }

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("DB_PORT", "7070");
        return Integer.valueOf(port);
    }

    public static Javalin getApp() {
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });

        app.get("/", ctx -> {
            LOGGER.info("Received request to root ®");
            var visited = Boolean.valueOf(ctx.cookie("visited"));
            var page = new MainPage("Домашняя страница", visited, "");
            ctx.render("index.jte", model(
                    "page", page,
                    "content", "",
                    "footer", null,
                    "ctx", ctx
            ));
            ctx.cookie("visited", String.valueOf(true));
        });

        app.get(NamedRoutes.urlsPath(), UrlController::index);
        app.get(NamedRoutes.urlsPath("{id}"), UrlController::show);
        app.post(NamedRoutes.urlsPath(), UrlController::create);
        app.post(NamedRoutes.urlsCheckPath("{id}"), UrlController::check);

        return app;
    }
}
