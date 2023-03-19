package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreStorage genreStorage;

    @Override
    public List<Genre> findAll() {
        return genreStorage.findAll();
    }

    @Override
    public Genre getById(int id) {
        try {
            genreStorage.getById(id);
        } catch (NullPointerException e) {
            throw new NotFoundException("Жанр с id = " + id + "не найден.");
        }
        return genreStorage.getById(id);
    }
}
