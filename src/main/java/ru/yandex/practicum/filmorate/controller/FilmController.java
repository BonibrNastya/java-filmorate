package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int idCounter = 1;

    @GetMapping
    public Collection<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        film.setId(idCounter);
        films.put(idCounter, film);
        idCounter++;
        log.info("Добавлен новый фильм: {}", film.toString());
        return film;
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        if (!(films.containsKey(film.getId()))) {
            log.warn("Введен неверный id: {}", film.getId());
            throw new ValidationException("Не найден фильм для обновления.");
        }
        films.put(film.getId(), film);
        log.info("Фильм {} обновлен.", film.getName());
        return film;
    }
}