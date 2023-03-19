package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmServiceImpl;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmServiceImpl filmServiceImpl;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmServiceImpl filmServiceImpl) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmServiceImpl = filmServiceImpl;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return inMemoryFilmStorage.findAll();
    }

    @GetMapping("{id}")
    public Film getById(@PathVariable("id") long filmId) {
        return inMemoryFilmStorage.getById(filmId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10", required = false) Integer count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Количество популярных фильмов не может быть отрицательным.");
        }
        return filmServiceImpl.getPopular(count);
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        return inMemoryFilmStorage.create(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        return inMemoryFilmStorage.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void putLike(@PathVariable("id") long filmId,
                        @PathVariable("userId") long userId) {
        filmServiceImpl.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") long filmId,
                           @PathVariable("userId") long userId) {
        filmServiceImpl.deleteLike(filmId, userId);
    }
}
