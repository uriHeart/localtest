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
('테스트벤더','서울시 송파구 신천동', 'ags0688@gmail.com', '01085640688', '123-45-56789','홍길동');

CREATE TABLE sales_channels (
  sales_channel_id SERIAL,
  code VARCHAR(50) NOT NULL,
  name VARCHAR(50) NOT NULL,
  url VARCHAR(100) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT sales_channels_pk PRIMARY KEY (sales_channel_id),
  CONSTRAINT sales_channels_uk01 UNIQUE (code)
);

INSERT INTO sales_channels (code, name, url) VALUES
('WEMAKEPRICE', '위메프', 'http://www.wemakeprice.com'),
('TIMON', '티몬', 'http://www.ticketmonster.co.kr');


CREATE TABLE vendor_codes (
  vendor_code_id SERIAL NOT NULL,
  sales_channel_id BIGINT not null,
  vendor_id BIGINT NOT NULL,
  code VARCHAR(50) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT vendor_codes_pk PRIMARY KEY (vendor_code_id),
  CONSTRAINT vendor_codes_uk01 UNIQUE (code),
  CONSTRAINT vendor_codes_uk02 UNIQUE (sales_channel_id, vendor_id)
);

INSERT INTO vendor_codes (sales_channel_id, vendor_id, code) VALUES
(1, 1, 'WMP-00001'),
(2, 1, 'TMN-00001');