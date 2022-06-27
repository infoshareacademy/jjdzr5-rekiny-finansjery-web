package org.infoshare.rekinyfinansjeryweb.service;

import org.infoshare.rekinyfinansjeryweb.entity.user.User;
import org.infoshare.rekinyfinansjeryweb.entity.user.UserEnum;
import org.infoshare.rekinyfinansjeryweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CreateUserService {

    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;

    public CreateUserService(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public void createUsers(){
        if (userRepository.count() == 0) {
            createAdmin();
            createUser();
        }
    }
    private void createAdmin(){
        User user = new User();
        user.setEmail("admin@admin.pl");
        user.setPassword(encoder.encode("admin"));
        user.setRole(Set.of(UserEnum.ROLE_ADMIN, UserEnum.ROLE_USER));
        user.setName("REKINY");
        user.setLastname("FINANSJERY");
        user.setBillingCurrency(155222.86);
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    private void createUser(){
        User user = new User();
        user.setEmail("user@user.pl");
        user.setPassword(encoder.encode("user"));
        user.setRole(Set.of(UserEnum.ROLE_USER));
        user.setName("Jan");
        user.setLastname("Nowak");
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}
