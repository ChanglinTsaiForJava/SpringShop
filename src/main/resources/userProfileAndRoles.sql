use shop;
CREATE TABLE users (
                       user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(20) NOT NULL UNIQUE,
                       password VARCHAR(20) NOT NULL,
                       email VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE roles (
                       role_id INT AUTO_INCREMENT PRIMARY KEY,
                       role_name VARCHAR(20) NOT NULL
);

CREATE TABLE user_role (
                           user_id BIGINT,
                           role_id INT,
                           PRIMARY KEY (user_id, role_id),
                           CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(user_id),
                           CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(role_id)
);

CREATE TABLE address (
                         address_id BIGINT AUTO_INCREMENT PRIMARY KEY
);