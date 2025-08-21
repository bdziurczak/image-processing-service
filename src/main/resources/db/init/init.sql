CREATE TABLE IF NOT EXISTS users (
                                     username VARCHAR(50) PRIMARY KEY,
                                     password VARCHAR(500) NOT NULL,
                                     enabled BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS authorities (
                                           username VARCHAR(50) NOT NULL,
                                           authority VARCHAR(50) NOT NULL,
                                           CONSTRAINT fk_authorities_users FOREIGN KEY(username) REFERENCES users(username),
                                           CONSTRAINT ix_auth_username UNIQUE (username, authority)
);

INSERT INTO users(username, password, enabled)
VALUES ('user1', 'zaqwsx', true)
ON CONFLICT (username) DO NOTHING;

INSERT INTO authorities(username, authority)
VALUES ('user1', 'ROLE_USER')
ON CONFLICT (username, authority) DO NOTHING;