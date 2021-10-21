-- Create persons table

create table "person" (
  id serial primary key,
  login text,
  password text,
  employee_id integer
);