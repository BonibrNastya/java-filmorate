package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.annotation.userLogin.ValidLogin;


import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
@AllArgsConstructor
@Builder
@RequiredArgsConstructor
public class User {
    private long id;
    @Email(message = "Email невалидный.")
    private String email;
    @ValidLogin(message = "Невалидный логин.")
    private String login;
    @Past
    private LocalDate birthday;
    private String name;
    @JsonIgnore
    private Set<Long> friends = new HashSet<>();

}
