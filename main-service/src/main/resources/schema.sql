drop table if exists requests, compilations_events, events, users, categories, locations, compilations;

create table if not exists users
(
    user_id bigint generated by default as identity primary key,
    name    varchar(250)        not null,
    email   varchar(254) unique not null
);

create table if not exists categories
(
    category_id bigint generated by default as identity primary key,
    name        varchar(50) unique not null
);

create table if not exists locations
(
    locations_id bigint generated by default as identity primary key,
    lat          float not null,
    lon          float not null
);

create table if not exists events
(
    event_id           bigint generated by default as identity primary key,
    annotation         varchar                                                       not null,
    category           bigint references categories (category_id) on delete restrict not null,
    confirmed_requests integer,
    created_on         timestamp without time zone                                   not null,
    description        varchar                                                       not null,
    event_date         timestamp without time zone                                   not null,
    initiator          bigint references users (user_id) on delete restrict          not null,
    location           bigint references locations (locations_id) on delete restrict,
    paid               boolean                                                       not null,
    participant_limit  integer                                                       not null,
    published_on       timestamp without time zone                                   not null,
    request_moderation boolean                                                       not null,
    state              varchar                                                       not null,
    title              varchar                                                       not null,
    views              integer
);

create table if not exists requests
(
    request_id bigint generated by default as identity primary key,
    created    timestamp without time zone                           not null,
    event      bigint references events (event_id) on delete cascade not null,
    requester  bigint references users (user_id) on delete restrict  not null,
    status     varchar,
    CONSTRAINT unique_event_requester UNIQUE (event, requester)
);

create table if not exists compilations
(
    compilation_id bigint generated always as identity primary key,
    pinned         boolean      not null,
    title          varchar(255) not null
);

create table if not exists compilations_events
(
    compilation bigint references compilations (compilation_id),
    event       bigint references events (event_id)
);