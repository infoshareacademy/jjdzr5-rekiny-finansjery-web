package org.infoshare.rekinyfinansjeryweb.service;

import org.infoshare.rekinyfinansjeryweb.data.User;
import org.infoshare.rekinyfinansjeryweb.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    public void addUser(User user){
        List<User> users = UserRepository.getUserRepository();
        Optional<User> max = users.stream().max(Comparator.comparingLong(User::getId));
        user.setId(max.orElse(new User()).getId()+1);
        user.setCreated(LocalDateTime.now());
        UserRepository.save(user);
    }

    public User getUser(){
        return UserRepository.getUser();
    }

    public List<User> getUsers(){
        return UserRepository.getUserRepository();
    }

    public void updateUser(User user){
        UserRepository.update(user);
    }

    public void deleteUser(long id) {
        UserRepository.delete(id);
    }

    public boolean loginUser(String email, String password) {
        User user = UserRepository.findByEmailAddress(email);
        if (!user.equals(null) && user.getPassword().equals(password)) {
            UserRepository.setUser(user);
            return true;
        }
        return false;
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
