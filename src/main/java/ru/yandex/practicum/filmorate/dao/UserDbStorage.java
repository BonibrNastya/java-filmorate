package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Component
@Qualifier("UserDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private final JdbcTemplate jdbcTemplate;
//    private static final RowMapper<User> USER_ROW_MAPPER = ((rs, rowNum) ->
//            User.builder()
//                    .id(rs.getLong("id"))
//                    .email(rs.getString("email"))
//                    .login(rs.getString("login"))
//                    .birthday(rs.getDate("birthday").toLocalDate())
//                    .name(rs.getString("user_name"))
//                    .build());


    @Override
    public User create(User user) {
        try {
            String sql = "INSERT INTO users (email, user_name, login, birthday) VALUES ( ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, user.getEmail());
                ps.setString(2, user.getName());
                ps.setString(3, user.getLogin());
                ps.setDate(4, Date.valueOf(user.getBirthday()));
                return ps;
            }, keyHolder);
            user.setId(keyHolder.getKey().intValue());
            log.info("Добавлен пользователь {} {}.", user.getId(), user.getLogin());
            return user;
        } catch (ValidationException e) {
            throw new ValidationException("Не получилось добавить пользователя.");
        }
    }

    @Override
    public User getById(long id) {
        List<User> users = jdbcTemplate.query("select * from users where id = ?",
                new Object[]{id}, (resultSet, i) -> User.builder()
                        .id(resultSet.getLong("id"))
                        .email(resultSet.getString("email"))
                        .login(resultSet.getString("login"))
                        .birthday(resultSet.getDate("birthday").toLocalDate())
                        .name(resultSet.getString("user_name"))
                        .build());
        if (users.size() == 1) {
            log.info("Найден пользователь: {} {}", users.get(0).getId(), users.get(0).getLogin());

            return users.get(0);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new NotFoundException(String.format("Пользователь с id %d не найден.", id));
        }
    }

    @Override
    public Collection<User> findAll() {
        return jdbcTemplate.query("select * from users", this::rowUserToMap);
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE users SET email = ?, login = ?, birthday = ?, user_name = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday(),
                user.getName());
        return user;
    }

    public void deleteUsers() {
        String delUsers = "DELETE FROM users";
        jdbcTemplate.update(delUsers);
    }

    public void addFriend(long userId, long friendId) {
        String sql = "INSERT INTO friends (id, friend_id) VALUES (?,?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    public void deleteFriend(long userId, long friendId) {
        String sql = "DELETE FROM friends WHERE id = ? AND friend_id = ?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    public List<User> getAllFriends(long userId) {
        return jdbcTemplate.query(
                "SELECT * FROM users WHERE id IN (SELECT friend_id FROM friends WHERE id = ?)",
                this::rowUserToMap, userId);
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        return jdbcTemplate.query(
                "SELECT * FROM users WHERE id IN " +
                        "(SELECT friend_id FROM friends WHERE id = ?)" +
                        "AND (SELECT friend_id FROM friends WHERE id = ?)",
                this::rowUserToMap, userId, friendId);
    }

    private User rowUserToMap(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .name(rs.getString("user_name"))
                .build();
    }
}
