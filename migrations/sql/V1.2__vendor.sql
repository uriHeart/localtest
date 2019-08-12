CREATE SEQUENCE vendors_seq
  INCREMENT 1
  MINVALUE 1
  START 1
  CACHE 1;

CREATE TABLE vendors
(
  vendor_id bigint NOT NULL,
  name character varying(50) NOT NULL,
  address character varying(100) NOT NULL,
  email character varying(50) NOT NULL,
  phone character varying(20) NOT NULL,
  business_number character varying(50) NOT NULL,
  representative  character varying(20) NOT NULL,
  registered_at timestamp without time zone DEFAULT now(),
  created_at timestamp without time zone DEFAULT now(),
  updated_at timestamp without time zone DEFAULT now(),
  CONSTRAINT vendors_pk PRIMARY KEY (vendor_id)
);

INSERT INTO vendors (vendor_id, name, address, email, phone, business_number, representative, registered_at, created_at, updated_at)
VALUES (nextval('vendors_seq'), '테스트벤더','서울시 송파구 신천동', 'ags0688@gmail.com', '01085640688', '123-45-56789','홍길동', now(), now(), now());

CREATE SEQUENCE sales_channels_seq
  INCREMENT 1
  MINVALUE 1
  START 1
  CACHE 1;

CREATE TABLE sales_channels
(
  sales_channel_id bigint NOT NULL,
  code character varying(50) NOT NULL,
  name character varying(50) NOT NULL,
  url character varying(100) NOT NULL,
  created_at timestamp without time zone DEFAULT now(),
  updated_at timestamp without time zone DEFAULT now(),
  CONSTRAINT sales_channels_pk PRIMARY KEY (sales_channel_id)
);

ALTER TABLE sales_channels
  ADD CONSTRAINT sales_channels_uk01 UNIQUE (code);

insert into sales_channels (sales_channel_id, code, name, url)
VALUES (nextVal('sales_channels_seq'), 'WEMAKEPRICE', '위메프', 'http://www.wemakeprice.com');
insert into sales_channels (sales_channel_id, code, name, url)
VALUES (nextVal('sales_channels_seq'), 'TIMON', '티몬', 'http://www.ticketmonster.co.kr');


CREATE SEQUENCE vendor_codes_seq
  INCREMENT 1
  MINVALUE 1
  START 1
  CACHE 1;

CREATE TABLE vendor_codes
(
  vendor_code_id bigint NOT NULL,
  sales_channel_id bigint not null,
  vendor_id bigint NOT NULL,
  code character varying(50) NOT NULL,
  created_at timestamp without time zone DEFAULT now(),
  updated_at timestamp without time zone DEFAULT now(),
  CONSTRAINT vendor_codes_pk PRIMARY KEY (vendor_code_id)
);

ALTER TABLE vendor_codes
  ADD CONSTRAINT vendor_codes_uk01 UNIQUE (code);

ALTER TABLE vendor_codes
  ADD CONSTRAINT vendor_codes_uk02 UNIQUE (sales_channel_id, vendor_id);

INSERT INTO vendor_codes (vendor_code_id, sales_channel_id, vendor_id, code)
VALUEs (nextVal('vendor_codes_seq'), 1, 1, 'WMP-00001');

INSERT INTO vendor_codes (vendor_code_id, sales_channel_id, vendor_id, code)
VALUEs (nextVal('vendor_codes_seq'), 2, 1, 'TMN-00001');