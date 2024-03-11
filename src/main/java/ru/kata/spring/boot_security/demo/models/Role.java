package ru.kata.spring.boot_security.demo.models;

import ru.kata.spring.boot_security.demo.models.User;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity(name = "role")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users_with_role;

    public Role(){

    }

    public Role(String name_of_role) {
        this.name = name_of_role;
    }

    public Role(Long id, String name_of_role, Collection<User> users_with_role) {
        this.id = id;
        this.name = name_of_role;
        this.users_with_role = users_with_role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String nameOfRole) {
        this.name = nameOfRole;
    }

    public Collection<User> getUsers_with_role() {
        return users_with_role;
    }

    public void setPeople(Collection<User> users_with_role) {
        this.users_with_role = users_with_role;
    }


    @Override
    public String getAuthority() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id) && Objects.equals(name, role.name) && Objects.equals(users_with_role, role.users_with_role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, users_with_role);
    }

    @Override
    public String toString() {
        return name.substring("ROLE_".length());
    }
}
