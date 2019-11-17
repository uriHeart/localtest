alter table argo_users rename column isapproved to status;

alter table argo_users alter column status type varchar(30) using status::varchar(30);
