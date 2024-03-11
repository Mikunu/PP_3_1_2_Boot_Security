package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface AdminService {
    List<User> getAllUsers();

    User findUserByName(String firstName);

    void updateUser(User person, List<String> roles);

    void removeUser(Long id);

    User findOneById(Long id);
}
