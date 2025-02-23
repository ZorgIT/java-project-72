package hexlet.code.repositories;

import hexlet.code.models.Url;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlRepository extends BaseRepository {

    public UrlRepository(DataSource dataSource) {
        super(dataSource);
    }

    public void createTable() {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS urls ("
                             + "id BIGSERIAL PRIMARY KEY, "
                             + "name VARCHAR(255) NOT NULL UNIQUE, "
                             + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                             + "checked_at DATE DEFAULT NULL,"
                             + "response_code VARCHAR(255) NULL UNIQUE)"
             )) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка создания таблицы urls", e);
        }
    }

    public static void save(Url url) {
        try (Connection connection = getConnection()) {
            // Проверка на уникальность добавляемого URL
            try (PreparedStatement checkStmt = connection.prepareStatement(
                    "SELECT id FROM urls WHERE name = ?"
            )) {
                checkStmt.setString(1, url.getName());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        // Если URL уже существует, устанавливаем его id и выходим из метода
                        url.setId(rs.getLong("id"));
                        return;
                    }
                }
            }

            // Если URL не найден, выполняем вставку
            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO urls (name, created_at) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                stmt.setString(1, url.getName());
                stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                stmt.executeUpdate();

                // Получаем сгенерированный идентификатор
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
        List<Url> urls = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM urls")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Url url = new Url(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                urls.add(url);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения списка URL", e);
        }
        return urls;
    }
    public static Optional<Url> findById(Long id) {
        String sql = "SELECT * FROM urls WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Url url = new Url(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                return Optional.of(url);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeAll() throws SQLException {
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute("SET REFERENTIAL_INTEGRITY FALSE");
            stmt.execute("TRUNCATE TABLE url_checks RESTART IDENTITY");
            stmt.execute("TRUNCATE TABLE urls RESTART IDENTITY");
            stmt.execute("SET REFERENTIAL_INTEGRITY TRUE");
        } catch (SQLException e) {
            // Игнорируем ошибку если таблицы нет
            if (!e.getMessage().contains("Table \"URLS\" not found")) {
                throw e;
            }
        }
    }
}
