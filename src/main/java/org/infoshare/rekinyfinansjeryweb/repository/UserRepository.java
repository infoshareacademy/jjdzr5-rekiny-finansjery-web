package org.infoshare.rekinyfinansjeryweb.repository;

import org.infoshare.rekinyfinansjeryweb.data.*;
import org.infoshare.rekinyfinansjeryweb.formData.FiltrationSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class UserRepository {

    @Autowired
    private PasswordEncoder encoder;

    private List<User> usersRepository = new ArrayList<>();
    
    public List<User> getUserRepository() {
        if (usersRepository.size() == 0) {
            fillRepositoryWithDefaults();
        }
        return usersRepository;
    }

    public User findUserById(long id){
        return getUserRepository().stream()
                .filter( e -> e.getId() == id)
                .findFirst().orElse(new User());
    }

    public User findByEmailAddress(String email){
        return getUserRepository().stream()
                .filter( e -> e.getEmail().equalsIgnoreCase(email))
                .findFirst().orElse(new User());
    }

    public User findByUsername(String username){
        return getUserRepository().stream()
                .filter( e -> e.getEmail().equalsIgnoreCase(username))
                .findFirst().orElse(new User());
    }

    public boolean save(User user){
        return usersRepository.add(user);
    }

    public boolean update(User user){
        User oldUser = findUserById(user.getId());
        usersRepository.remove(oldUser);
        return usersRepository.add(user);
    }

    public void delete(long id){
        usersRepository.removeIf(user -> user.getId() == id);
    }

    private void fillRepositoryWithDefaults() {
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("admin@admin.pl");
        user1.setPassword(encoder.encode("admin"));
        Set<UserEnum> roleSet = new HashSet<>();
        roleSet.add(UserEnum.ROLE_ADMIN);
        roleSet.add(UserEnum.ROLE_USER);
        user1.setRole(roleSet);
        user1.setName("REKINY");
        user1.setLastname("FINANSJERY");
        user1.setBillingCurrency(155222.86);
        user1.setHistoryList(new ArrayList<>());
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
        List<OperationHistory> operationHistoryList3 = new ArrayList<>();
        operationHistoryList3.add(new OperationHistory(LocalDateTime.now().minusDays(60), OperationEnum.ASK, 4.6025, 1.00));
        operationHistoryList3.add(new OperationHistory(LocalDateTime.now().minusDays(98), OperationEnum.ASK, 4.6021, 1.00));
        UserCurrency currency3 = new UserCurrency(2.00,operationHistoryList3);
        currencyMap.put("RFC", currency3);
        user1.setMyCurrencies(currencyMap);
        user1.setSavedFiltrationSettings(new HashMap<>());
        user1.setEnabled(true);
        user1.setCreated(LocalDateTime.now());
        usersRepository.add(user1);

        User user2 = new User();
        user2.setId(2);
        user2.setEmail("user@user.pl");
        user2.setPassword(encoder.encode("user"));
        Set<UserEnum> roleSet2 = new HashSet<>();
        roleSet2.add(UserEnum.ROLE_USER);
        user2.setRole(roleSet2);
        user2.setName("Jan");
        user2.setLastname("Kowalski");
        user2.setBillingCurrency(1000);
        List<OperationHistory> historyList = new ArrayList<>();
        user2.setHistoryList(historyList);
        Map<String, UserCurrency> currencyMap2 = new HashMap<>();
        user2.setMyCurrencies(currencyMap2);

        Map<String, FiltrationSettings> map = new HashMap<>();
        FiltrationSettings dolar = new FiltrationSettings();
        dolar.setCurrency(Arrays.asList("USD"));
        dolar.setEffectiveDateMin(LocalDate.of(2022,05,01));
        map.put("Dolary", dolar);
        map.put("Dolarki", dolar);
        user2.setSavedFiltrationSettings(map);


        user2.setEnabled(true);
        user2.setCreated(LocalDateTime.now());
        usersRepository.add(user2);
    }
}
