package hexlet.code.repositories;

import hexlet.code.dto.UrlCheck;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для работы с таблицей url_checks.
 * Класс не предназначен для наследования.
 */
public class UrlCheckRepository extends BaseRepository {

    /**
     * Конструктор.
     *
     * @param dataSource источник данных
     */
    public UrlCheckRepository(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * Создаёт таблицу url_checks, если она не существует.
     * Не предназначен для переопределения.
     */
    public final void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS url_checks (
                id BIGSERIAL PRIMARY KEY,
                url_id BIGINT REFERENCES urls(id),
                status_code INT,
                title TEXT,
                h1 TEXT,
                description TEXT,
                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)
                """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void save(UrlCheck check) {
        String sql = "INSERT INTO url_checks (status_code, title, h1, description, url_id, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?)";


        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, check.getStatusCode());
            stmt.setString(2, check.getTitle());
            stmt.setString(3, check.getH1());
            stmt.setString(4, check.getDescription());
            stmt.setLong(5, check.getUrlId());
            stmt.setTimestamp(6, Timestamp.valueOf(check.getCreatedAt()));
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    check.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<UrlCheck> findAllById(Long urlId) {
        String sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY created_at DESC";
        List<UrlCheck> checks = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, urlId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Timestamp timestamp = rs.getTimestamp("created_at");
                LocalDateTime createdAt = timestamp != null
                        ? timestamp.toLocalDateTime()
                        : null;

                UrlCheck check = new UrlCheck(
                        rs.getInt("status_code"),
                        rs.getString("title"),
                        rs.getString("h1"),
                        rs.getString("description"),
                        rs.getLong("url_id")
                );
                check.setId(rs.getLong("id"));
                check.setCreatedAt(createdAt);  //
                checks.add(check);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return checks;
    }

    public static void removeAll() throws SQLException {
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM url_checks");
        } catch (SQLException e) {
            // Ignore if table doesn't exist
            if (!e.getMessage().contains("Table \"URL_CHECKS\" not found")) {
                throw e;
            }
        }
    }
}
