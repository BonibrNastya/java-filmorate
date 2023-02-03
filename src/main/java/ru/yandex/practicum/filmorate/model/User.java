package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.userLogin.ValidLogin;


import javax.validation.constraints.*;
import java.time.LocalDate;


@Data
public class User {
    private int id;
    @Email(message = "Email should be valid")
    private String email;
    @ValidLogin(message = "Невалидный логин.")
    private String login;
    @Past
    private LocalDate birthday;
    private String name;

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

}
