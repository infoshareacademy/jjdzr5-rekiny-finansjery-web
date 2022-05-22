package org.infoshare.rekinyfinansjeryweb.service;

import com.infoshareacademy.domain.ExchangeRate;
import org.infoshare.rekinyfinansjeryweb.data.*;
import org.infoshare.rekinyfinansjeryweb.formData.FiltrationSettings;
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
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    SearchService searchService;
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

    public boolean addUser(User user){
        List<User> users = userRepository.getUserRepository();
        Optional<User> max = users.stream().max(Comparator.comparingLong(User::getId));
        user.setId(max.orElse(new User()).getId()+1);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(Set.of(UserEnum.ROLE_USER));
        Map<String, UserCurrency> currencyMap = new HashMap<>();
        user.setHistoryList(new ArrayList<>());
        user.setSavedFiltrationSettings(new HashMap<>());
        user.setMyCurrencies(currencyMap);
        user.setCreated(LocalDateTime.now());
        user.setEnabled(true);
        return userRepository.save(user);
    }

    public boolean updateUser(User user){
        return userRepository.update(user);
    }

    public void deleteUser(long id) {
        userRepository.delete(id);
    }

    public boolean bidCurrency(String currency, double amount){
        User user = getUser();
        if (!subtractCurrency(user, currency, amount)) return false;
        return updateUser(user);
    }

    private boolean subtractCurrency(User user, String currency, double amount) {
        ExchangeRate exchangeRate = searchService.getCurrencyOfLastExchangeRates(currency);
        if (addCurrencyHistory(user, currency, amount, exchangeRate.getBid(), OperationEnum.BID)){
            addToBillingCurrency(user, exchangeRate, amount);
            return true;
        }
        return false;
    }

    private void addToBillingCurrency(User user, ExchangeRate exchangeRate, double amount) {
        double billingCurrency = exchangeRate.getBid() * amount;
        user.setBillingCurrency(user.getBillingCurrency() + billingCurrency);
    }

    public boolean askCurrency(String currency, double amount){
        User user = getUser();
        if (!addCurrency(user, currency, amount)) return false;
        return updateUser(user);
    }
    
    private boolean addCurrency(User user, String currency, double amount) {
        ExchangeRate exchangeRate = searchService.getCurrencyOfLastExchangeRates(currency);
        if (subtractFromBillingCurrency(user, exchangeRate, amount) &&
            addCurrencyHistory(user, currency, amount, exchangeRate.getAsk(), OperationEnum.ASK)) return true;
        return false;
    }

    private boolean subtractFromBillingCurrency(User user, ExchangeRate exchangeRate, double amount) {
        double billingCurrency = exchangeRate.getAsk() * amount;
        if (user.getBillingCurrency() < billingCurrency) return false;
        user.setBillingCurrency(user.getBillingCurrency() - billingCurrency);
        return true;
    }

    private boolean addCurrencyHistory(User user, String currency, double amount, double exchangeRate, OperationEnum operationEnum) {
        user.getMyCurrencies().putIfAbsent(currency, new UserCurrency(0.00, new ArrayList<>()));
        UserCurrency userCurrency = user.getMyCurrencies().get(currency);
        if (operationEnum == OperationEnum.ASK) {
            userCurrency.setAmount(userCurrency.getAmount() + amount);
        } else if (operationEnum == OperationEnum.BID && userCurrency.getAmount() >= amount){
            userCurrency.setAmount(userCurrency.getAmount() - amount);
        } else return false;
        List<OperationHistory> historyList = userCurrency.getHistoryList();
        addOperationHistory(amount, exchangeRate, operationEnum, historyList);
        user.getMyCurrencies().put(currency, userCurrency);
        return true;
    }

    private void addOperationHistory(double amount, double exchangeRate, OperationEnum operationEnum, List<OperationHistory> historyList ) {
        historyList.add(new OperationHistory(LocalDateTime.now(), operationEnum, exchangeRate, amount));
    }

    public boolean paymentToWallet(double amount) {
        User user = getUser();
        user.setBillingCurrency(user.getBillingCurrency() + amount);
        List<OperationHistory> historyList = user.getHistoryList();
        addOperationHistory(amount,1,OperationEnum.PAYMENT, historyList);
        updateUser(user);
        return true;
    }

    public boolean withdrawalFromWallet(double amount){
        User user = getUser();
        user.setBillingCurrency(user.getBillingCurrency() - amount);
        List<OperationHistory> historyList = user.getHistoryList();
        addOperationHistory(amount,1,OperationEnum.PAYCHECK, historyList);
        updateUser(user);
        return true;
    }

    public boolean emailExists(final String email) {
        return userRepository.findByEmailAddress(email).getEmail() != null;
    }

    public List<CurrencyOperationHistory> getHistoryOperation() {
        List<CurrencyOperationHistory> currencyOperationHistories = new ArrayList<>();
        User user = getUser();
        user.getHistoryList().forEach(e -> currencyOperationHistories.add(new CurrencyOperationHistory(e, "PLN")));
        user.getMyCurrencies()
                .entrySet().
                forEach(e -> e.getValue()
                        .getHistoryList()
                        .forEach( history -> currencyOperationHistories.add(new CurrencyOperationHistory(history, e.getKey()))));

        return sortHistory(currencyOperationHistories);
    }

    public List<CurrencyOperationHistory> getHistoryOperation(String code) {
        List<CurrencyOperationHistory> currencyOperationHistories = new ArrayList<>();
        User user = getUser();
        user.getMyCurrencies().get(code).getHistoryList()
                        .forEach( history -> currencyOperationHistories.add(new CurrencyOperationHistory(history, code)));

        return sortHistory(currencyOperationHistories);
    }

    private List<CurrencyOperationHistory> sortHistory(List<CurrencyOperationHistory> currencyOperationHistories) {
        return currencyOperationHistories.stream()
                .sorted(((o1, o2) -> o2.getDateOpreation().compareTo(o1.getDateOpreation())))
                .collect(Collectors.toList());
    }

    public List<Map.Entry<String, FiltrationSettings>> getListOfSavedFiltrationSettings(MyUserPrincipal principal){
        return principal
                .getUser()
                .getSavedFiltrationSettings()
                .entrySet()
                .stream()
                .toList();
    }
}
