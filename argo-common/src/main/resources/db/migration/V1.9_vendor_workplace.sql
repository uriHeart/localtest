CREATE TABLE vendor_workplace (
    vendor_workplace_id SERIAL,
    vendor_id SERIAL,
    type VARCHAR(100),
    full_address VARCHAR(500),
    jibun_address VARCHAR(300),
    jibun_address_english VARCHAR(300),
    road_address VARCHAR(300),
    road_address_english VARCHAR(300),
    post_code_6_digits VARCHAR(30),
    zip_code_5_digits VARCHAR(30),
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    national_info VARCHAR(300),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT vendor_workplace_seq PRIMARY KEY (vendor_workplace_id),
    CONSTRAINT vendor_workplace_uk01 UNIQUE (vendor_id)
--     CONSTRAINT vendor_channels_uk02 UNIQUE (vendor_id, vendor_workplace_id)
);
