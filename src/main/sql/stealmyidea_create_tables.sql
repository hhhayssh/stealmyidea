create sequence idea_id_sequence;
create sequence idea_number_sequence;
create table idea (
	idea_id bigint primary key default nextval('idea_id_sequence'),
	idea_number bigint not null default nextval('idea_number_sequence'),
	idea varchar(100) not null unique,
	idea_date varchar(100),
	steal_status varchar(100),
	steal_status_description varchar(500),
	greatness varchar(100),
	description varchar(500),
	status varchar(100) not null,
	status_date timestamp with time zone not null,
	enter_date timestamp with time zone,
	modification_date timestamp with time zone
);
create unique index on idea (idea_id);
create unique index on idea (idea_number);
create index on idea (steal_status);
create index on idea (greatness);
create index on idea (enter_date);
create index on idea (modification_date);
create index on idea (status);
create index on idea (status_date);
