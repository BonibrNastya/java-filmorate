package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.userLogin.ValidLogin;


import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
public class User {
    private long id;
    @Email(message = "Email should be valid")
    private String email;
    @ValidLogin(message = "Невалидный логин.")
    private String login;
    @Past
    private LocalDate birthday;
    private String name;
    @JsonIgnore
    private Set<Long> friends = new HashSet<>();

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
