package com.edu.silva.users.infra.security;

import com.edu.silva.users.repositories.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OwnerSecurity {

    private final CompanyRepository repository;

    public boolean isOwnerOrAdmin(UUID companyId) {

        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        assert auth != null;
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_ADMIN"));

        if (isAdmin) return true;

        String username = auth.getName();

        return repository.findById(companyId)
                .map(c -> c.getOwner().getUsername().equals(username))
                .orElse(false);
    }
}
