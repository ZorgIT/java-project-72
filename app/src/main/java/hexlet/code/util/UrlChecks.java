package hexlet.code.util;

import hexlet.code.dto.UrlCheck;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Optional;

public class UrlChecks {
    private MockWebServer mockWebServer;
    private String mockBaseUrl;

    public static void main(String[] args) {
        UrlChecks app = new UrlChecks();
        app.startMockServer();
        app.sendRequestToMockServer();
    }

    public void startMockServer() {
        try {
            mockWebServer = new MockWebServer();
            mockWebServer.start();

            // Готовый ответ
            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody("""
                                <html>
                                    <head><title>Mock Page</title></head>
                                    <body>
                                        <h1>Hello from Mock Server</h1>
                                    </body>
                                </html>
                            """)
            );

            mockBaseUrl = mockWebServer.url("/").toString();
            System.out.println("Mock server started at: " + mockBaseUrl);

        } catch (IOException e) {
            throw new RuntimeException("Failed to start mock server", e);
        }
    }

    public void sendRequestToMockServer() {
        // Отправляем запрос через Unirest на наш mock-сервер
        HttpResponse<String> response = Unirest.get(mockBaseUrl)
                .asString();

        // Разбираем ответ
        System.out.println("Status code: " + response.getStatus());
        System.out.println("Response body:\n" + response.getBody());

        // Парсим HTML
        String title = response.getBody().split("<title>")[1].split("</title>")[0];
        System.out.println("Page title: " + title);

        shutdownMockServer();
    }

    public void shutdownMockServer() {
        try {
            mockWebServer.shutdown();
            System.out.println("Mock server stopped");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static UrlCheck check(String url) {
        try {
            HttpResponse<String> response = Unirest.get(url).asString();
            String body = response.getBody();

            Document doc = Jsoup.parse(body);
            String title = doc.title();
            String h1 = Optional.ofNullable(doc.selectFirst("h1")).map(Element::text).orElse("");
            String description = Optional.ofNullable(doc.selectFirst("meta[name=description]"))
                    .map(e -> e.attr("content"))
                    .orElse("");

            return new UrlCheck(
                    response.getStatus(),
                    title,
                    h1,
                    description
            );
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }
}
