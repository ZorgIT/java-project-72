package hexlet.code.util;

import hexlet.code.dto.UrlCheck;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;

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
            URL parsed = new URL(url);
            String host = parsed.getHost();
            if (host.equalsIgnoreCase("localhost")) {
                int port = parsed.getPort();
                String protocol = parsed.getProtocol();
                String file = parsed.getFile();
                String newUrl = protocol + "://" + "127.0.0.1";
                if (port != -1) {
                    newUrl += ":" + port;
                }
                if (!file.isEmpty()) {
                    newUrl += file;
                }
                url = newUrl;
            }

            Document doc = Jsoup.connect(url).get();

            int statusCode = 200;
            String title = doc.title();
            String h1 = doc.selectFirst("h1") != null
                    ? doc.selectFirst("h1").text()
                    : null;
            String description = doc.selectFirst("meta[name=description]") != null
                    ? doc.selectFirst("meta[name=description]").attr("content")
                    : null;

            UrlCheck check = new UrlCheck(statusCode, title, h1, description);
            // Положим дату создания, чтобы при сохранении в url_checks всё было корректно
            check.setCreatedAt(LocalDateTime.now());
            return check;

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при проверке URL: " + url, e);
        }
    }
}
