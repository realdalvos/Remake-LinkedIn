create sequence "user_user-id_seq"
    as integer;

alter sequence "user_user-id_seq" owner to gwolni2s;

create sequence "company_user-id_seq"
    as integer;

alter sequence "company_user-id_seq" owner to gwolni2s;

create sequence "student_user-id_seq"
    as integer;

alter sequence "student_user-id_seq" owner to gwolni2s;

create sequence "first-name_user-id_seq"
    as integer;

alter sequence "first-name_user-id_seq" owner to gwolni2s;

create sequence "job-postings_job-id_seq"
    as integer;

alter sequence "job-postings_job-id_seq" owner to gwolni2s;

create table "user"
(
    userid    integer default nextval('mid9db."user_user-id_seq"'::regclass) not null
        primary key,
    username  char(32)                                                       not null
        unique,
    password  char(32)                                                       not null,
    emailaddr char(32)
);

alter table "user"
    owner to gwolni2s;

alter sequence "user_user-id_seq" owned by "user".userid;

create table company
(
    userid   integer not null
        primary key
        constraint "user-foreign-key"
            references "user"
            on update cascade on delete cascade,
    name     char(32),
    industry char(32),
    banned   boolean default false
);

alter table company
    owner to gwolni2s;

alter sequence "company_user-id_seq" owned by company.userid;

create table student
(
    userid         integer  not null
        primary key
        constraint "user-foreign-key"
            references "user"
            on update cascade on delete cascade,
    surname        char(32) not null,
    "study major"  char(32),
    university     char(32),
    "matrikel-num" integer  not null
        unique
);

alter table student
    owner to gwolni2s;

alter sequence "student_user-id_seq" owned by student.userid;

create table firstname
(
    userid   integer  not null
        constraint "user-foreign-key"
            references student
            on update cascade on delete cascade,
    position smallint not null,
    name     char(32) not null,
    constraint "first-name_pkey"
        primary key (userid, position)
);

alter table firstname
    owner to gwolni2s;

alter sequence "first-name_user-id_seq" owned by firstname.userid;

create table skills
(
    userid integer  not null
        constraint "user-foreign-key"
            references student
            on update cascade on delete cascade,
    skill  char(32) not null,
    primary key (userid, skill)
);

alter table skills
    owner to gwolni2s;

create table topics
(
    userid integer  not null,
    topic  char(32) not null,
    primary key (userid, topic)
);

alter table topics
    owner to gwolni2s;

create table reports
(
    userid integer      not null
        constraint "user-foreign-key"
            references company
            on update cascade on delete cascade,
    report varchar(256) not null,
    primary key (userid, report)
);

alter table reports
    owner to gwolni2s;

create table jobpostings
(
    jobid       integer default nextval('mid9db."job-postings_job-id_seq"'::regclass) not null
        constraint "job-postings_pkey"
            primary key,
    "user-id"   integer                                                               not null
        constraint "user-foreign-key"
            references company
            on update cascade on delete cascade,
    title       char(32)                                                              not null,
    description varchar(256),
    salaryrange money
);

alter table jobpostings
    owner to gwolni2s;

alter sequence "job-postings_job-id_seq" owned by jobpostings.jobid;


