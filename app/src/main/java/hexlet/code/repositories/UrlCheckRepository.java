package hexlet.code.repositories;

import hexlet.code.dto.UrlCheck;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UrlCheckRepository extends BaseRepository {
    public UrlCheckRepository(DataSource dataSource) {
        super(dataSource);
    }

    public void createTable() {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS url_checks  ("
                             + "id BIGSERIAL PRIMARY KEY, "
                             + "url_id BIGINT NOT NULL,"
                             + "status_code VARCHAR(255) NULL,"
                             + "h1 VARCHAR(255) NULL,"
                             + "title VARCHAR(255) NULL,"
                             + "description VARCHAR(255) NULL,"
                             + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)"
             )) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка создания таблицы url_checks", e);
        }
    }

    public static void save(UrlCheck urlCheck) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO url_checks (id, url_id, status_code, h1, title, description, created_at) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                stmt.setLong(1, urlCheck.getId());
                stmt.setLong(2, urlCheck.getUrlId());
                stmt.setInt(3, urlCheck.getStatusCode());
                stmt.setString(4, urlCheck.getH1());
                stmt.setString(5, urlCheck.getTitle());
                stmt.setString(6, urlCheck.getDescription());
                stmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
                stmt.executeUpdate();

                // Получаем сгенерированный идентификатор
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        urlCheck.setId(generatedKeys.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения URL", e);
        }
    }

    public static List<UrlCheck> findAllById(long id) {
        List<UrlCheck> urls = new ArrayList<>();
        String sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY created_at DESC";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id); // Устанавливаем параметр id
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UrlCheck url = new UrlCheck(
                        rs.getLong("id"),
                        rs.getLong("url_id"),
                        rs.getInt("status_code"),
                        rs.getString("h1"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getTimestamp("created_at").toLocalDateTime().toString()
                );
                urls.add(url);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения списка URL", e);
        }
        return urls;
    }

    public static void removeAll() throws SQLException {
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute("TRUNCATE TABLE url_checks RESTART IDENTITY");
        } catch (SQLException e) {
            // Игнорируем ошибку если таблицы нет
            if (!e.getMessage().contains("Table \"URLS\" not found")) {
                throw e;
            }
        }
    }
}
