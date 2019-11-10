CREATE TABLE argo_users (
  argo_user_id SERIAL,
  user_name VARCHAR (100) NOT NULL,
  login_id VARCHAR(200) NOT NULL,
  password VARCHAR (500) NOT NULL,
  dtype VARCHAR (100) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT argo_users_pk PRIMARY KEY (argo_user_id)
);
