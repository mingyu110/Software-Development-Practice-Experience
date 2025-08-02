CREATE TABLE city (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- You can add some initial data for testing if you want
-- This will be executed for each tenant database
INSERT INTO city (name) VALUES ('Default City 1');
INSERT INTO city (name) VALUES ('Default City 2');
