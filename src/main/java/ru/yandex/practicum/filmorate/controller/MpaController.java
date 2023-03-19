package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaServiceImpl mpaServiceImpl;

    @Autowired
    public MpaController(MpaServiceImpl mpaServiceImpl) {
        this.mpaServiceImpl = mpaServiceImpl;
    }

    @GetMapping()
    public List<Mpa> findAll() {
        return mpaServiceImpl.findAll();
    }

    @GetMapping("/{id}")
    public Mpa findById(@PathVariable("id") int id) {
        return mpaServiceImpl.getById(id);
    }
}
