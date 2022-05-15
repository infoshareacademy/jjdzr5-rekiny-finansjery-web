package org.infoshare.rekinyfinansjeryweb.service;

import org.infoshare.rekinyfinansjeryweb.data.MyUserPrincipal;
import org.infoshare.rekinyfinansjeryweb.data.User;
import org.infoshare.rekinyfinansjeryweb.data.UserCurrency;
import org.infoshare.rekinyfinansjeryweb.data.UserEnum;
import org.infoshare.rekinyfinansjeryweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByEmailAddress(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new MyUserPrincipal(user);
    }

    public User getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserPrincipal userDetails = (MyUserPrincipal) authentication.getPrincipal();
        return userDetails.getUser();
    }

    public List<User> getUsers(){
        return userRepository.getUserRepository();
    }

    public void addUser(User user){
        List<User> users = userRepository.getUserRepository();
        Optional<User> max = users.stream().max(Comparator.comparingLong(User::getId));
        user.setId(max.orElse(new User()).getId()+1);
        user.setPassword(encoder.encode(user.getPassword()));
        Set<UserEnum> roleSet = new HashSet<>();
        roleSet.add(UserEnum.ROLE_USER);
        user.setRole(roleSet);
        Map<String, UserCurrency> currencyMap = new HashMap<>();
        user.setMyCurrencies(currencyMap);
        user.setCreated(LocalDateTime.now());
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void updateUser(User user){
        userRepository.update(user);
    }

    public void deleteUser(long id) {
        userRepository.delete(id);
    }

    public boolean bidCurrency(String currency, double amount){
        //todo bidCurrency
        return true;
    }

    public boolean askCurrency(String currency, double amount){
        //todo askCurrency
        return true;
    }

    public boolean emailExists(final String email) {
        return userRepository.findByEmailAddress(email).getEmail() != null;
    }
}
