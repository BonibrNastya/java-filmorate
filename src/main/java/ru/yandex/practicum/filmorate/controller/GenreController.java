package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreServiceImpl genreServiceImpl;

    @Autowired
    public GenreController(GenreServiceImpl genreServiceImpl) {
        this.genreServiceImpl = genreServiceImpl;
    }

    @GetMapping()
    public List<Genre> findAll() {
        return genreServiceImpl.findAll();
    }

    @GetMapping("/{id}")
    public Genre findById(@PathVariable("id") int id) {
        return genreServiceImpl.getById(id);
    }
}
