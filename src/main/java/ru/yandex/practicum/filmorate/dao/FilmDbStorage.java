package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.storage.MpaStorage;


import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Qualifier("FilmDBStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;
    private MpaStorage mpaStorage;

//    private final RowMapper<Mpa> MPA_ROW_MAPPER = (rs, rowNum) ->
//            Mpa.builder()
//                    .id(rs.getInt("mpa_id"))
//                    .name(rs.getString("mpa_name"))
//                    .build();

//    private final RowMapper<Film> FILM_ROW_MAPPER = ((rs, rowNum) ->
//            Film.builder()
//                    .id(rs.getLong("film_id"))
//                    .name(rs.getString("film_name"))
//                    .description(rs.getString("description"))
//                    .releaseDate(rs.getDate("film_release_date").toLocalDate())
//                    .duration(rs.getInt("film_duration"))
//                    .mpa(Mpa.builder()
//                            .id(rs.getInt("mpa_id"))
//                            .name(rs.getString("mpa_name"))
//                            .build())
//                    .genres(getGenreFromDB(rs.getLong("film_id")))
//                    .build());

    @Override
    public Film getById(long id) {
        String sql = "select * from films f join rating_mpa r on f.mpa_id = r.mpa_id where f.film_id = ?";
        List<Film> films = jdbcTemplate.query(sql, this::rowFilmToMap);
        if (films.size() == 1) {
            log.info("Найден фильм: {} {}", films.get(0).getId(), films.get(0).getName());
            return films.get(0);
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new NotFoundException(String.format("Фильм с id %d не найден.", id));
        }
    }

    @Override
    public Collection<Film> findAll() {
        Collection<Film> films = jdbcTemplate.query("select * from films", this::rowFilmToMap);
        List<Mpa> mpaFromDB = mpaStorage.findAll();
        for (Film f : films) {
            f.setMpa(mpaFromDB.get(f.getMpa().getId()));
        }
        return films;
    }

    @Override
    public Film create(Film film) {
        String sql = "INSERT INTO films " +
                "(film_name, description, film_release_date, film_duration, mpa_id) " +
                "VALUES ( ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, film.getName());
                ps.setString(2, film.getDescription());
                ps.setDate(3, Date.valueOf(film.getReleaseDate()));
                ps.setInt(4, film.getDuration());
                ps.setInt(5, film.getMpa().getId());
                return ps;
            }, keyHolder);
            long id = keyHolder.getKey().longValue();
            film.setId(id);
            film.setMpa(getMpaFromDB(film.getMpa().getId()));
            if (film.getGenres().size() != 0) {
                updateFilmsGenre(film);
                film.setGenres(getGenreFromDB(id));
            }
            return film;
        } catch (ValidationException e) {
            throw new ValidationException("Фильм не прошел валидацию.");
        }
    }

    @Override
    public Film update(Film film) {
        long likes = film.getLikes();
        String sql = "UPDATE films SET film_name = ?, description = ?, film_release_date = ?, film_duration = ?, mpa_id = ? " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sql,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa());
        if (film.getGenres() != null) {
            updateFilmsGenre(film);
            film.setGenres(getGenreFromDB(film.getId()));
        }
        film.setLikes(likes);
        return film;
    }

    public void deleteFilms() {
        String delFilms = "DELETE FROM films";
        jdbcTemplate.update(delFilms);
    }

    public void addLike(long filmId, long userId) {
        String sql = "INSERT INTO likes (film_id, id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public void deleteLike(long filmId, long userId) {
        String sql = "DELETE FROM likes (film_id, id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public List<Film> getPopular(int count) {
        String sql = "SELECT f.film_id, f.film_name, f.description, f.film_release_date, f.film_duration, f.mpa_id " +
                "FROM films AS f " +
                "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(l.user_id) " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, this::rowFilmToMap, count);
    }

    private Mpa getMpaFromDB(int id) {
        String sql = "SELECT * FROM rating_mpa";
        return jdbcTemplate.queryForObject(sql, this::rowMpaToMap, id);
    }

    private List<Genre> getGenreFromDB(long id) {
        String filmGenres = "SELECT g.genre_id, g.genre_name FROM films_genre fg " +
                "LEFT JOIN genre g ON fg.genre_id = g.genre_id WHERE fg.films_id = ?";

        return jdbcTemplate.query(filmGenres, this::rowGenreToMap, id);
    }

    public void updateFilmsGenre(Film film) {
        String sql = "INSERT INTO films_genre VALUES (?,?)";
        if (film.getGenres().size() != 0) {
            film.getGenres().stream().map(Genre::getId).distinct()
                    .forEach(id -> jdbcTemplate.update(sql, film.getId(), id));
            log.info("У фильма с id {} добавлен список жанров {}", film.getId(), film.getGenres());
        }
    }

    private Genre rowGenreToMap(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("genre_name"))
                .build();
    }

    private Mpa rowMpaToMap(ResultSet rs, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(rs.getInt("mpa_id"))
                .name(rs.getString("mpa_name"))
                .build();
    }

    private Film rowFilmToMap(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("film_name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("film_release_date").toLocalDate())
                .duration(rs.getInt("film_duration"))
                .mpa(Mpa.builder()
                        .id(rs.getInt("mpa_id"))
                        .name(rs.getString("mpa_name"))
                        .build())
                .genres(getGenreFromDB(rs.getLong("film_id")))
                .build();
    }
}

