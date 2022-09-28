-- почистить таблицы, чтобы тесты проходили

--truncate table users cascade ;
truncate table bookings cascade ;
truncate table comments cascade ;
truncate table items cascade ;
truncate table requests cascade ;

-- and numeration must begin from 1
--ALTER SEQUENCE users_id_seq  RESTART WITH 1;
ALTER SEQUENCE item_id_seq  RESTART WITH 1;

-- fill the statuses
insert into booking_statuses
select *
from (select 1, 'WAITING'
     ) x
where (select count (*) from booking_statuses where id=1)=0;

insert into booking_statuses
select *
from (select 2, 'APPROVED'
     ) x
where (select count (*) from booking_statuses where id=2)=0;

insert into booking_statuses
select *
from (select 3, 'REJECTED'
     ) x
where (select count (*) from booking_statuses where id=3)=0;

insert into booking_statuses
select *
from (select 4, 'CANCELED'
     ) x
where (select count (*) from booking_statuses where id=4)=0;