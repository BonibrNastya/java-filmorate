package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenreDBStorage implements GenreStorage {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findAll() {
        String genres = "SELECT * FROM genre";
        try {
            return jdbcTemplate.query(genres, this::rowGenreToMap);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public Genre getById(int id) {
        String sql = "SELECT * FROM genre WHERE genre_id = ?";
        List<Genre> genres = jdbcTemplate.query(
                sql, this::rowGenreToMap, id);
        if (genres.size() == 1) {
            log.info("Найден жанр:{} {}", genres.get(0).getId(), genres.get(0).getName());
            return genres.get(0);
        } else {
            log.info("Жанр с идентификатором {} не найден.", id);
            throw new NotFoundException(String.format("Жанр с id %d не найден.", id));
        }
    }

    @Override
    public List<Genre> findAllFromFilm(long id) {
        String sql = "SELECT fg.genre_id, g.genre_name " +
                "FROM films_genre fg JOIN genre g ON fg.genre_id = g.genre_id " +
                "WHERE fg.films_id = ?";
        try {
            List<Genre> genres = jdbcTemplate.query(sql, this::rowGenreToMap, id);
            log.info("Найдены жанры фильма {}: {}.", id, genres);
            return genres;
        } catch (DataAccessException e) {
            return null;
        }
    }

    private Genre rowGenreToMap(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("genre_name"))
                .build();
    }
}
