CREATE TABLE categories (
    id   BIGINT       AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE customers (
    id      BIGINT       AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    email   VARCHAR(255) NOT NULL UNIQUE,
    cpf     VARCHAR(11)  NOT NULL UNIQUE,
    address VARCHAR(255)
);

CREATE TABLE products (
    id          BIGINT         AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    description VARCHAR(255),
    price       DECIMAL(19,2)  NOT NULL,
    category_id BIGINT         NOT NULL,
    CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE TABLE stocks (
    id         BIGINT  AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT  NOT NULL UNIQUE,
    quantity   INTEGER NOT NULL,
    CONSTRAINT fk_stocks_product FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE TABLE orders (
    id          BIGINT      AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT      NOT NULL,
    status      VARCHAR(50) NOT NULL,
    created_at  DATETIME    NOT NULL,
    CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id) REFERENCES customers (id)
);

CREATE TABLE order_items (
    id         BIGINT        AUTO_INCREMENT PRIMARY KEY,
    order_id   BIGINT        NOT NULL,
    product_id BIGINT        NOT NULL,
    quantity   INTEGER       NOT NULL,
    unit_price DECIMAL(19,2) NOT NULL,
    CONSTRAINT fk_order_items_order   FOREIGN KEY (order_id)   REFERENCES orders (id),
    CONSTRAINT fk_order_items_product FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE TABLE payments (
    id           BIGINT        AUTO_INCREMENT PRIMARY KEY,
    order_id     BIGINT        NOT NULL UNIQUE,
    method       VARCHAR(50)   NOT NULL,
    status       VARCHAR(50)   NOT NULL,
    amount       DECIMAL(19,2) NOT NULL,
    processed_at DATETIME,
    CONSTRAINT fk_payments_order FOREIGN KEY (order_id) REFERENCES orders (id)
);
