
/*******************************************
 * RabbitMQ 를 통해 수신한 이벤트 정보
 *******************************************/
create table rca.tb_rca_event_cache
(
    sequence varchar(21) not null,
    event_type varchar(20),
    contents text,
    update_time timestamp with time zone,
    ticket_id varchar(20)
);

alter table rca.tb_rca_event_cache owner to rcadb;

create unique index tb_event_cache_sequence_uindex
on rca.tb_rca_event_cache (sequence);


/*******************************************
 * 코드 테이블
 *******************************************/
create table rca.tb_code
(
    category varchar(20),
    lvl integer,
    p_code varchar(20),
    code varchar(20) not null,
    name varchar(20),
    use_yn char default 'Y'::bpchar,
    display_order integer,
    description varchar(50)
);
comment on table rca.tb_code is '코드테이블';
comment on column rca.tb_code.category is '분류';
comment on column rca.tb_code.lvl is '반드시 필요하지 않을 수도있음. 카테고리의 하이라키가 복잡할때를 대비해 만듦';
comment on column rca.tb_code.p_code is '상위코드';
comment on column rca.tb_code.code is '코드';
comment on column rca.tb_code.name is '코드명';
comment on column rca.tb_code.use_yn is '사용여부';
comment on column rca.tb_code.display_order is '표시순서';
comment on column rca.tb_code.description is '설명';
alter table rca.tb_code owner to rcadb;


/*******************************************
 * 로그인을 위한 사용자 테이블
 *******************************************/
create table rca.tb_user
(
    id varchar(20) not null
        constraint tb_user_pkey
            primary key,
    pw char(41) default ''::bpchar not null,
    name varchar(20) not null,
    lvl integer not null,
    last_login timestamp with time zone,
    update_time timestamp with time zone
);
alter table rca.tb_user owner to rcadb;
