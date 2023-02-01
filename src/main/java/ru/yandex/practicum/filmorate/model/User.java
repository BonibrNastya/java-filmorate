package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Date;


@Getter
@Setter
@ToString
public class User {
    private int id;
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank
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
