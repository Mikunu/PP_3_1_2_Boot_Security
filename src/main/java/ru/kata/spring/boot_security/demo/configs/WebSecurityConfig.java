package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import ru.kata.spring.boot_security.demo.services.MyUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final MyUserDetailsService myUserDetailsService;
    private final SuccessUserHandler successUserHandler;

    public WebSecurityConfig(MyUserDetailsService myUserDetailsService, SuccessUserHandler successUserHandler) {
        this.myUserDetailsService = myUserDetailsService;
        this.successUserHandler = successUserHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/auth/login", "/", "/error", "/auth/registration").permitAll()
                .anyRequest().hasAnyRole("USER", "ADMIN")
                .and()
                .formLogin().successHandler(successUserHandler)
                .loginPage("/auth/login")
                .loginProcessingUrl("/process_login")
                .failureUrl("/auth/login?error")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/auth/login")
                .permitAll();
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}