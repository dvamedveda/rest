-- populate employee table

insert into "employee"
  (id, first_name, last_name, inn, hired)
values
  (1, 'system', 'default user', 0, now()),
  (2, 'Boris', 'Savelev', 12345678987654321, now());