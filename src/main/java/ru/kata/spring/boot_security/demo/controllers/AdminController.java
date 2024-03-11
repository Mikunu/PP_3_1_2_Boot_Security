package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.security.MyUserDetails;
import ru.kata.spring.boot_security.demo.services.AdminService;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.util.UserValidator;
import ru.kata.spring.boot_security.demo.util.RoleValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final RoleService roleService;
    private final UserValidator userValidator;
    private final RoleValidator roleValidator;

    @Autowired
    public AdminController(AdminService adminService, RoleService roleService, UserValidator userValidator, RoleValidator roleValidator) {
        this.adminService = adminService;
        this.roleService = roleService;
        this.userValidator = userValidator;
        this.roleValidator = roleValidator;
    }

    @GetMapping("/users")
    public String getAllUsers(Model model, Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        model.addAttribute("userDetails", userDetails);
        User user = adminService.findUserByName(principal.getName());
        model.addAttribute("user", user);
        List<User> users = adminService.getAllUsers();
        model.addAttribute("userList", users);
        return "admin/users";
    }

    @GetMapping("admin/removeUser")
    public String removeUser(@RequestParam("id") Long id) {
        adminService.removeUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/updateUser")
    public String getEditUserForm(Model model, @RequestParam("id") Long id) {
        model.addAttribute("person", adminService.findOneById(id));
        model.addAttribute("roles", roleService.getRoles());
        return "admin/userUpdate";
    }

    @PostMapping("/updateUser")
    public String postEditUserForm(@ModelAttribute("person") @Valid User user,
                                   BindingResult personBindingResult,
                                   @RequestParam(value = "roles", required = false) @Valid List<String> roles,
                                   BindingResult rolesBindingResult,
                                   RedirectAttributes redirectAttributes) {

        System.out.println();
        userValidator.validate(user, personBindingResult);
        if (personBindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorsPerson", personBindingResult.getAllErrors());
            return "/admin/userUpdate";
        }


        roleValidator.validate(roles, rolesBindingResult);
        if (rolesBindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorsRoles", rolesBindingResult.getAllErrors());
            return "/admin/userUpdate";
        }

        adminService.updateUser(user, roles);
        return "redirect:/admin/users";
    }
}
