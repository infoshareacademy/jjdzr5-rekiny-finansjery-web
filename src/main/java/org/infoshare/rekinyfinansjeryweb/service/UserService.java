package org.infoshare.rekinyfinansjeryweb.service;

import org.infoshare.rekinyfinansjeryweb.data.MyUserPrincipal;
import org.infoshare.rekinyfinansjeryweb.data.User;
import org.infoshare.rekinyfinansjeryweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

//@Component
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

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
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByEmailAddress(userDetails.getUsername());
    }

    public List<User> getUsers(){
        return userRepository.getUserRepository();
    }

    public void addUser(User user){
        List<User> users = userRepository.getUserRepository();
        Optional<User> max = users.stream().max(Comparator.comparingLong(User::getId));
        user.setId(max.orElse(new User()).getId()+1);
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
}
