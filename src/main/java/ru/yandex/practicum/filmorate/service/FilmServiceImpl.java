package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmServiceImpl implements FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public FilmServiceImpl(@Qualifier("InMemoryFilmStorage") InMemoryFilmStorage inMemoryFilmStorage,
                           @Qualifier("InMemoryUserStorage") InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @Override
    public void addLike(long filmId, long userId) {
        try {
            inMemoryFilmStorage.getById(filmId);
            inMemoryUserStorage.getById(userId);
        } catch (NotFoundException e) {
            throw new NotFoundException("Невозможно добавить лайк.");
        }
        setLike(filmId, userId);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        try {
            inMemoryFilmStorage.getById(filmId);
            inMemoryUserStorage.getById(userId);
        } catch (NotFoundException e) {
            throw new NotFoundException("Невозможно добавить лайк.");
        }
        removeLike(filmId, userId);
    }

    @Override
    public List<Film> getPopular(int count) {
        return inMemoryFilmStorage.findAll().stream()
                .sorted(COMPARE_BY_COUNT)
                .limit(count)
                .collect(Collectors.toList());
    }

    private void setLike(long filmId, long userId) {
        inMemoryFilmStorage.getById(filmId).getLikedUsers().add(userId);
        likesCounter(filmId);
    }

    private void removeLike(long filmId, long userId) {
        inMemoryFilmStorage.getById(filmId).getLikedUsers().remove(userId);
        likesCounter(filmId);

    }

    private void likesCounter(long filmId) {
        long likes = inMemoryFilmStorage.getById(filmId).getLikedUsers().size();
        inMemoryFilmStorage.getById(filmId).setLikes(likes);
    }

    public static final Comparator<Film> COMPARE_BY_COUNT = (o1, o2) -> (int) (o2.getLikes() - o1.getLikes());
}
