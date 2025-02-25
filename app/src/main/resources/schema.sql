DROP TABLE IF EXISTS url_checks;
DROP TABLE IF EXISTS urls;

CREATE TABLE urls (
                      id                         SERIAL PRIMARY KEY,
                      name                       VARCHAR(255) NOT NULL,
                      created_at                 TIMESTAMP NOT NULL,
                      checked_at DATE DEFAULT NULL,
                      response_code VARCHAR(255)

);

CREATE TABLE url_checks (
                            id                            BIGSERIAL PRIMARY KEY,
                            url_id                        BIGINT REFERENCES urls(id),
                            status_code                   INTEGER NOT NULL,
                            title                         VARCHAR(255),
                            h1                            VARCHAR(255),
                            description                   TEXT,
                            created_at                    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);