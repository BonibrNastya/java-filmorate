package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.annotation.filmDate.ValidDate;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    private int id;
    @NotEmpty(message = "Не указано название фильма.")
    private String name;
    @Size(max = 200, message = "Описание должно уместиться в 200 символов.")
    private String description;
    @ValidDate(message = "Неверная дата релиза.")
    private LocalDate releaseDate;
    @Positive(message = "продолжительность должна быть положительной")
    private int duration;

}
