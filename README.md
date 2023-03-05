# java-filmorate
Template repository for Filmorate project.

![file](https://user-images.githubusercontent.com/48413092/222975319-154caf68-d519-4ec7-8513-93860cfa72e7.png)

## Код БД:

Table film as F{

    id long [pk, increment]
  
    name varchar [not null, note: 'название обязательно']
  
    description varchar(200)
  
    releaseDate date
  
    duration varchar [not null, note: 'должна быть положительной']
  
    rating MPA [ref: > MPA.id]
}

Table users as U {

    id long [pk, increment] 
   
    email varchar [not null]
  
    login varchar [not null]
  
    birthday date
  
    name varchar
  
    friends varchar
}

Table friens {

     user_id long [pk, ref: > U.id]

    friend_id long [ref: > U.id]

    status boolean
}

Table likes {

    user_id long [pk, ref: > U.id]
  
    film_id long [ref: > F.id]
}

Table genre as G {

     id long [pk, increment]
 
     name varchar [not null]
 }

Ref film_genre: film.id <> genre.id

Table MPA {

    id long [pk, increment]

    name varchar(5)
}


## примеры запросов:

### первые 5 фильмов (выведем название, описание):

SELECT name
FROM film
LIMIT 5;

### 5 логинов самых молодых пользователей с их датой рождения:
SELECT login,
       birthday
FROM users
ORDER BY birthday DESC
LIMIT 5;
