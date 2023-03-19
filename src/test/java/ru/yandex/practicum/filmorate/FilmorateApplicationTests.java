package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserDbStorage userDbStorage;
    private final FilmDbStorage filmDbStorage;


    @Test
    public void testFindUserById() {
        User user = userDbStorage.getById(1);
        assertThat(user).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    public void testFindAllUsers() {
        Collection<User> userList = userDbStorage.findAll();
        assertThat(userList).hasSize(3);
    }

    @Test
    public void testUpdateUser() {
        User user = userDbStorage.getById(1);
        assertThat(user).hasFieldOrPropertyWithValue("login", "hoh");

        User updateUser = userDbStorage.update(User.builder()
                .id(1)
                .email("a2@ya.ru")
                .login("b@ya.ru")
                .birthday(LocalDate.of(2001, 01, 20))
                .build());
        assertThat(updateUser).hasFieldOrPropertyWithValue("login", "b@ya.ru");
    }

    @Test
    public void testAddFriend() {
        List<User> friends = userDbStorage.getAllFriends(1);
        assertThat(friends).hasSize(2);
    }

    @Test
    public void testGetCommonFriends() {
        List<User> comFriends = userDbStorage.getCommonFriends(1, 3);
        assertThat(comFriends).hasSize(0);
    }

    @Test
    public void testFindFilmById() {
        Film film = filmDbStorage.getById(1);
        assertThat(film).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    public void testFindAllFilms() {
        Collection<Film> films = filmDbStorage.findAll();
        assertThat(films).hasSize(3);
    }

    @Test
    public void testUpdateFilm() {
        Film updateFilm = filmDbStorage.update(Film.builder()
                .id(1)
                .name("Слово")
                .description("Про стерву")
                .releaseDate(LocalDate.of(2017, 01, 24))
                .duration(108)
                .build());

        assertThat(updateFilm).hasFieldOrPropertyWithValue("name", "Слово");
    }
}
