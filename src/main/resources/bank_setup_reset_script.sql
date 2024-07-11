drop table if exists "user";
drop table if exists account;

create table "user" (
userId integer primary key not null,
username text unique,
password text
);

create table account (
id integer primary key not null,
type text,
balance real,
userId integer not null,
FOREIGN KEY (userId) references "user"(userId)
);

pragma foreign_keys;


insert into "user" (username, password) values ("admin", 123);