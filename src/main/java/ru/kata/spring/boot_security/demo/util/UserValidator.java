package ru.kata.spring.boot_security.demo.util;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private final UserService userService;

    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (userService.loadUserByUsername(user.getFirstName()).isPresent()
                && !userService.loadUserByUsername(user.getFirstName()).orElse(null).equals(target)) {
            errors.rejectValue("firstName", "", "User with this name already exists");
        }
    }
}
