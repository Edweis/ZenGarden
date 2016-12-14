# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table appointment (
  id                            bigint auto_increment not null,
  title                         varchar(255) not null,
  date                          datetime(6),
  place                         varchar(255),
  price                         integer,
  comment                       varchar(255),
  rating                        integer,
  constraint pk_appointment primary key (id)
);

create table chat (
  id                            bigint auto_increment not null,
  title                         varchar(255) not null,
  constraint pk_chat primary key (id)
);

create table contact (
  id                            bigint auto_increment not null,
  iduser                        bigint not null,
  type                          varchar(255) not null,
  value                         varchar(255) not null,
  constraint pk_contact primary key (id)
);

create table country (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  code2                         varchar(255),
  code3                         varchar(255),
  constraint pk_country primary key (id)
);

create table education (
  id                            bigint auto_increment not null,
  iduser                        bigint,
  idschool                      bigint,
  duration_month                integer,
  start_year                    integer,
  promotion                     varchar(255),
  major                         varchar(255),
  is_home_university            tinyint(1) default 0,
  is_current_education          tinyint(1) default 0,
  verif_email                   varchar(255),
  is_email_verified             tinyint(1) default 0,
  constraint pk_education primary key (id)
);

create table experience (
  id                            bigint auto_increment not null,
  iduser                        bigint not null,
  name                          varchar(255),
  duration                      varchar(255),
  details                       varchar(255),
  constraint pk_experience primary key (id)
);

create table funding (
  id                            bigint auto_increment not null,
  iduser                        bigint not null,
  idscholarship                 bigint not null,
  amount                        varchar(255) not null,
  constraint pk_funding primary key (id)
);

create table message (
  id                            bigint auto_increment not null,
  idwriter                      bigint not null,
  content                       varchar(255) not null,
  date                          datetime(6) not null,
  idchat                        bigint not null,
  constraint pk_message primary key (id)
);

create table room (
  id                            bigint auto_increment not null,
  idchat                        bigint,
  idappointment                 bigint,
  constraint uq_room_idchat unique (idchat),
  constraint pk_room primary key (id)
);

create table chair (
  idroom                        bigint not null,
  iduser                        bigint not null,
  constraint pk_chair primary key (idroom,iduser)
);

create table scholarship (
  id                            bigint auto_increment not null,
  title                         varchar(255) not null,
  year                          varchar(255) not null,
  ref                           varchar(255),
  constraint pk_scholarship primary key (id)
);

create table school (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  idcountry                     bigint not null,
  city                          varchar(255),
  constraint pk_school primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  email                         varchar(255),
  password                      varchar(255),
  first_name                    varchar(255),
  picture_extension             varchar(255),
  introduction_text             varchar(255),
  appointment_price             varchar(255),
  rating_result                 integer,
  date_registration             datetime(6),
  idnationality                 bigint,
  constraint pk_user primary key (id)
);

create table work (
  id                            bigint auto_increment not null,
  company_name                  varchar(255),
  idcountry                     bigint,
  city                          varchar(255),
  constraint pk_work primary key (id)
);

create table work_cursus (
  id                            bigint auto_increment not null,
  iduser                        bigint,
  idwork                        bigint,
  start_date                    date,
  duration_month                integer,
  position                      varchar(255),
  is_current_work               tinyint(1) default 0,
  constraint pk_work_cursus primary key (id)
);

alter table contact add constraint fk_contact_iduser foreign key (iduser) references user (id) on delete restrict on update restrict;
create index ix_contact_iduser on contact (iduser);

alter table education add constraint fk_education_iduser foreign key (iduser) references user (id) on delete restrict on update restrict;
create index ix_education_iduser on education (iduser);

alter table education add constraint fk_education_idschool foreign key (idschool) references school (id) on delete restrict on update restrict;
create index ix_education_idschool on education (idschool);

alter table experience add constraint fk_experience_iduser foreign key (iduser) references user (id) on delete restrict on update restrict;
create index ix_experience_iduser on experience (iduser);

alter table funding add constraint fk_funding_iduser foreign key (iduser) references user (id) on delete restrict on update restrict;
create index ix_funding_iduser on funding (iduser);

alter table funding add constraint fk_funding_idscholarship foreign key (idscholarship) references scholarship (id) on delete restrict on update restrict;
create index ix_funding_idscholarship on funding (idscholarship);

alter table message add constraint fk_message_idwriter foreign key (idwriter) references user (id) on delete restrict on update restrict;
create index ix_message_idwriter on message (idwriter);

alter table message add constraint fk_message_idchat foreign key (idchat) references chat (id) on delete restrict on update restrict;
create index ix_message_idchat on message (idchat);

alter table room add constraint fk_room_idchat foreign key (idchat) references chat (id) on delete restrict on update restrict;

alter table room add constraint fk_room_idappointment foreign key (idappointment) references appointment (id) on delete restrict on update restrict;
create index ix_room_idappointment on room (idappointment);

alter table chair add constraint fk_chair_room foreign key (idroom) references room (id) on delete restrict on update restrict;
create index ix_chair_room on chair (idroom);

alter table chair add constraint fk_chair_user foreign key (iduser) references user (id) on delete restrict on update restrict;
create index ix_chair_user on chair (iduser);

alter table school add constraint fk_school_idcountry foreign key (idcountry) references country (id) on delete restrict on update restrict;
create index ix_school_idcountry on school (idcountry);

alter table user add constraint fk_user_idnationality foreign key (idnationality) references country (id) on delete restrict on update restrict;
create index ix_user_idnationality on user (idnationality);

alter table work add constraint fk_work_idcountry foreign key (idcountry) references country (id) on delete restrict on update restrict;
create index ix_work_idcountry on work (idcountry);

alter table work_cursus add constraint fk_work_cursus_iduser foreign key (iduser) references user (id) on delete restrict on update restrict;
create index ix_work_cursus_iduser on work_cursus (iduser);

alter table work_cursus add constraint fk_work_cursus_idwork foreign key (idwork) references work (id) on delete restrict on update restrict;
create index ix_work_cursus_idwork on work_cursus (idwork);


# --- !Downs

alter table contact drop foreign key fk_contact_iduser;
drop index ix_contact_iduser on contact;

alter table education drop foreign key fk_education_iduser;
drop index ix_education_iduser on education;

alter table education drop foreign key fk_education_idschool;
drop index ix_education_idschool on education;

alter table experience drop foreign key fk_experience_iduser;
drop index ix_experience_iduser on experience;

alter table funding drop foreign key fk_funding_iduser;
drop index ix_funding_iduser on funding;

alter table funding drop foreign key fk_funding_idscholarship;
drop index ix_funding_idscholarship on funding;

alter table message drop foreign key fk_message_idwriter;
drop index ix_message_idwriter on message;

alter table message drop foreign key fk_message_idchat;
drop index ix_message_idchat on message;

alter table room drop foreign key fk_room_idchat;

alter table room drop foreign key fk_room_idappointment;
drop index ix_room_idappointment on room;

alter table chair drop foreign key fk_chair_room;
drop index ix_chair_room on chair;

alter table chair drop foreign key fk_chair_user;
drop index ix_chair_user on chair;

alter table school drop foreign key fk_school_idcountry;
drop index ix_school_idcountry on school;

alter table user drop foreign key fk_user_idnationality;
drop index ix_user_idnationality on user;

alter table work drop foreign key fk_work_idcountry;
drop index ix_work_idcountry on work;

alter table work_cursus drop foreign key fk_work_cursus_iduser;
drop index ix_work_cursus_iduser on work_cursus;

alter table work_cursus drop foreign key fk_work_cursus_idwork;
drop index ix_work_cursus_idwork on work_cursus;

drop table if exists appointment;

drop table if exists chat;

drop table if exists contact;

drop table if exists country;

drop table if exists education;

drop table if exists experience;

drop table if exists funding;

drop table if exists message;

drop table if exists room;

drop table if exists chair;

drop table if exists scholarship;

drop table if exists school;

drop table if exists user;

drop table if exists work;

drop table if exists work_cursus;

