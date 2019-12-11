CREATE TABLE vendor_workplace_id (
    vendor_workplace_id SERIAL,
    vendor_id SERIAL,
    vendor_office_address VARCHAR(300),
    vendor_warehouse_address VARCHAR(300),
    vendor_store_address VARCHAR(300),
    vendor_national_info VARCHAR(300),
    CONSTRAINT vendor_workplace_seq PRIMARY KEY (vendor_workplace_id)
--     CONSTRAINT vendor_channels_uk02 UNIQUE (vendor_id, vendor_workplace_id)
);
