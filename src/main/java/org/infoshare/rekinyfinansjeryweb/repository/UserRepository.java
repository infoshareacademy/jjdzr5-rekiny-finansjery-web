package org.infoshare.rekinyfinansjeryweb.repository;

import org.infoshare.rekinyfinansjeryweb.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {

    private static User user;
    private static List<User> usersRepository = new ArrayList<>();
    
    public static List<User> getUserRepository() {
        if (usersRepository.size() == 0) {
            fillRepositoryWithDefaults();
        }
        return usersRepository;
    }

    public static User getUser(){
        return user;
    }

    public static void singOut(){
        user = null;
    }

    public static void setUser(User setUser){
        user = setUser;
    }

    public static User findUserById(long id){
        return UserRepository.getUserRepository().stream()
                .filter( e -> e.getId() == id)
                .findFirst().orElse(new User());
    }

    public static User findByEmailAddress(String email){
        return UserRepository.getUserRepository().stream()
                .filter( e -> e.getEmail().equalsIgnoreCase(email))
                .findFirst().orElse(new User());
    }

    public static void save(User user){
        usersRepository.add(user);
    }

    public static void update(User user){
        User oldUser = UserRepository.findUserById(user.getId());
        UserRepository.getUserRepository().remove(oldUser);
        UserRepository.getUserRepository().add(user);
    }

    public static void delete(long id){
        usersRepository.removeIf(user -> user.getId() == id);
    }


    private static void fillRepositoryWithDefaults() {
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("admin@admin.pl");
        user1.setPassword("admin");
        user1.setRole(UserEnum.ADMIN);
        user1.setName("Maciej");
        user1.setLastname("Chyła");
        user1.setBillingCurrency(155222.86);
        Map<String, UserCurrency> currencyMap = new HashMap<>();
        List<OperationHistory> operationHistoryList1 = new ArrayList<>();
        operationHistoryList1.add(new OperationHistory(LocalDateTime.now().minusDays(15), OperationEnum.ASK, 4.3193, 50.99));
        operationHistoryList1.add(new OperationHistory(LocalDateTime.now().minusDays(10), OperationEnum.ASK, 4.3084, 50.00));
        operationHistoryList1.add(new OperationHistory(LocalDateTime.now().minusDays(5), OperationEnum.BID, 4.2341, 50.00));
        UserCurrency currency1 = new UserCurrency(100.99,operationHistoryList1);
        currencyMap.put("USD", currency1);
        List<OperationHistory> operationHistoryList2 = new ArrayList<>();
        operationHistoryList2.add(new OperationHistory(LocalDateTime.now().minusDays(30), OperationEnum.ASK, 4.6025, 250.50));
        operationHistoryList2.add(new OperationHistory(LocalDateTime.now().minusDays(18), OperationEnum.ASK, 4.6021, 250.00));
        UserCurrency currency2 = new UserCurrency(500.50,operationHistoryList2);
        currencyMap.put("EUR", currency2);
        user1.setMyCurrencies(currencyMap);
        user1.setCreated(LocalDateTime.now());
        usersRepository.add(user1);
    }
}
