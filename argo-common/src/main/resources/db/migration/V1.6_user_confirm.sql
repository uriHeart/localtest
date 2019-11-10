CREATE TABLE user_confirm
(
    user_confirm_id SERIAL,
    argo_user_id    BIGINT,
    ttl             TIMESTAMP NOT NULL,
    uuid            varchar(255),
    updated_at      timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at      timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT user_confirm_pk PRIMARY KEY (user_confirm_id),
    CONSTRAINT argo_user_uk01 UNIQUE (argo_user_id),
    CONSTRAINT uuid_uk01 UNIQUE (uuid)
);



