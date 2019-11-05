CREATE TABLE password_recoveries (
  password_recovery_id SERIAL,
  argo_user SERIAL,
  token VARCHAR (256) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT FALSE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT password_recoveries_pk PRIMARY KEY (password_recovery_id)
);