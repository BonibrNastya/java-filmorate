package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MpaDBStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> findAll() {
        String mpas = "SELECT * FROM rating_mpa";
        try {
            return jdbcTemplate.query(mpas, this::mapRowToMpa);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public Mpa getById(int id) {
        List<Mpa> mpas = jdbcTemplate.query("SELECT * FROM rating_mpa WHERE mpa_id = ?",
                new Object[]{id}, (rs, rowNum) -> Mpa.builder()
                        .id(rs.getInt("mpa_id"))
                        .name(rs.getString("mpa_name"))
                        .build());
        if (mpas.size() == 1) {
            log.info("Найден mpa: {} {}", mpas.get(0).getId(), mpas.get(0).getName());
            return mpas.get(0);
        } else {
            log.info("Mpa с идентификатором {} не найден.", id);
            throw new NotFoundException(String.format("Mpa с id %d не найден.", id));
        }
    }

    public Mpa mapRowToMpa(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("mpa_id");
        String name = rs.getString("mpa_name");
        return Mpa.builder()
                .id(id)
                .name(name)
                .build();
    }
}
