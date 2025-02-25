package hexlet.code.repositories;

import hexlet.code.models.Url;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlRepository extends BaseRepository {

    public UrlRepository(DataSource dataSource) {
        super(dataSource);
    }

    public void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS urls (
                    id BIGSERIAL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL UNIQUE,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    checked_at TIMESTAMP,
                    response_code VARCHAR(255)
                )
                """;
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка создания таблицы urls", e);
        }
    }

    public static void save(Url url) {
        try (Connection connection = getConnection()) {
            // Проверка, нет ли уже такого URL
            try (PreparedStatement checkStmt = connection.prepareStatement(
                    "SELECT id FROM urls WHERE name = ?")) {
                checkStmt.setString(1, url.getName());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        url.setId(rs.getLong("id"));
                        return; // уже есть, просто возвращаем с установленным id
                    }
                }
            }

            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO urls (name, created_at) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                stmt.setString(1, url.getName());
                stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        url.setId(generatedKeys.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения URL", e);
        }
    }


    public static List<Url> findAll() {
        String sql = """
                SELECT
                    u.id               AS url_id,
                    u.name             AS url_name,
                    u.created_at       AS url_created_at,
                    c.created_at       AS check_created_at,
                    c.status_code      AS check_status_code
                FROM urls u
                LEFT JOIN url_checks c
                    ON c.url_id = u.id
                   AND c.created_at = (
                       SELECT MAX(created_at)
                       FROM url_checks cc
                       WHERE cc.url_id = u.id
                   )
                ORDER BY u.id
                """;

        List<Url> urls = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                long id = rs.getLong("url_id");
                String name = rs.getString("url_name");
                Timestamp createdAtTs = rs.getTimestamp("url_created_at");

                Url url = new Url(id, name,
                        createdAtTs == null ? null : createdAtTs.toLocalDateTime()
                );

                Timestamp checkCreatedAt = rs.getTimestamp("check_created_at");
                if (checkCreatedAt != null) {
                    url.setLastCheck(checkCreatedAt.toLocalDateTime());
                }
                int status = rs.getInt("check_status_code");

                if (!rs.wasNull()) {
                    url.setResponseCode(String.valueOf(status));
                }

                urls.add(url);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения списка URL (с последней проверкой)", e);
        }

        return urls;
    }

    public static Optional<Url> findById(Long id) {
        String sql = """
                SELECT
                    u.id               AS url_id,
                    u.name             AS url_name,
                    u.created_at       AS url_created_at,
                    c.created_at       AS check_created_at,
                    c.status_code      AS check_status_code
                FROM urls u
                LEFT JOIN url_checks c
                    ON c.url_id = u.id
                   AND c.created_at = (
                       SELECT MAX(created_at)
                       FROM url_checks cc
                       WHERE cc.url_id = u.id
                   )
                WHERE u.id = ?
                """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }

                String name = rs.getString("url_name");
                Timestamp createdAtTs = rs.getTimestamp("url_created_at");

                Url url = new Url(id, name,
                        createdAtTs == null ? null : createdAtTs.toLocalDateTime()
                );

                Timestamp checkCreatedAt = rs.getTimestamp("check_created_at");
                if (checkCreatedAt != null) {
                    url.setLastCheck(checkCreatedAt.toLocalDateTime());
                }
                int status = rs.getInt("check_status_code");
                if (!rs.wasNull()) {
                    url.setResponseCode(String.valueOf(status));
                }

                return Optional.of(url);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении Url по id", e);
        }
    }

    public static void update(Url url) {
        String sql = "UPDATE urls SET checked_at = ?, response_code = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (url.getLastCheck() == null) {
                stmt.setTimestamp(1, null);
            } else {
                stmt.setTimestamp(1, Timestamp.valueOf(url.getLastCheck()));
            }
            stmt.setString(2, url.getResponseCode());
            stmt.setLong(3, url.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении Url", e);
        }
    }

    public static void removeAll() throws SQLException {
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM url_checks");
            stmt.execute("DELETE FROM urls");
        } catch (SQLException e) {
            if (!e.getMessage().contains("Table \"URLS\" not found")) {
                throw e;
            }
        }
    }
}
