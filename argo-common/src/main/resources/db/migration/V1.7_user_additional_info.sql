alter table argo_users add status varchar(30);

create table user_additional_infos (
    user_additional_info_id SERIAL,
    argo_user_id SERIAL,
    constraint user_additional_infos_pk PRIMARY KEY (user_additional_info_id)
);

alter table user_additional_infos owner to argo;

create table user_additional_info_values (
    useradditionalinfo_user_additional_info_id SERIAL,
    value varchar(255),
    user_additional_info_id SERIAL,
    constraint user_additional_info_values_pk primary key (useradditionalinfo_user_additional_info_id, user_additional_info_id)
);

alter table user_additional_info_values owner to argo;

