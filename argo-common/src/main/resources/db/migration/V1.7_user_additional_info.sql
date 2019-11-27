alter table argo_users add status varchar(30);

create table user_additional_infos
(
    user_additional_info_id serial
        constraint user_additional_infos_pkey
        primary key,
    argo_user_id bigint
        constraint fki9tlgnmsyg9qwqt9465aygfvn
        references argo_users
);

alter table user_additional_infos owner to argo;

create table user_additional_info_values
(
    useradditionalinfo_user_additional_info_id serial not null,
    value varchar(255),
    user_additional_info_id varchar(255) not null,
    constraint user_additional_info_values_pkey
        primary key (useradditionalinfo_user_additional_info_id, user_additional_info_id)
);

alter table user_additional_info_values owner to argo;


