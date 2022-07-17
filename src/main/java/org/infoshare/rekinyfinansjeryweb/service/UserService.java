package org.infoshare.rekinyfinansjeryweb.service;

import org.infoshare.rekinyfinansjeryweb.entity.user.CustomOAuth2User;
import org.infoshare.rekinyfinansjeryweb.dto.ExchangeRateDTO;
import org.infoshare.rekinyfinansjeryweb.dto.FiltrationSettingsDTO;
import org.infoshare.rekinyfinansjeryweb.dto.SaveOfFiltrationSettingsDTO;
import org.infoshare.rekinyfinansjeryweb.dto.user.*;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    public EditUserDataFormDTO getEditUserData() {
        EditUserDataFormDTO editUser = modelMapper.map(getUser(), EditUserDataFormDTO.class);
        return editUser;
    }

    public boolean saveEditUser(EditUserDataFormDTO editUserDataFormDTO) {
        User user = getUser();
        user.setName(editUserDataFormDTO.getName());
        user.setLastname(editUserDataFormDTO.getLastname());
        user.setEmail(editUserDataFormDTO.getEmail());
        return updateUser(user) != null;
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public Boolean addUser(CreateUserFormDTO createUserFormDTO, AuthenticationProvider provider){
        User user = modelMapper.map(createUserFormDTO, User.class);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(Set.of(UserEnum.ROLE_USER));
        user.setAuthProvider(provider);
        user.setCreatedAt(LocalDateTime.now());
        user.setEnabled(true);
        return userRepository.save(user) != null;
    }
    public Boolean addUserOAuth2(CreateUserFormDTO createUserFormDTO, AuthenticationProvider provider){
        User user = modelMapper.map(createUserFormDTO, User.class);
        user.setRole(Set.of(UserEnum.ROLE_USER));
        user.setAuthProvider(provider);
        user.setCreatedAt(LocalDateTime.now());
        user.setEnabled(true);
        return userRepository.save(user) != null;
    }

    public User updateUser(User user){
        MyUserPrincipal userDetails = getUserPrincipal();
        userDetails.setName(user.getName());
        userDetails.setLastname(user.getLastname());
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

    public boolean bidCurrency(String currency, String amount){
        if (!subtractCurrency(getUser(), currency, amount)) return false;
        return updateUser(getUser()) != null;
    }

    private boolean subtractCurrency(User user, String currency, String amount) {
        ExchangeRateDTO exchangeRate = currentRatesService.getCurrencyOfLastExchangeRates(currency);
           return addToBillingCurrency(user, exchangeRate, amount) &&
                   addCurrencyHistory(user, currency, amount, exchangeRate.getBidPrice(), OperationEnum.BID);
    }

    private boolean addToBillingCurrency(User user, ExchangeRateDTO exchangeRate, String amount) {
        BigDecimal billingCurrency = multiplyAmount(exchangeRate.getBidPrice(), amount,  OperationEnum.BID);
        user.setBillingCurrency(addAmount(user.getBillingCurrency(), billingCurrency,  OperationEnum.BID).toString());
        return true;
    }

    public boolean askCurrency(String currency, String amount){
        if (!addCurrency(getUser(), currency, amount)) return false;
        return updateUser(getUser()) != null;
    }
    
    private boolean addCurrency(User user, String currency, String amount) {
        ExchangeRateDTO exchangeRate = currentRatesService.getCurrencyOfLastExchangeRates(currency);
        return subtractFromBillingCurrency(user, exchangeRate, amount) &&
            addCurrencyHistory(user, currency, amount, exchangeRate.getAskPrice(), OperationEnum.ASK);
    }

    private boolean subtractFromBillingCurrency(User user, ExchangeRateDTO exchangeRate, String amount) {
        BigDecimal billingCurrency = multiplyAmount(exchangeRate.getAskPrice(), amount, OperationEnum.ASK);
        BigDecimal userBillingCurrency = convertStringToBigDecimal(user.getBillingCurrency());
        if (userBillingCurrency.compareTo(billingCurrency) < 0) return false;
        user.setBillingCurrency(roundAmount(userBillingCurrency.subtract(billingCurrency), OperationEnum.ASK).toString());
        return true;
    }

    private boolean addCurrencyHistory(User user, String currency, String amount, double exchangeRate, OperationEnum operationEnum) {
        Currency code = currencyRepository.findByCode(currency);
        UserCurrency userCurrency = getUserCurrency(user, code);
        BigDecimal amountDecimal = convertStringToBigDecimal(amount);
        BigDecimal amountUserCurrency = convertStringToBigDecimal(userCurrency.getAmount());
        if (operationEnum == OperationEnum.ASK) {
            userCurrency.setAmount(roundAmount(amountUserCurrency.add(amountDecimal), operationEnum).toString());
        } else if (operationEnum == OperationEnum.BID && amountUserCurrency.compareTo(amountDecimal) >= 0){
            userCurrency.setAmount(roundAmount(amountUserCurrency.subtract(amountDecimal), operationEnum).toString());
        } else return false;
        userCurrency.setUser(user);
        userCurrency.setCurrency(code);
        addOperationHistory(amount, exchangeRate, operationEnum, userCurrency);
        user.getMyCurrencies().add(userCurrency);
        return true;
    }

    private void addOperationHistory(String amount, double exchangeRate, OperationEnum operationEnum, Object whereSave ) {
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

    public boolean paymentToWallet(String amount) {
        User user = getUser();
        user.setBillingCurrency(addAmount(user.getBillingCurrency(), amount, OperationEnum.PAYMENT).toString());
        addOperationHistory(amount,1,OperationEnum.PAYMENT, user);
        return updateUser(user) != null;
    }

    public boolean withdrawalFromWallet(String amount){
        User user = getUser();
        BigDecimal balance = subtractAmount(user.getBillingCurrency(),amount, OperationEnum.PAYCHECK);
        if (balance.compareTo(new BigDecimal("0")) < 0) return false;
        user.setBillingCurrency(balance.toString());
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

    private MyUserPrincipal getUserPrincipal(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        User user = null;
        if (principal instanceof CustomOAuth2User) {
            String email = ((CustomOAuth2User) principal).getEmail();
            user = userRepository.findByEmail(email);
        } else if (principal instanceof MyUserPrincipal) {
            user = ((MyUserPrincipal) principal).getUser();
        }
        return new MyUserPrincipal(user);
    }

    private BigDecimal convertStringToBigDecimal(String s) {
        return new BigDecimal(s);
    }

    private BigDecimal roundAmount(BigDecimal bigDecimal, OperationEnum operation) {
        RoundingMode roundingMode;
        switch (operation) {
            case ASK -> roundingMode = RoundingMode.HALF_UP;
            case BID -> roundingMode = RoundingMode.DOWN;
            default -> roundingMode = RoundingMode.HALF_DOWN;
        }
        return bigDecimal.setScale(2, roundingMode);
    }

    private BigDecimal addAmount(String amount1, String amount2, OperationEnum operation) {
        return roundAmount(convertStringToBigDecimal(amount1).add(convertStringToBigDecimal(amount2)), operation);
    }
    private BigDecimal addAmount(String amount1, BigDecimal amount2, OperationEnum operation) {
        return roundAmount(convertStringToBigDecimal(amount1).add(amount2),  operation);
    }

    private BigDecimal subtractAmount(String amount1, String amount2, OperationEnum operation) {
        return roundAmount(convertStringToBigDecimal(amount1).subtract(convertStringToBigDecimal(amount2)), operation);
    }

    private BigDecimal multiplyAmount(Double amount1, String amount2, OperationEnum operation) {
        return roundAmount(BigDecimal.valueOf(amount1).multiply(convertStringToBigDecimal(amount2)), operation);
    }


    public boolean changePassword(ChangeUserPasswordFormDTO newPassword) {
        User user = getUser();
        boolean result = false;
        if (checkPassword(user.getPassword(), newPassword.getOldPassword(), user.getAuthProvider())){

            user.setPassword(encoder.encode(newPassword.getNewPassword()));
            result = updateUser(user) != null;
        }
        return result;
    }

    private boolean checkPassword(String userPassword, String enteredPassword, AuthenticationProvider provider) {
        if (userPassword == null && !provider.equals(AuthenticationProvider.LOCAL)) {
            return true;
        }
        return encoder.matches(enteredPassword, userPassword);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
