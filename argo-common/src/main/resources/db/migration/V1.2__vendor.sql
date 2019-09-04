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
('PLAYER', '플레이어', 'http://biz.player.co.kr', '/po/login/set_token', '/po/login/make_login'),
('EZ_ADMIN', '이지어드민', 'https://www.ezadmin.co.kr', null, '/login_process2.php');


CREATE TABLE vendor_channels (
  vendor_channel_id SERIAL,
  vendor_id SERIAL,
  sales_channel_id SERIAL,
  enabled VARCHAR(1),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT vendor_channels_pk PRIMARY KEY (vendor_channel_id),
  CONSTRAINT vendor_channels_uk01 UNIQUE (vendor_id, sales_channel_id)
);

INSERT INTO vendor_channels (vendor_id, sales_channel_id, enabled) VALUES
(1, 1, 'Y'),
(1, 2, 'Y');


CREATE TABLE channel_collect_info (
  channel_collect_info_id SERIAL,
  sales_channel_id SERIAL,
  collect_uri VARCHAR(100),
  collect_param TEXT,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT channel_collect_info_pk PRIMARY KEY (channel_collect_info_id)
);

INSERT INTO channel_collect_info (sales_channel_id, collect_uri, collect_param) VALUES
(1, '/po/order/ord01/search', '{"S_SDATE":"S_SDATE","S_EDATE":"S_EDATE"}'),
(2, '/function.htm', '');


CREATE TABLE channel_vendor_accounts (
  channel_vendor_account_id SERIAL,
  vendor_id SERIAL,
  sales_channel_id SERIAL,
  credential_id VARCHAR(50) NOT NULL,
  credential_password VARCHAR(200) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT channel_vendor_accounts_pk PRIMARY KEY (channel_vendor_account_id),
  CONSTRAINT channel_vendor_accounts_uk01 UNIQUE (vendor_id, sales_channel_id)
);

INSERT INTO channel_vendor_accounts (vendor_id, sales_channel_id, credential_id, credential_password) VALUES
(1, 1, '/dYXT/o6bVw0anv8hg5YWg==', '/EjrfGgL4AMixXEZxvqRSQ=='),
(1, 2, 'MmEFpXSLYkDpmzKSwkE3Jg==','BqzHi4A7eW1G0RD1whEKRA==');

