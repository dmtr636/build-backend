CREATE TABLE IF NOT EXISTS login_attempts (
    ip TEXT PRIMARY KEY,
    attempts INT NOT NULL,
    ban_expiration_time TIMESTAMP WITH TIME ZONE,
    last_login_attempt TIMESTAMP WITH TIME ZONE
);
