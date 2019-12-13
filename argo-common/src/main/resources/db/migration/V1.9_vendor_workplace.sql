CREATE TABLE vendor_workplace (
    vendor_workplace_id SERIAL,
    vendor_id SERIAL,
    type VARCHAR(100),
    vendor_office_address VARCHAR(300),
    vendor_warehouse_address VARCHAR(300),
    vendor_store_address VARCHAR(300),
    vendor_national_info VARCHAR(300),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT vendor_workplace_seq PRIMARY KEY (vendor_workplace_id)
--     CONSTRAINT vendor_channels_uk02 UNIQUE (vendor_id, vendor_workplace_id)
);
