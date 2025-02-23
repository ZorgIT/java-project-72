package hexlet.code;

import hexlet.code.dto.UrlCheck;
import hexlet.code.util.UrlChecks;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class UrlCheckTest {
    private static MockWebServer mockWebServer;

    @BeforeAll
    static void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @Test
    public void testCheckUrl() {
        String mockHtml = """
            <!DOCTYPE html>
            <html>
                <head><title>Test Page</title></head>
                <body>
                    <h1>Test Header</h1>
                    <meta name="description" content="Test Description">
                </body>
            </html>""";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockHtml));

        UrlCheck check = UrlChecks.check(mockWebServer.url("/").toString());

        assertThat(check.getStatusCode()).isEqualTo(200);
        assertThat(check.getTitle()).isEqualTo("Test Page");
        assertThat(check.getH1()).isEqualTo("Test Header");
        assertThat(check.getDescription()).isEqualTo("Test Description");
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }
}
