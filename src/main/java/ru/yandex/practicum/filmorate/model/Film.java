package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

import static java.time.LocalDate.*;

@Getter
@Setter
@ToString
public class Film {
    private int id;
    @NotEmpty
    private String name;
    @Size( max = 200, message = "Description must be up to 200 characters.")
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "продолжительность должна быть положительной")
    private int duration;


    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
