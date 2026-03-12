package com.edu.silva.users.services;

import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AuthService extends UserDetailsService {
    UserDetails loadUserByUsername(@NotNull String username) throws UsernameNotFoundException;
}
