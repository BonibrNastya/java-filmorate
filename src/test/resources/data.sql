INSERT INTO users (EMAIL, LOGIN, BIRTHDAY, USER_NAME) VALUES('a@ya.ru', 'hoh', '2000-10-20', 'holoso');
INSERT INTO users (EMAIL, LOGIN, BIRTHDAY, USER_NAME) VALUES('a2@ya.ru', 'mmm', '2001-01-20', 'mops');
INSERT INTO users (EMAIL, LOGIN, BIRTHDAY, USER_NAME) VALUES('a3@ya.ru', 'pupu', '2002-02-20', 'pussy');

INSERT INTO friends (id, friend_id) VALUES (1, 2);
INSERT INTO friends (id, friend_id) VALUES (1, 3);
INSERT INTO friends (id, friend_id) VALUES (2, 3);

INSERT INTO FILMS (FILM_NAME, DESCRIPTION, FILM_RELEASE_DATE, FILM_DURATION, MPA_ID, GENRE_ID) VALUES ( 'Последнее слово', 'Пожилая стерва хочет исправить ошибки.', '2017-01-24', '108', 4 , 1);
INSERT INTO FILMS (FILM_NAME, DESCRIPTION, FILM_RELEASE_DATE, FILM_DURATION, MPA_ID, GENRE_ID) VALUES ( 'Брат', 'Братья, чьи пути разошлись, вынуждены жить под одной крышей.', '2016-11-23', '110', 2, 2 );
INSERT INTO FILMS (FILM_NAME, DESCRIPTION, FILM_RELEASE_DATE, FILM_DURATION, MPA_ID, GENRE_ID) VALUES ( 'Достать ножи', 'Частный детектив ведет запутанное дело о смерти известного писателя.', '2019-11-28', '130', 3, 5 );

insert into LIKES (ID, FILM_ID) VALUES ( 2,3 );