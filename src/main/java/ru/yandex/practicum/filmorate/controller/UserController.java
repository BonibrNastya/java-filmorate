package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    public int idCounter = 1;

    @GetMapping
    public Collection<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        if (user.getLogin().contains(" ")) {
            log.warn("Введен невалидный логин: ({})", user.getLogin());
            throw new ValidationException("Логин не должен содержать пробелы.");
        }

        if (user.getName() == null) {
            log.info("На место пустого имени присвоен логин.");
            user.setName(user.getLogin());
        }
        user.setId(idCounter);
        users.put(idCounter, user);
        idCounter++;
        log.info("Добавлен новый пользователь: {}", user.toString());
        return user;
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Введен неверный id: {}", user.getId());
            throw new ValidationException("Не найден пользователь для обновления.");
        }
        users.remove(user.getId());
        users.put(user.getId(), user);
        log.info("Данные пользователя {} обновлены.", user.getLogin());
        return user;
    }
}
