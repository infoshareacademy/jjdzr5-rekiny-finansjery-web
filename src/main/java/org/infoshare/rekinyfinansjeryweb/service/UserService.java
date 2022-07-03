package org.infoshare.rekinyfinansjeryweb.service;

import org.infoshare.rekinyfinansjeryweb.dto.ExchangeRateDTO;
import org.infoshare.rekinyfinansjeryweb.dto.FiltrationSettingsDTO;
import org.infoshare.rekinyfinansjeryweb.dto.SaveOfFiltrationSettingsDTO;
import org.infoshare.rekinyfinansjeryweb.dto.user.CreateUserFormDTO;
import org.infoshare.rekinyfinansjeryweb.dto.user.UserCurrencyDTO;
import org.infoshare.rekinyfinansjeryweb.dto.user.UserDTO;
import org.infoshare.rekinyfinansjeryweb.entity.Currency;
import org.infoshare.rekinyfinansjeryweb.entity.user.*;
import org.infoshare.rekinyfinansjeryweb.repository.CurrencyRepository;
import org.infoshare.rekinyfinansjeryweb.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    SearchAndFiltrationService searchAndFiltrationService;
    @Autowired
    CurrentRatesService currentRatesService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new MyUserPrincipal(user);
    }

    public UserDTO getUserDTO(){
        MyUserPrincipal userDetails = getUserPrincipal();
        return modelMapper.map(userDetails.getUser(), UserDTO.class);
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public Boolean addUser(CreateUserFormDTO createUserFormDTO){
        User user = modelMapper.map(createUserFormDTO, User.class);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(Set.of(UserEnum.ROLE_USER));
        user.setCreatedAt(LocalDateTime.now());
        user.setEnabled(true);

        return userRepository.save(user) != null;
    }

    public User updateUser(User user){
        MyUserPrincipal userDetails = getUserPrincipal();
        userDetails.setBillingCurrency(user.getBillingCurrency());
        return userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public Set<UserCurrencyDTO> getMyCurrencies() {
        User user = getUser();
        return user.getMyCurrencies()
                .stream().map(e ->  modelMapper.map(e, UserCurrencyDTO.class))
                .collect(Collectors.toSet());
    }

    public boolean bidCurrency(String currency, double amount){
        if (!subtractCurrency(getUser(), currency, amount)) return false;
        return updateUser(getUser()) != null;
    }

    private boolean subtractCurrency(User user, String currency, double amount) {
        ExchangeRateDTO exchangeRate = currentRatesService.getCurrencyOfLastExchangeRates(currency);
           return addToBillingCurrency(user, exchangeRate, amount) &&
                   addCurrencyHistory(user, currency, amount, exchangeRate.getBidPrice(), OperationEnum.BID);
    }

    private boolean addToBillingCurrency(User user, ExchangeRateDTO exchangeRate, double amount) {
        double billingCurrency = exchangeRate.getBidPrice() * amount;
        user.setBillingCurrency(user.getBillingCurrency() + billingCurrency);
        return true;
    }

    public boolean askCurrency(String currency, double amount){
        if (!addCurrency(getUser(), currency, amount)) return false;
        return updateUser(getUser()) != null;
    }
    
    private boolean addCurrency(User user, String currency, double amount) {
        ExchangeRateDTO exchangeRate = currentRatesService.getCurrencyOfLastExchangeRates(currency);
        return subtractFromBillingCurrency(user, exchangeRate, amount) &&
            addCurrencyHistory(user, currency, amount, exchangeRate.getAskPrice(), OperationEnum.ASK);
    }

    private boolean subtractFromBillingCurrency(User user, ExchangeRateDTO exchangeRate, double amount) {
        double billingCurrency = exchangeRate.getAskPrice() * amount;
        if (user.getBillingCurrency() < billingCurrency) return false;
        user.setBillingCurrency(user.getBillingCurrency() - billingCurrency);
        return true;
    }

    private boolean addCurrencyHistory(User user, String currency, double amount, double exchangeRate, OperationEnum operationEnum) {
        Currency code = currencyRepository.findByCode(currency);
        UserCurrency userCurrency = getUserCurrency(user, code);
        if (operationEnum == OperationEnum.ASK) {
            userCurrency.setAmount(userCurrency.getAmount() + amount);
        } else if (operationEnum == OperationEnum.BID && userCurrency.getAmount() >= amount){
            userCurrency.setAmount(userCurrency.getAmount() - amount);
        } else return false;
        userCurrency.setUser(user);
        userCurrency.setCurrency(code);
        addOperationHistory(amount, exchangeRate, operationEnum, userCurrency);
        user.getMyCurrencies().add(userCurrency);
        return true;
    }

    private void addOperationHistory(double amount, double exchangeRate, OperationEnum operationEnum, Object whereSave ) {
        List<OperationHistory> operationHistoryList = new ArrayList<>();
        OperationHistory operationHistory = new OperationHistory(LocalDateTime.now(), operationEnum, exchangeRate, amount);
        if (operationEnum.equals(OperationEnum.PAYCHECK) || operationEnum.equals(OperationEnum.PAYMENT)) {
            operationHistoryList = ((User) whereSave).getHistoryList();
            operationHistory.setUser((User) whereSave);
        }
        else if (operationEnum.equals(OperationEnum.ASK) || operationEnum.equals(OperationEnum.BID)) {
            operationHistoryList = ((UserCurrency) whereSave).getHistoryList();
            operationHistory.setUserCurrency((UserCurrency) whereSave);
        }
        operationHistoryList.add(operationHistory);
    }

    public boolean paymentToWallet(double amount) {
        User user = getUser();
        user.setBillingCurrency(user.getBillingCurrency() + amount);
        addOperationHistory(amount,1,OperationEnum.PAYMENT, user);
        return updateUser(user) != null;
    }

    public boolean withdrawalFromWallet(double amount){
        User user = getUser();
        double balance = user.getBillingCurrency() - amount;
        if (balance < 0) return false;
        user.setBillingCurrency(balance);
        addOperationHistory(amount,1,OperationEnum.PAYCHECK, user);
        return updateUser(user) != null;
    }

    public boolean emailExists(final String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Transactional
    public List<CurrencyOperationHistory> getHistoryOperation() {
        List<CurrencyOperationHistory> currencyOperationHistories = new ArrayList<>();
        User user = getUser();
        user.getHistoryList().
                forEach(e -> currencyOperationHistories.add(new CurrencyOperationHistory(e, "PLN")));

        user.getMyCurrencies().forEach(e -> e.getHistoryList().forEach(h -> currencyOperationHistories.add(new CurrencyOperationHistory(h, e.getCurrency().getCode()))));

        return sortHistory(currencyOperationHistories);
    }

    @Transactional
    public List<CurrencyOperationHistory> getHistoryOperation(String code) {
        Currency currency = currencyRepository.findByCode(code);
        User user = getUser();
        return sortHistory(
                convertFromOperationHistory(
                        getHistoryList(user.getMyCurrencies(), currency),currency));
    }
    private User getUser(){
        return userRepository.findById(getUserDTO().getId());
    }

    private List<OperationHistory> getHistoryList(Set<UserCurrency> userCurrencySet, Currency currency) {
        for (UserCurrency userCurrency :userCurrencySet){
            if (userCurrency.getCurrency().getCode().equals(currency.getCode())){
                return userCurrency.getHistoryList();
            }
        }
        return new ArrayList<>();
    }

    private UserCurrency getUserCurrency(User user, org.infoshare.rekinyfinansjeryweb.entity.Currency currency) {
        for (UserCurrency userCurrency :user.getMyCurrencies()){
            if (userCurrency.getCurrency().getCode().equals(currency.getCode())){
                return userCurrency;
            }
        }
        return new UserCurrency();
    }

    private List<CurrencyOperationHistory> convertFromOperationHistory(List<OperationHistory> operationHistory, Currency currency) {
        List<CurrencyOperationHistory> currencyOperationHistories = new ArrayList<>();
        operationHistory.forEach(h -> currencyOperationHistories.add(new CurrencyOperationHistory(h, currency.getCode())));
        return currencyOperationHistories;
    }

    private List<CurrencyOperationHistory> sortHistory(List<CurrencyOperationHistory> currencyOperationHistories) {
        return currencyOperationHistories.stream()
                .sorted(((o1, o2) -> o2.getDateOperation().compareTo(o1.getDateOperation())))
                .collect(toList());
    }

    @Transactional
    public boolean savedFiltrationSettings(SaveOfFiltrationSettingsDTO filtrationSettings){
        User user = getUser();
        user.getSavedFiltrationSettings().put(filtrationSettings.getPreferenceName(), modelMapper.map(filtrationSettings, FiltrationSettings.class));
        return updateUser(user) != null;
    }

    @Transactional
    public List<Map.Entry<String, FiltrationSettingsDTO>> getListOfSavedFiltrationSettings(){
        return mapperToFiltrationSettingsDTO()
                .entrySet()
                .stream()
                .toList();
    }

    @Transactional
    public boolean removeSavedFiltrationSettings(String key) {
        User user = getUser();
        user.getSavedFiltrationSettings().remove(key);
        return updateUser(user) != null;
    }

    @Transactional
    public Map<String, FiltrationSettingsDTO> getSavedFiltrationSettings(){
        return mapperToFiltrationSettingsDTO();
    }

    private Map<String, FiltrationSettingsDTO> mapperToFiltrationSettingsDTO(){
        Map<String, FiltrationSettingsDTO> filtrationSettingsDTO = new HashMap<>();
        getUser().getSavedFiltrationSettings()
                .entrySet()
                .forEach( e -> filtrationSettingsDTO.put(e.getKey(),modelMapper.map(e.getValue(),FiltrationSettingsDTO.class)));
        return filtrationSettingsDTO;
    }

    //todo del
    private Map<String, FiltrationSettings> mapperToFiltrationSettings(Map<String, FiltrationSettingsDTO> filtrationSettingsDTO){
        Map<String, FiltrationSettings> filtrationSettings = new HashMap<>();
        filtrationSettingsDTO
                .entrySet()
                .forEach( e -> filtrationSettings.put(e.getKey(),modelMapper.map(e.getValue(),FiltrationSettings.class)));
        return filtrationSettings;
    }

    private MyUserPrincipal getUserPrincipal(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (MyUserPrincipal) authentication.getPrincipal();
    }


}
