package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.yandex.practicum.filmorate.annotation.filmDate.ValidDate;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Data
//@Builder(toBuilder = true)
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
    @JsonIgnore
    private Set<Long> likedUsers = new HashSet<>();

    @JsonIgnore
    private long likes = 0L;

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public void addLike(long userId) {
        likedUsers.add(userId);
        likes = likedUsers.size();
    }

    public void deleteLike(long userId) {
        likedUsers.remove(userId);
        likes = likedUsers.size();
    }

}
