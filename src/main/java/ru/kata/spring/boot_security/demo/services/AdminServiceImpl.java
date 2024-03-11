package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByName(String firstName) {
        Optional<User> user = userRepository.findByFirstName(firstName);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User: " + firstName + " not found");
        }
        return user.get();
    }


    @Override
    public void updateUser(User person, List<String> roles) {
        User beforeUpdate = userRepository.getById(person.getId());
        person.setPassword(beforeUpdate.getPassword());
        Set<Role> roleSet = roles.stream()
                .map(Long::valueOf)
                .map(roleRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        person.setRoles(roleSet);
        userRepository.save(person);
    }

    @Override
    public void removeUser(Long id) {
        userRepository.delete(userRepository.getById(id));
    }

    @Override
    public User findOneById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User with ID: " + id + " not found");
        }
        return user.get();
    }
}
