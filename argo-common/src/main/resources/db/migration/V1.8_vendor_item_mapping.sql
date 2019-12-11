CREATE TABLE source_item_mapping (
  source_item_mapping_id SERIAL,
  channel_id BIGINT,
  vendor_id BIGINT,
  item_id VARCHAR(100) NOT NULL,
  item_name VARCHAR(500) NOT NULL,
  item_option VARCHAR(500) NOT NULL,
  vendor_item_id BIGINT,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT source_item_info_pk PRIMARY KEY (source_item_id)
);

CREATE TABLE vendor_item (
  vendor_item_id SERIAL,
  vendor_id BIGINT,
  vendor_item_name VARCHAR(500) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT vendor_item_pk PRIMARY KEY (vendor_item_id)
);

CREATE TABLE sku_mapping (
  sku_mapping_id SERIAL,
  vendor_item_id BIGINT,
  sku_id BIGINT,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT vendor_item_sku_mapping_pk PRIMARY KEY (sku_mapping_id),
  CONSTRAINT vendor_item_sku_mapping_uk01 UNIQUE (vendor_item_id, sku_id)
);


CREATE TABLE sku_master (
  sku_id SERIAL,
  brand VARCHAR(30),
  name VARCHAR(300),
  description VARCHAR(500),
  barcode VARCHAR(50) NOT NULL,
  model_number VARCHAR(50),
  length DECIMAL(19, 3),
  width DECIMAL(19, 3),
  height DECIMAL(19, 3),
  weight DECIMAL(19, 3),
  parent_sku_id bigint,
  image_url VARCHAR(500),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by VARCHAR(200),
  updated_by VARCHAR(200),
  CONSTRAINT sku_master_pk PRIMARY KEY (sku_id)
);


CREATE TABLE sku_attribute (
  sku_attribute_id SERIAL,
  sku_id BIGINT NOT NULL,
  attribute_key VARCHAR(100) NOT NULL,
  attribute_value VARCHAR(500) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by VARCHAR(200),
  updated_by VARCHAR(200),
  CONSTRAINT sku_attribute_pk PRIMARY KEY (sku_attribute_id)
);

CREATE INDEX sku_attribute_idx01
  ON sku_attribute (sku_id);
