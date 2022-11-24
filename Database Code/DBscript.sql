create sequence mid9db.company_companyid_seq
    as integer;

alter sequence mid9db.company_companyid_seq owner to gwolni2s;

create sequence mid9db.student_studentid_seq
    as integer;

alter sequence mid9db.student_studentid_seq owner to gwolni2s;

create sequence mid9db.skill_skillid_seq;

alter sequence mid9db.skill_skillid_seq owner to gwolni2s;

create sequence mid9db.topic_topicid_seq;

alter sequence mid9db.topic_topicid_seq owner to gwolni2s;

create table mid9db."user"
(
    userid   serial,
    username varchar(32)  not null,
    password varchar(100) not null,
    email    varchar(32)  not null,
    role     varchar(32)  not null,
    primary key (userid),
    unique (username),
    constraint key_email
        unique (email)
);

alter table mid9db."user"
    owner to gwolni2s;

create table mid9db.company
(
    userid         integer                                                           not null,
    name           varchar(32)                                                       not null,
    industry       varchar(32),
    banned         boolean default false,
    companyid      integer default nextval('mid9db.company_companyid_seq'::regclass) not null,
    contactdetails varchar(64),
    primary key (companyid),
    constraint key_userid_company
        unique (userid),
    constraint fkey_user
        foreign key (userid) references mid9db."user"
            on update cascade on delete cascade
);

alter table mid9db.company
    owner to gwolni2s;

alter sequence mid9db.company_companyid_seq owned by mid9db.company.userid;

create table mid9db.student
(
    userid         integer                                                           not null,
    firstname      varchar(32)                                                       not null,
    studymajor     varchar(32),
    university     varchar(32),
    matrikelnumber varchar(32)                                                       not null,
    lastname       varchar(32)                                                       not null,
    studentid      integer default nextval('mid9db.student_studentid_seq'::regclass) not null,
    primary key (studentid),
    constraint key_userid_student
        unique (userid),
    constraint "student_matrikel-num_key"
        unique (matrikelnumber),
    constraint fkey_user
        foreign key (userid) references mid9db."user"
            on update cascade on delete cascade
);

alter table mid9db.student
    owner to gwolni2s;

alter sequence mid9db.student_studentid_seq owned by mid9db.student.userid;

create table mid9db.skill
(
    skillid integer default nextval('mid9db.skill_skillid_seq'::regclass) not null,
    skill   varchar(32)                                                   not null,
    primary key (skillid),
    constraint skill_key
        unique (skill)
);

alter table mid9db.skill
    owner to gwolni2s;

create table mid9db.topic
(
    topicid integer default nextval('mid9db.topic_topicid_seq'::regclass) not null,
    topic   varchar(32),
    primary key (topicid),
    unique (topic)
);

alter table mid9db.topic
    owner to gwolni2s;

create table mid9db.reports
(
    companyid integer      not null,
    report    varchar(256) not null,
    primary key (companyid, report),
    foreign key (companyid) references mid9db.company
        on update cascade on delete cascade
);

alter table mid9db.reports
    owner to gwolni2s;

create table mid9db.job
(
    jobid       serial,
    companyid   integer      not null,
    title       varchar(100) not null,
    description varchar(1024),
    salary      varchar(32),
    location    varchar(64),
    constraint "job-postings_pkey"
        primary key (jobid),
    constraint jobpostings_companyid_fkey
        foreign key (companyid) references mid9db.company
            on update cascade on delete cascade
);

alter table mid9db.job
    owner to gwolni2s;

create table mid9db.student_has_topic
(
    studentid integer not null,
    topicid   integer not null,
    primary key (studentid, topicid),
    constraint fley_student
        foreign key (studentid) references mid9db.student
            on update cascade on delete cascade,
    constraint fkey_topic
        foreign key (topicid) references mid9db.topic
            on update cascade on delete cascade
);

comment on table mid9db.student_has_topic is 'table between student and topic';

alter table mid9db.student_has_topic
    owner to gwolni2s;

create table mid9db.student_has_skill
(
    studentid integer not null,
    skillid   integer not null,
    primary key (studentid, skillid),
    constraint fkey_student
        foreign key (studentid) references mid9db.student
            on update cascade on delete cascade,
    constraint fkey_skill
        foreign key (skillid) references mid9db.skill
            on update cascade on delete cascade
);

comment on table mid9db.student_has_skill is 'table between student and skill';

alter table mid9db.student_has_skill
    owner to gwolni2s;


