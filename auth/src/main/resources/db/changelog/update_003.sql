-- add employee table

create table "employee" (
    id serial primary key,
    first_name text,
    last_name text,
    inn bigint,
    hired timestamp
)