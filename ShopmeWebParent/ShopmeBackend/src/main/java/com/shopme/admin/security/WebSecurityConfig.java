package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder PasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new ShopmeUserDetailsService();
    }

    // tell spring that the authentication will base on the database
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        // set the user detail service on above
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(PasswordEncoder());
        return authenticationProvider;
    }

    // new syntax for Spring 6
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(configurer -> {
                    configurer
                            // permit user to request the images and  web jars like bootstrap and jquery
                            .requestMatchers("/images/**").permitAll()
                            .requestMatchers("/webjars/**").permitAll()
                            .requestMatchers("/js/**").permitAll()
                            .anyRequest().authenticated();
                })
                .formLogin(form -> {
                    form.loginPage("/login")
                            .usernameParameter("email")
                            .defaultSuccessUrl("/")
                            .permitAll();
                })
                .logout(configurer -> {
                    configurer.permitAll();
                })
                // remember me keep user logged in even when we quit the browser
                // after the server restart. The new key for the cookie will be generated
                // so we need to specify a fixed key so when we restart the server. The cookie remain
                .rememberMe(remember -> {
                    remember.key("This_is_the_fixed_key_for_remember_me")
                            // set cookie for 1 week
                            .tokenValiditySeconds(7 * 24 * 60 * 60);
                });
        return http.build();
    }


}
