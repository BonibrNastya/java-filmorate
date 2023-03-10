package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    protected final Map<Long, Film> films = new HashMap<>();
    private long idCounter = 1;


    @Override
    public Film getById(long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException(String.format("Фильм с id %d не найден.", id));
        }
        return films.get(id);
    }

    @Override
    public Collection<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    @Override
    public Film create(Film film) {
        film.setId(idCounter);
        films.put(idCounter, film);
        idCounter++;
        log.info("Добавлен новый фильм: {}", film.toString());
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!(films.containsKey(film.getId()))) {
            log.warn("Введен неверный id: {}", film.getId());
            throw new NotFoundException("Не найден фильм для обновления.");
        }
        films.put(film.getId(), film);
        log.info("Фильм {} обновлен.", film.getName());
        return film;
    }
}
