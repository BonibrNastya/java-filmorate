package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
@RequiredArgsConstructor

public class MpaServiceImpl implements MpaService {
    private final MpaStorage mpaStorage;


    @Override
    public List<Mpa> findAll() {
        return mpaStorage.findAll();
    }

    @Override
    public Mpa getById(int id) {
        try {
            mpaStorage.getById(id);
        } catch (NotFoundException e) {
            throw new NotFoundException("Mpa с id = " + id + "не найден.");
        }
        return mpaStorage.getById(id);
    }
}
