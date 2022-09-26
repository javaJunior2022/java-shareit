create table IF NOT EXISTS users
(
    id    serial
        primary key,
    name  varchar not null,
    email varchar not null
        constraint users_pk
            unique
);

alter table users
    owner to sa;

create table IF NOT EXISTS requests
(
    id           serial
        primary key
        constraint requestor_id
            references users
            on update cascade on delete cascade,
    description  varchar not null,
    requestor_id integer not null
);


create table IF NOT EXISTS  bookings
(
    id         serial
        primary key,
    start_date timestamp not null,
    end_date   timestamp not null,
    item_id    integer   not null
        constraint item_id
            references items
            on update cascade on delete cascade,
    booker_id  integer
        constraint booker_id
            references bookings
            on update cascade on delete cascade,
    status     integer
        constraint status
            references booking_statuses
            on update cascade on delete cascade
);

alter table bookings
    owner to sa;


alter table requests
    owner to sa;

create table IF NOT EXISTS items
(
    id           integer default nextval('item_id_seq'::regclass) not null
        constraint item_pkey
            primary key,
    name         varchar                                          not null,
    description  varchar                                          not null,
    is_available boolean default false                            not null,
    owner_id     integer                                          not null
        constraint owner_id
            references users
            on delete cascade,
    request_id   integer
        constraint request_id
            references requests
            on delete cascade
);

alter table items
    owner to sa;

create table IF NOT EXISTS comments
(
    id        serial
        primary key,
    text      varchar not null,
    item_id   integer not null
        constraint item_id
            references items
            on update cascade on delete cascade,
    author_id integer
        constraint author_id
            references users
            on update cascade on delete cascade
);

alter table comments
    owner to sa;

create table IF NOT EXISTS booking_statuses
(
    id   integer not null
        primary key,
    name varchar not null
);

alter table booking_statuses
    owner to sa;

