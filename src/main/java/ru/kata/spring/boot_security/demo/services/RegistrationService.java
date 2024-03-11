package ru.kata.spring.boot_security.demo.services;


import ru.kata.spring.boot_security.demo.models.User;

public interface RegistrationService {
    public void registrationAdmin(User user);

    public void registrationUser(User user);

    public void setUserRoles(User user, String[] roles);
}
