CREATE TABLE vendor_workplace_id (
    vendor_workplace_id SERIAL,
    vendorOfficeAddress VARCHAR(500),
    vendorWarehouseAddress VARCHAR(500),
    vendorNationalInfo VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT vendor_workplace_seq PRIMARY KEY (vendor_workplace_id)
);
ALTER TABLE main_board owner to argo;
SELECT * FROM main_board;
