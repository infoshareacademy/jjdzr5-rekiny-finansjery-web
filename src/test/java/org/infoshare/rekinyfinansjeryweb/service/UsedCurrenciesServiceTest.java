package org.infoshare.rekinyfinansjeryweb.service;

import org.infoshare.rekinyfinansjeryweb.dto.CurrencyTypeBucket;
import org.infoshare.rekinyfinansjeryweb.dto.PossibleCurrency;
import org.infoshare.rekinyfinansjeryweb.entity.Currency;
import org.infoshare.rekinyfinansjeryweb.repository.CurrencyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UsedCurrenciesServiceTest {

    @Mock
    CurrencyRepository currencyRepository;

    @Test
    void getShortNamesOfCurrencies_returnListOfPossibleCurrencies_whenMethodCalledWithSelectedCodes() {
        //given
        List<Currency> currencies = List.of(new Currency(UUID.randomUUID(), "USD", "dolar", "currency", List.of(), ""),
                new Currency(UUID.randomUUID(), "EUR", "euro", "currency", List.of(), ""));
        given(currencyRepository.findAll()).willReturn(currencies);
        UsedCurrenciesService usedCurrenciesService = new UsedCurrenciesService(currencyRepository);
        List<String> selectedCurrencies = List.of("USD");
        //when
        List<PossibleCurrency> possibleCurrencies = usedCurrenciesService.getShortNamesOfCurrencies(selectedCurrencies);
        //then
        verify(currencyRepository).findAll();
        assertThat(possibleCurrencies.size()).isEqualTo(2);
        assertThat(possibleCurrencies.get(0).getCode()).isEqualTo("USD");
        assertThat(possibleCurrencies.get(0).getCategory()).isEqualTo("currency");
        assertThat(possibleCurrencies.get(0).isChecked()).isEqualTo(true);
        assertThat(possibleCurrencies.get(1).getCode()).isEqualTo("EUR");
        assertThat(possibleCurrencies.get(1).getCategory()).isEqualTo("currency");
        assertThat(possibleCurrencies.get(1).isChecked()).isEqualTo(false);
    }

    @Test
    void getShortNamesOfCurrenciesSplitByCategory_returnListOfPossibleCurrenciesSplitByCategory_whenMethodCalledWithSelectedCodes() {
        //given
        List<Currency> currencies = List.of(new Currency(UUID.randomUUID(), "USD", "dolar", "currency", List.of(), ""),
                new Currency(UUID.randomUUID(), "EUR", "euro", "currency", List.of(), ""),
                new Currency(UUID.randomUUID(), "BTC", "bitcoin", "cryptocurrency", List.of(), ""));
        given(currencyRepository.findAll()).willReturn(currencies);
        UsedCurrenciesService usedCurrenciesService = new UsedCurrenciesService(currencyRepository);
        List<String> selectedCurrencies = List.of("USD");
        //when
        List<CurrencyTypeBucket> categoryBuckets = usedCurrenciesService.getShortNamesOfCurrenciesSplitByCategory(selectedCurrencies);
        //then
        verify(currencyRepository).findAll();
        assertThat(categoryBuckets.size()).isEqualTo(2);
        for(CurrencyTypeBucket currencyTypeBucket : categoryBuckets) {
            if (currencyTypeBucket.getCategory().equals("currency")) {
                assertThat(currencyTypeBucket.getPossibleCurrencies().size()).isEqualTo(2);
                assertThat(currencyTypeBucket.getPossibleCurrencies().get(0).getCode()).isEqualTo("USD");
                assertThat(currencyTypeBucket.getPossibleCurrencies().get(0).getCategory()).isEqualTo("currency");
                assertThat(currencyTypeBucket.getPossibleCurrencies().get(0).isChecked()).isEqualTo(true);
                assertThat(currencyTypeBucket.getPossibleCurrencies().get(1).getCode()).isEqualTo("EUR");
                assertThat(currencyTypeBucket.getPossibleCurrencies().get(1).getCategory()).isEqualTo("currency");
                assertThat(currencyTypeBucket.getPossibleCurrencies().get(1).isChecked()).isEqualTo(false);
            } else if (currencyTypeBucket.getCategory().equals("cryptocurrency")) {
                assertThat(currencyTypeBucket.getPossibleCurrencies().size()).isEqualTo(1);
                assertThat(currencyTypeBucket.getPossibleCurrencies().get(0).getCode()).isEqualTo("BTC");
                assertThat(currencyTypeBucket.getPossibleCurrencies().get(0).getCategory()).isEqualTo("cryptocurrency");
                assertThat(currencyTypeBucket.getPossibleCurrencies().get(0).isChecked()).isEqualTo(false);
            }
        }
    }

    @Test
    void getShortNamesOfCurrenciesForStats_returnListOfCodes_whenMethodCalled() {
        //given
        List<Currency> currencies = List.of(new Currency(UUID.randomUUID(), "USD", "dolar", "currency", List.of(), ""),
                new Currency(UUID.randomUUID(), "EUR", "euro", "currency", List.of(), ""),
                new Currency(UUID.randomUUID(), "BTC", "bitcoin", "cryptocurrency", List.of(), ""));
        given(currencyRepository.findAll()).willReturn(currencies);
        UsedCurrenciesService usedCurrenciesService = new UsedCurrenciesService(currencyRepository);
        //when
        List<String> codes = usedCurrenciesService.getShortNamesOfCurrenciesForStats();
        //then
        verify(currencyRepository).findAll();
        assertThat(codes.size()).isEqualTo(3);
        assertThat(codes.get(0)).isEqualTo("USD");
        assertThat(codes.get(1)).isEqualTo("EUR");
        assertThat(codes.get(2)).isEqualTo("BTC");
    }
}