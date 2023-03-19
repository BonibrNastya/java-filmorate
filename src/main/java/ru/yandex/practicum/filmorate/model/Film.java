package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.yandex.practicum.filmorate.annotation.filmDate.ValidDate;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;


@Data
@AllArgsConstructor
@Builder
@RequiredArgsConstructor
public class Film {
    private long id;
    @NotEmpty(message = "Не указано название фильма.")
    private String name;
    @Size(max = 200, message = "Описание должно уместиться в 200 символов.")
    private String description;
    @ValidDate(message = "Неверная дата релиза.")
    private LocalDate releaseDate;
    @Positive(message = "продолжительность должна быть положительной")
    private int duration;
    private Mpa mpa;
    private List<Genre> genres = new ArrayList<>();
    @JsonIgnore
    private Set<Long> likedUsers = new HashSet<>();
    @JsonIgnore
    private long likes = 0L;


}
