package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;


import java.util.*;
import java.util.stream.Collectors;

@Component
@Qualifier("InMemoryFilmStorage")
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    protected final Map<Long, Film> films = new HashMap<>();
    private long idCounter = 1;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    public InMemoryFilmStorage(MpaStorage mpaStorage, GenreStorage genreStorage) {
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }


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
        Mpa mpa = mpaStorage.getById(film.getMpa().getId());
        film.setId(idCounter);
        film.setMpa(mpa);
        films.put(idCounter, film);
        idCounter++;
        film.setGenres(getGenresFilm(film));
        log.info("Добавлен новый фильм: {}", film.toString());
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!(films.containsKey(film.getId()))) {
            log.warn("Введен неверный id: {}", film.getId());
            throw new NotFoundException("Не найден фильм для обновления.");
        }
        if (mpaStorage.getById(film.getMpa().getId()) == null) {
            throw new NotFoundException("Mpa не найден.");
        }
        Mpa mpa = mpaStorage.getById(film.getMpa().getId());
        film.setMpa(mpa);
        film.setGenres(new ArrayList<>(getGenresFilm(film)));
        films.put(film.getId(), film);
        log.info("Фильм {} {} обновлен.", film.getId(), film.getName());
        return film;
    }

    private List<Genre> getGenresFilm(Film film) {
        List<Genre> genres = new ArrayList<>();
        List<Genre> genresFromDB = genreStorage.findAll();
        if (film.getGenres().size() != 0) {
            for (Genre g : film.getGenres()) {
               int id = g.getId();
                if (!containsGenre(genresFromDB, id)) {
                    throw new NotFoundException("Жанр не найден.");
                } else {
                    Genre newGenre = genresFromDB.stream().filter(o -> o.getId() == id).collect(Collectors.toList()).get(0);
                    genres.add(newGenre);
                }
            }
        }
        return genres.stream().distinct().collect(Collectors.toList());
    }

    private boolean containsGenre (final List<Genre> genres, final int id){
        return genres.stream().anyMatch(o -> o.getId() == id);
    }

}
