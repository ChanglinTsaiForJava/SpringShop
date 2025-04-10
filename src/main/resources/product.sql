use shop;
CREATE TABLE product (
                         product_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         product_name VARCHAR(255) NOT NULL,
                         image VARCHAR(255),
                         description TEXT,
                         quantity INT,
                         price DOUBLE,
                         discount DOUBLE,
                         special_price DOUBLE,

                         category_id BigInt,
                         CONSTRAINT fk_category
                             FOREIGN KEY (category_id)
                                 REFERENCES category(category_id)
                                 ON DELETE SET NULL
                                 ON UPDATE CASCADE
);