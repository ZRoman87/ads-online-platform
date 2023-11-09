package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder encoder;

    public AuthServiceImpl(UserDetailsServiceImpl userDetailsService,
                           PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.encoder = passwordEncoder;
    }
    @Override
    public boolean login(String userName, String password) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

        if (userDetails.getUsername() == null) {
            return false;
        } else {
            return encoder.matches(password, userDetails.getPassword());
        }

    }

}
