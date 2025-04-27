CREATE TABLE IF NOT EXISTS orders (
    order_id UUID PRIMARY KEY,
    username VARCHAR,
    shopping_cart_id UUID,
    payment_id UUID,
    delivery_id UUID,
    state VARCHAR,
    delivery_weight DOUBLE PRECISION,
    delivery_volume DOUBLE PRECISION,
    fragile BOOLEAN,
    total_price DOUBLE PRECISION,
    delivery_price DOUBLE PRECISION,
    product_price DOUBLE PRECISION
);

CREATE TABLE IF NOT EXISTS order_products (
    order_id UUID REFERENCES orders(order_id),
    product_id UUID,
    quantity BIGINT,
    PRIMARY KEY (order_id, product_id)
);