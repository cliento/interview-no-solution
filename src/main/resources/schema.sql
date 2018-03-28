-- noinspection SqlNoDataSourceInspectionForFile

create table customer (
  id bigint not null identity,
  name varchar(255),
  email varchar(255),
  primary key (id)
);

create table sale(
  id bigint not null identity,
  customer_id bigint not null,
  cardTransactionRef varchar(255) not null,
  amount numeric(19,2) not null,
  primary key (id),
  foreign key (customer_id) references customer(id) on delete cascade
);

create table sale_item(
  id bigint not null identity,
  sale_id bigint not null,
  price numeric(19,2) not null,
  description varchar(255) not null,
  primary key (id),
  foreign key (sale_id) references sale(id) on delete cascade
);

