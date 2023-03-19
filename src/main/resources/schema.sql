
create table IF NOT EXISTS GENRE
(
    GENRE_ID   serial not null primary key,
    GENRE_NAME CHARACTER VARYING(255) not null,
    constraint "GENRE_pk"
        primary key (GENRE_ID)
);
create table IF NOT EXISTS RATING_MPA
(
    MPA_ID   serial not null primary key,
    MPA_NAME CHARACTER VARYING(5) not null,
    constraint "MPA_pk"
        primary key (MPA_ID)
);
create table IF NOT EXISTS USERS
(
    ID       BIGINT auto_increment,
    EMAIL    CHARACTER VARYING(255) not null,
    LOGIN    CHARACTER VARYING(255) not null,
    BIRTHDAY DATE                   not null,
    USER_NAME     CHARACTER VARYING(255),
    constraint "USERS_pk"
        primary key (ID)
);

create table IF NOT EXISTS FILMS
(
    FILM_ID           BIGINT auto_increment,
    FILM_NAME         CHARACTER VARYING(255) not null,
    DESCRIPTION       CHARACTER VARYING(200),
    FILM_RELEASE_DATE DATE                   not null,
    FILM_DURATION     INTEGER                not null,
    MPA_ID            INTEGER,
    GENRE_ID          INTEGER,
    constraint FILM_PK
        primary key (FILM_ID),
    constraint FILMS_GENRE_GENRE_ID_FK
        foreign key (FILM_ID) references GENRE,
    constraint FILMS_MPA_MPA_ID_FK
        foreign key (MPA_ID) references RATING_MPA
);
create table IF NOT EXISTS FILMS_GENRE
(
    FILMS_ID BIGINT  not null,
    GENRE_ID INTEGER not null,
    constraint "FILMS_GENRE_FILMS_FILM_ID_fk"
        foreign key (FILMS_ID) references FILMS,
    constraint "FILMS_GENRE_GENRE_GENRE_ID_fk"
        foreign key (GENRE_ID) references GENRE
);

create table IF NOT EXISTS FRIENDS
(
    ID        BIGINT,
    FRIEND_ID BIGINT,
    STATUS    BOOLEAN,
    constraint "FRIENDS_USERS_USER_ID_fk"
        foreign key (ID) references USERS,
    constraint "FRIENDS_USERS_USER_ID_fk2"
        foreign key (FRIEND_ID) references USERS
);

create table IF NOT EXISTS LIKES
(
    ID      INTEGER,
    FILM_ID BIGINT,
    constraint "LIKES_FILMS_FILM_ID_fk"
        foreign key (FILM_ID) references FILMS,
    constraint "LIKES_USERS_USER_ID_fk"
        foreign key (ID) references USERS
);