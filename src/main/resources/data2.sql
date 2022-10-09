-- почистить таблицы, чтобы тесты проходили

--truncate table users cascade ;
truncate table users cascade ;
truncate table bookings cascade ;
truncate table comments cascade ;
truncate table items cascade ;
truncate table requests cascade ;

-- and numeration must begin from 1

ALTER SEQUENCE items_id_seq  RESTART WITH 1;
ALTER SEQUENCE users_id_seq  RESTART WITH 1;
ALTER SEQUENCE bookings_id_seq  RESTART WITH 1;
ALTER SEQUENCE comments_id_seq  RESTART WITH 1;
