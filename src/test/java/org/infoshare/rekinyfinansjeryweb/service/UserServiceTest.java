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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.infoshare.rekinyfinansjeryweb.entity.user.UserEnum.ROLE_USER;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    CurrentRatesService currentRatesService;
    @Mock
    CurrencyRepository currencyRepository;
    @InjectMocks
    UserService userService;
    @Spy
    PasswordEncoder encoder = new BCryptPasswordEncoder(11);
    User user;
    UUID id;
    @Spy
    public ModelMapper modelMapper;
    {
        modelMapper = new ModelMapper();
    }

    @BeforeEach
    void setup() {
        id = UUID.fromString("a7941fc3-5855-4a87-a1f5-65adb0f413fe");
        user = new User();
        user.setId(id);
        user.setName("Test");
        user.setLastname("Testowy");
        user.setEmail("test@test.pl");
        user.setPassword(encoder.encode("test"));
        user.setRole(Set.of(ROLE_USER));
        user.setCreatedAt(LocalDateTime.now());
        user.setEnabled(true);
    }

    @Test
    void loadUserByUsername_returnsUserDetails_givenUsername() {
        // given
        String usernameResult = "test@test.pl";
        when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);
        // when
        UserDetails userDetails = userService.loadUserByUsername(usernameResult);
        // then
        verify(userRepository, Mockito.times(1)).findByEmail(usernameResult);
        assertThat(userDetails.getUsername()).isEqualTo(usernameResult);
    }

    @Test
    void getUserDTO_resultsUserDTO_givenGetUserDTO() {
        // given
        loginUser();
        // when
        UserDTO userDTO = userService.getUserDTO();
        // then
        assertThat(userDTO.getId()).isNotNull();
        assertThat(userDTO.getId()).isEqualTo(user.getId());
        assertThat(userDTO.getEmail()).isEqualTo(user.getEmail());
        assertThat(userDTO.getName()).isEqualTo(user.getName());
        assertThat(userDTO.getLastname()).isEqualTo(user.getLastname());
        assertThat(userDTO.getBillingCurrency()).isEqualTo(user.getBillingCurrency());
    }

    @Test
    void getUsers_returnsUsersList_givenGetUsers() {
        // given
        when(userRepository.findAll()).thenReturn(List.of(user));
        int expectedResult = 1;
        // when
        List<User> users = userService.getUsers();
        // then
        assertThat(users).hasSize(expectedResult);
    }

    @Test
    void addUser_returnsNewUser_givenUserDTO() {
        // given
        CreateUserFormDTO createUserFormDTO = new CreateUserFormDTO();
        createUserFormDTO.setName("Maciej");
        createUserFormDTO.setLastname("Ch");
        createUserFormDTO.setEmail("test@testing.com");
        createUserFormDTO.setPassword("12345678");

        User newUser = modelMapper.map(createUserFormDTO, User.class);
        newUser.setPassword(encoder.encode(newUser.getPassword()));
        newUser.setRole(Set.of(UserEnum.ROLE_USER));
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setEnabled(true);
        // when
        when(userRepository.save(Mockito.any(User.class))).thenReturn(newUser);
        Boolean addUser = userService.addUser(createUserFormDTO);
        // then
        assertThat(addUser).isTrue();
        assertThat(newUser.getName()).isEqualTo(createUserFormDTO.getName());
        assertThat(newUser.getLastname()).isEqualTo(createUserFormDTO.getLastname());
        assertThat(newUser.getEmail()).isEqualTo(createUserFormDTO.getEmail());
        boolean matches = encoder.matches(createUserFormDTO.getPassword(), newUser.getPassword());
        assertThat(matches).isTrue();
    }

    @Test
    void updateUser_changeUser_ChangesBillingCurrency() {
        // given
        loginUser();
        user.setBillingCurrency("100.55");
        // when
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        User updateUser = userService.updateUser(user);
        // then
        assertThat(updateUser.getBillingCurrency()).isNotNull();
        assertThat(updateUser.getBillingCurrency()).isEqualTo("100.55");
    }

    @Test
    void getMyCurrencies_returnsUserCurrency_givenGetMyCurrencies() {
        // given
        loginUser();
        given(userRepository.findById(id)).willReturn(user);
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO(4.5524, 4.4622, "USD", "Dolar", "currency");
        Currency currency = new Currency(UUID.randomUUID(), "USD", "dolar", "currency", List.of());
        when(currencyRepository.findByCode(Mockito.anyString())).thenReturn(currency);
        when(currentRatesService.getCurrencyOfLastExchangeRates(Mockito.anyString())).thenReturn(exchangeRateDTO);
        when(userService.updateUser(user)).thenReturn(user);
        userService.paymentToWallet("100.55");
        userService.askCurrency("USD", "4");
        // when
        Set<UserCurrencyDTO> myCurrencies = userService.getMyCurrencies();
        UserCurrencyDTO usd = myCurrencies.stream().filter(e -> e.getCurrency().getCode().equals("USD")).findFirst().orElse(new UserCurrencyDTO());
        // then
        assertThat(myCurrencies).hasSize(1);
        assertThat(usd).isNotNull();
        assertThat(usd.getAmount()).isEqualTo(4);
    }

    @Test
    void bidCurrency_returnsChangesBillingCurrency_givenAskAndBidCurrency() {
        // given
        loginUser();
        given(userRepository.findById(id)).willReturn(user);
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO(4.5524, 4.4622, "USD", "Dolar", "currency");
        Currency currency = new Currency(UUID.randomUUID(), "USD", "dolar", "currency", List.of());
        when(currencyRepository.findByCode(Mockito.anyString())).thenReturn(currency);
        when(currentRatesService.getCurrencyOfLastExchangeRates(Mockito.anyString())).thenReturn(exchangeRateDTO);
        when(userService.updateUser(user)).thenReturn(user);
        boolean paymentToWallet = userService.paymentToWallet("100.55");
        boolean askCurrency = userService.askCurrency("USD", "4");
        // when
        boolean bidCurrency = userService.bidCurrency("USD", "4");
        // then
        assertThat(paymentToWallet).isTrue();
        assertThat(askCurrency).isTrue();
        assertThat(bidCurrency).isTrue();
        assertThat(user.getBillingCurrency()).isEqualTo("100.18");
    }

    @Test
    void askCurrency_returnsChangesBillingCurrency_givenAskCurrency() {
        // given
        loginUser();
        given(userRepository.findById(id)).willReturn(user);
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO(4.5524, 4.4622, "USD", "Dolar", "currency");
        Currency currency = new Currency(UUID.randomUUID(), "USD", "dolar", "currency", List.of());
        when(currencyRepository.findByCode(Mockito.anyString())).thenReturn(currency);
        when(currentRatesService.getCurrencyOfLastExchangeRates(Mockito.anyString())).thenReturn(exchangeRateDTO);
        when(userService.updateUser(user)).thenReturn(user);
        // when
        boolean paymentToWallet = userService.paymentToWallet("100.55");
        boolean askCurrency = userService.askCurrency("USD", "4");
        // then
        assertThat(user.getMyCurrencies()).isNotNull();
        assertThat(paymentToWallet).isTrue();
        assertThat(askCurrency).isTrue();
        assertThat(user.getBillingCurrency()).isEqualTo("82.34");
    }

    @Test
    void paymentToWallet_changesUserBillingCurrency_givenPaymentValue() {
        // given
        loginUser();
        String expectedResult = "100.55";
        given(userRepository.findById(id)).willReturn(user);
        // when
        userService.paymentToWallet("100.55");
        // then
        assertThat(user.getBillingCurrency()).isEqualTo(expectedResult);
    }

    @Test
    void paymentToWallet_addedUserHistoryOperation_givenPaymentValue() {
        // given
        loginUser();
        given(userRepository.findById(id)).willReturn(user);
        int expectedResult = 1;
        // when
        userService.paymentToWallet("150");
        // then
        assertThat(user.getHistoryList()).hasSize(expectedResult);
    }

    @Test
    void withdrawalFromWallet_returnsUserBillingCurrency_givenWithdrawalValue() {
        // given
        loginUser();
        given(userRepository.findById(id)).willReturn(user);
        String expectedResult = "0.22";
        // when
        userService.paymentToWallet("100.33");
        userService.withdrawalFromWallet("100.11");
        // then
        assertThat(user.getBillingCurrency()).isEqualTo(expectedResult);
        assertThat(user.getHistoryList()).hasSize(2);
    }

    @Test
    void emailExists_returnsTrue_givenUserEmail() {
        // given
        given(userRepository.findByEmail("test@testing.com")).willReturn(user);
        // when
        boolean b = userService.emailExists("test@testing.com");
        // then
        assertThat(b).isTrue();
    }

    @Test
    void getHistoryOperation_returnsAllHistoryOperation_givenHistoryOperation() {
        // given
        loginUser();
        given(userRepository.findById(id)).willReturn(user);
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO(4.5524, 4.4622, "USD", "Dolar", "currency");
        Currency currency = new Currency(UUID.randomUUID(), "USD", "dolar", "currency", List.of());
        when(currencyRepository.findByCode(Mockito.anyString())).thenReturn(currency);
        when(currentRatesService.getCurrencyOfLastExchangeRates(Mockito.anyString())).thenReturn(exchangeRateDTO);
        when(userService.updateUser(user)).thenReturn(user);
        userService.paymentToWallet("100.55");
        userService.askCurrency("USD", "4");
        // when
        List<CurrencyOperationHistory> historyOperation = userService.getHistoryOperation();
        // then
        assertThat(historyOperation).isNotNull().hasSize(2);
    }

    @Test
    void getHistoryOperation_returnsCurrencyHistoryOperation_givenCurrencyCode() {
        // given
        loginUser();
        given(userRepository.findById(id)).willReturn(user);
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO(4.5524, 4.4622, "USD", "Dolar", "currency");
        Currency currency = new Currency(UUID.randomUUID(), "USD", "dolar", "currency", List.of());
        when(currencyRepository.findByCode(Mockito.anyString())).thenReturn(currency);
        when(currentRatesService.getCurrencyOfLastExchangeRates(Mockito.anyString())).thenReturn(exchangeRateDTO);
        when(userService.updateUser(user)).thenReturn(user);
        userService.paymentToWallet("100.55");
        userService.askCurrency("USD", "4");
        // when
        List<CurrencyOperationHistory> usd = userService.getHistoryOperation("USD");
        // then
        assertThat(usd).isNotNull().hasSize(1);
        assertThat(usd.get(0).getOperation()).isEqualTo(OperationEnum.ASK);
        assertThat(usd.get(0).getExchangeRate()).isEqualTo(4.5524);
    }

    @Test
    void savedFiltrationSettings() {
        // given
        loginUser();
        given(userRepository.findById(id)).willReturn(user);
        SaveOfFiltrationSettingsDTO saveOfFiltrationSettingsDTO = new SaveOfFiltrationSettingsDTO();
        saveOfFiltrationSettingsDTO.setPreferenceName("Test");
        saveOfFiltrationSettingsDTO.setAskPriceMin(4.00);
        saveOfFiltrationSettingsDTO.setAskPriceMax(5.00);
        saveOfFiltrationSettingsDTO.setCurrency(List.of("USD", "EUR"));
        // when
        when(userService.updateUser(user)).thenReturn(user);
        boolean savedFiltrationSettings = userService.savedFiltrationSettings(saveOfFiltrationSettingsDTO);
        // then
        assertThat(savedFiltrationSettings).isTrue();
        assertThat(user.getSavedFiltrationSettings()).hasSize(1);
    }

    @Test
    void getListOfSavedFiltrationSettings() {
        // given
        savedFiltrationSettings();
        // when
        List<Map.Entry<String, FiltrationSettingsDTO>> listOfSavedFiltrationSettings = userService.getListOfSavedFiltrationSettings();
        // then
        assertThat(listOfSavedFiltrationSettings).isNotNull().hasSize(1);
    }

    @Test
    void getSavedFiltrationSettings() {
        // given
        savedFiltrationSettings();
        // when
        Map<String, FiltrationSettingsDTO> savedFiltrationSettings = userService.getSavedFiltrationSettings();
        // then
        assertThat(savedFiltrationSettings).isNotNull().hasSize(1);
        assertThat(savedFiltrationSettings.get("Test").getAskPriceMax()).isEqualTo(5.00);
        assertThat(savedFiltrationSettings.get("Test").getAskPriceMin()).isEqualTo(4.00);
        assertThat(savedFiltrationSettings.get("Test").getCurrency()).isEqualTo(List.of("USD", "EUR"));

    }
    void loginUser() {
        MyUserPrincipal myUserPrincipal = new MyUserPrincipal(user);
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(myUserPrincipal);
    }
}



