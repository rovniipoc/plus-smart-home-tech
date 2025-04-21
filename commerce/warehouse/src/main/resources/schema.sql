CREATE TABLE IF NOT EXISTS warehouse_products
(
    product_id UUID PRIMARY KEY,
    fragile    BOOLEAN,
    width      DOUBLE PRECISION,
    height     DOUBLE PRECISION,
    depth      DOUBLE PRECISION,
    weight     DOUBLE PRECISION,
    quantity   BIGINT
);

CREATE TABLE IF NOT EXISTS bookings
(
    booking_id      UUID PRIMARY KEY,
    fragile         BOOLEAN,
    delivery_volume DOUBLE PRECISION,
    delivery_weight DOUBLE PRECISION,
    delivery_id     UUID,
    order_id        UUID
);

CREATE TABLE IF NOT EXISTS booking_products
(
    booking_id UUID REFERENCES bookings (booking_id) ON DELETE CASCADE,
    product_id UUID REFERENCES warehouse_products (product_id) ON DELETE CASCADE,
    quantity   BIGINT
);