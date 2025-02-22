package hexlet.code.controllers;

import hexlet.code.dto.MainPage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.models.Url;
import hexlet.code.repositories.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.validation.ValidationError;
import io.javalin.validation.ValidationException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlController {
    public static void index(Context ctx) {
        String flash = ctx.consumeSessionAttribute("flash");
        String flashType = ctx.consumeSessionAttribute("flashType");
        List<Url> urls = UrlRepository.findAll();
        var page = new UrlsPage(urls, "Анализатор страниц", "Сайты", flash, flashType);
        //page.setFlash(flash);
        //page.setFlash(ctx.consumeSessionAttribute(flash));
        ctx.render("urls/index.jte", model(
                "page", page,
                "content", "",
                "footer", null,
                "ctx", ctx
        ));
    }

    public static void show(Context ctx) {
        var urlId = ctx.pathParamAsClass("id", Long.class).get();
        var url =
                UrlRepository.findAll().stream().filter(x -> x.getId() == urlId)
                        .findFirst().orElseThrow(() -> new NotFoundResponse("Course not found"));
        var pageTitle = "Сайт : " + url.getName();
        var page = new UrlPage(url, pageTitle, "Анализатор страниц");
        ctx.render("urls/show.jte", model(
                "page", page,
                "content", "",
                "footer", null,
                "ctx", ctx
        ));
    }

    public static void create(Context ctx) {
        var inputUrl = ctx.formParam("url");

        try {
            if (inputUrl == null || inputUrl.isEmpty()) {
                throw new ValidationException(Map.of("url", List.of(new ValidationError<>("URL не может быть пустым"))));
            }

            URI uri = new URI(inputUrl.trim());
            if (!uri.isAbsolute()) {
                throw new ValidationException(Map.of("url", List.of(new ValidationError<>("Некорректный URL"))));
            }

            URL url = uri.toURL();
            String normalizedUrl = url.getProtocol() + "://" + url.getAuthority();

            boolean urlExists = UrlRepository.findAll().stream()
                    .anyMatch(u -> u.getName().equals(normalizedUrl));

            if (urlExists) {
                ctx.sessionAttribute("flash", "Страница уже существует");
                ctx.sessionAttribute("flashType", "info");
            } else {
                Url checkedUrl = new Url(normalizedUrl);
                UrlRepository.save(checkedUrl);
                ctx.sessionAttribute("flash", "Страница успешно добавлена");
                ctx.sessionAttribute("flashType", "success");
            }
            ctx.redirect(NamedRoutes.urlsPath());

        } catch (ValidationException e) {
            var page = new MainPage("Анализатор страниц", "Некорректный URL", "error");
            ctx.render("index.jte", model("page", page));
        } catch (URISyntaxException | MalformedURLException e) {
            var page = new MainPage("Анализатор страниц", "Некорректный URL", "error");
            ctx.render("index.jte", model("page", page));
        }
    }
}
