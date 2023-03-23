package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;

@Service
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;

    public UserService(@Qualifier("InMemoryUserStorage") InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void addFriend(long userId, long friendId) {
        try {
            inMemoryUserStorage.getById(userId);
            inMemoryUserStorage.getById(friendId);
        } catch (NotFoundException e) {
            throw new NotFoundException(("Пользователь не найден"));
        }

        setFriend(userId, friendId);
    }

    public void deleteFriend(long userId, long friendId) {
        try {
            inMemoryUserStorage.getById(userId);
        } catch (NotFoundException e) {
            throw new NotFoundException(("Пользователь не найден"));
        }
        inMemoryUserStorage.getById(userId).getFriends().remove(friendId);
    }

    public List<User> getAllFriends(Long id) {
        List<User> userFriends = new ArrayList<>();
        Set<Long> friends = inMemoryUserStorage.getById(id).getFriends();
        if (friends.size() == 0) {
            return userFriends;
        }
        for (Long l : friends) {
            userFriends.add(inMemoryUserStorage.getById(l));
        }
        return userFriends;
    }

    public List<User> getCommonFriends(long id, long otherId) {
        List<User> user = getAllFriends(id);
        List<User> friend = getAllFriends(otherId);
        if (!user.isEmpty()) {
            List<User> commonFriends = new ArrayList<>(user);
            commonFriends.retainAll(friend);
            return commonFriends;
        } else {
            return new ArrayList<>();
        }
    }

    private void setFriend(long id, long otherId) {
        inMemoryUserStorage.getById(id).getFriends().add(otherId);
    }

}


