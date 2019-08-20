CREATE TABLE vendors (
  vendor_id SERIAL,
  name VARCHAR(50) NOT NULL,
  address VARCHAR(100) NOT NULL,
  email VARCHAR(50) NOT NULL,
  phone VARCHAR(20) NOT NULL,
  business_number VARCHAR(50) NOT NULL,
  representative  VARCHAR(20) NOT NULL,
  registered_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT vendors_pk PRIMARY KEY (vendor_id)
);

INSERT INTO vendors (name, address, email, phone, business_number, representative) VALUES
('(주)왁티','서울시 강남구 압구정로 10길 7 3층', 'support@wagti.com', '0234460930', '123-45-56789','강정훈');

CREATE TABLE sales_channels (
  sales_channel_id SERIAL,
  code VARCHAR(50) NOT NULL,
  name VARCHAR(50) NOT NULL,
  base_uri VARCHAR(100) NOT NULL,
  token_uri VARCHAR(100),
  login_uri VARCHAR(100),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT sales_channels_pk PRIMARY KEY (sales_channel_id),
  CONSTRAINT sales_channels_uk01 UNIQUE (code)
);

INSERT INTO sales_channels (code, name, base_uri, token_uri, login_uri) VALUES
('PLAYER', '플레이어', 'http://biz.player.co.kr', '/po/login/set_token', '/po/login/make_login');


CREATE TABLE sales_channel_collects (
  sales_channel_collect_id SERIAL,
  sales_channel_id SERIAL NOT NULL,
  collect_uri VARCHAR(100),
  collect_param TEXT,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT sales_channel_collects_pk PRIMARY KEY (sales_channel_collect_id)
);

INSERT INTO sales_channel_collects (sales_channel_id, collect_uri, collect_param) VALUES
(1, '/po/order/ord01/search', '{"S_SDATE":"S_SDATE","S_EDATE":"S_EDATE"}');

CREATE TABLE vendor_sales_channels (
  vendor_sales_channel_id SERIAL,
  vendor_id SERIAL,
  sales_channel_id SERIAL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT vendor_sales_channels_pk PRIMARY KEY (vendor_sales_channel_id),
  CONSTRAINT vendor_sales_channels_uk01 UNIQUE (vendor_id, sales_channel_id)
);

INSERT INTO vendor_sales_channels (vendor_id, sales_channel_id) VALUES
(1, 1);

CREATE TABLE vendor_accounts (
  vendor_account_id SERIAL,
  vendor_sales_channel_id SERIAL NOT NULL,
  credential_id VARCHAR(50) NOT NULL,
  credential_password VARCHAR(200) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT vendor_accounts_pk PRIMARY KEY (vendor_account_id),
  CONSTRAINT vendor_accounts_uk01 UNIQUE (vendor_sales_channel_id)
);

INSERT INTO vendor_accounts (vendor_sales_channel_id, credential_id, credential_password) VALUES
(1, '/dYXT/o6bVw0anv8hg5YWg==', '/EjrfGgL4AMixXEZxvqRSQ==');

