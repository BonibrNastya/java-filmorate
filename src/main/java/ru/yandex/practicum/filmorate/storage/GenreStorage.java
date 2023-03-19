package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
public interface GenreStorage {
    List<Genre> findAll();

    Genre getById(int id);

    List<Genre> findAllFromFilm(long id);
}
