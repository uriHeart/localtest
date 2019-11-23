CREATE TABLE source_item_info (
  source_item_id SERIAL,
  channel_id BIGINT,
  vendor_id BIGINT,
  item_id VARCHAR(100) NOT NULL,
  item_name VARCHAR(500) NOT NULL,
  item_option VARCHAR(500) NOT NULL,
  barcode VARCHAR(50) NOT NULL,
  CONSTRAINT source_item_info_pk PRIMARY KEY (source_item_id)
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
