create table password_recoveries
(
    password_recovery_id bigint not null
        constraint password_recoveries_pk
            primary key,
    active boolean,
    created_at timestamp not null,
    token varchar(255),
    updated_at timestamp not null,
    user_id serial
);

alter table password_recoveries owner to argo;

