package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    protected final Map<Long, User> users = new HashMap<>();
    public long idCounter = 1;

    @Override
    public User getById(long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException(String.format("Пользователь с id %d не найден.", id));
        }
        return users.get(id);
    }

    @Override
    public Collection<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @Override
    public User create(User user) {
        if (user.getLogin().contains(" ")) {
            log.warn("Введен невалидный логин: ({})", user.getLogin());
            throw new ValidationException("Логин не должен содержать пробелы.");
        }

        if (user.getName().isEmpty()) {
            log.info("На место пустого имени присвоен логин.");
            user.setName(user.getLogin());
        }
        user.setId(idCounter);
        users.put(idCounter, user);
        idCounter++;
        log.info("Добавлен новый пользователь: {}", user.toString());
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Введен неверный id: {}", user.getId());
            throw new NotFoundException("Не найден пользователь для обновления.");
        }
        users.remove(user.getId());
        users.put(user.getId(), user);
        log.info("Данные пользователя {} обновлены.", user.getLogin());
        return user;
    }
}
