package com.edu.silva.users.domain.seed;

import com.edu.silva.common.enums.Status;
import com.edu.silva.users.domain.entities.Company;
import com.edu.silva.users.domain.entities.User;
import com.edu.silva.users.domain.enums.UserRole;
import com.edu.silva.users.domain.enums.UserStatus;
import com.edu.silva.users.repositories.CompanyRepository;
import com.edu.silva.users.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.UserDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminUserSeeder {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    @Value(value = "${admin.email}")
    private String email;

    @Value(value = "${admin.password}")
    private String password;

    @PostConstruct
    public void seed() {
        UserDetails details = userRepository.findByEmail(email);
        if (details != null) {
            return;
        }

        Company company = new Company();
        company.setName("EHS Admin");
        company.setStatus(Status.ACTIVE);

        companyRepository.save(company);

        User admin = new User();
        admin.setUsername("Admin");
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setStatus(UserStatus.OK);
        admin.setRoles(List.of(UserRole.ADMIN));
        admin.setCompany(company);

        userRepository.save(admin);

        company.setOwner(admin);
        company.getUsers().add(admin);

        companyRepository.save(company);
    }
}