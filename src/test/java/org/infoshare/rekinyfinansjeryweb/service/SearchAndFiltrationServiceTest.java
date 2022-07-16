package org.infoshare.rekinyfinansjeryweb.service;

import org.infoshare.rekinyfinansjeryweb.dto.DailyTableDTO;
import org.infoshare.rekinyfinansjeryweb.dto.FiltrationSettingsDTO;
import org.infoshare.rekinyfinansjeryweb.dto.PageDTO;
import org.infoshare.rekinyfinansjeryweb.dto.SearchSettingsDTO;
import org.infoshare.rekinyfinansjeryweb.entity.Currency;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRateCurrency;
import org.infoshare.rekinyfinansjeryweb.repository.CurrencyRepository;
import org.infoshare.rekinyfinansjeryweb.repository.ExchangeRateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SearchAndFiltrationServiceTest {

    @Mock
    ExchangeRateRepository exchangeRateRepository;
    @Mock
    CurrencyRepository currencyRepository;
    @Mock
    CurrencyStatisticsClientService currencyStatisticsClientService;
    @Mock
    ExecutorService executorService;

    @Captor
    ArgumentCaptor<List<LocalDate>> datesCaptor;
    @Captor
    ArgumentCaptor<List<Currency>> currencyCaptor;

    @Test
    void getFilteredCollection_returnsRequestedPage_givenFiltrationSettingsAndPageAble(){
        //given
        SearchAndFiltrationService searchAndFiltrationService =
                new SearchAndFiltrationService(exchangeRateRepository, currencyRepository, currencyStatisticsClientService, executorService);
        FiltrationSettingsDTO filtrationSettingsDTO = new FiltrationSettingsDTO();
        Pageable pageable = PageRequest.of(0, 2);
        List<Currency> currencies = List.of(new Currency(UUID.randomUUID(), "USD", "dolar", "currency", new ArrayList<>()),
                new Currency(UUID.randomUUID(), "EUR", "euro", "currency", new ArrayList<>()));

        List<LocalDate> dates = List.of(LocalDate.of(2022, 05, 1),
                LocalDate.of(2022, 05, 2));
        List<ExchangeRateCurrency> rates = List.of(
                new ExchangeRateCurrency(UUID.randomUUID(), LocalDate.of(2022, 5, 1), 5d, 4.9d, "USD", "dolar", "currency"),
                new ExchangeRateCurrency(UUID.randomUUID(), LocalDate.of(2022, 5, 2), 5.1d, 5d, "USD", "dolar", "currency"),
                new ExchangeRateCurrency(UUID.randomUUID(), LocalDate.of(2022, 5, 1), 4.9d, 4.7d, "EUR", "euro", "currency"),
                new ExchangeRateCurrency(UUID.randomUUID(), LocalDate.of(2022, 5, 2), 4d, 3.9d, "EUR", "euro", "currency"));
        given(currencyRepository.findAllCurrencyByCodeIn(eq(filtrationSettingsDTO))).willReturn(currencies);
        given(exchangeRateRepository.countDatesByFilterSettings(eq(filtrationSettingsDTO), currencyCaptor.capture())).willReturn(5L);
        given(exchangeRateRepository.findDatesFromPageByFilterSettings(eq(filtrationSettingsDTO), eq(pageable), currencyCaptor.capture()))
                .willReturn(dates);
        given(exchangeRateRepository.findSelectedDates(eq(filtrationSettingsDTO), datesCaptor.capture(), currencyCaptor.capture()))
                .willReturn(rates);

        //when
        PageDTO pageDTO = searchAndFiltrationService.getFilteredCollection(filtrationSettingsDTO, pageable, Optional.of(0L));
        List<DailyTableDTO> page = pageDTO.getTables();

        //then
        assertThat(datesCaptor.getValue()).isEqualTo(dates);
        verify(currencyRepository).findAllCurrencyByCodeIn(filtrationSettingsDTO);
        verify(exchangeRateRepository).countDatesByFilterSettings(filtrationSettingsDTO, currencies);
        verify(exchangeRateRepository).findDatesFromPageByFilterSettings(filtrationSettingsDTO, pageable, currencies);
        verify(exchangeRateRepository).findSelectedDates(eq(filtrationSettingsDTO), eq(dates), eq(currencies));
        assertThat(pageDTO.getNumberOfPages()).isEqualTo(3);
        assertThat(pageDTO.getTotalDailyTables()).isEqualTo(5);
        assertThat(page.size()).isEqualTo(2);
        page.forEach(table -> assertThat(table.getRates().size()).isEqualTo(2));
    }

    /*@Test
    void searchInCollection_returnsRequestedPage_givenSearchSettingsAndPageAble(){
        //given
        SearchAndFiltrationService searchAndFiltrationService = new SearchAndFiltrationService(exchangeRateRepository);
        SearchSettingsDTO searchSettingsDTO = new SearchSettingsDTO();
        searchSettingsDTO.setSearchPhrase("USD");
        Pageable pageable = PageRequest.of(0, 2);
        List<LocalDate> dates = List.of(LocalDate.of(2022, 05, 1),
                LocalDate.of(2022, 05, 2));
        List<ExchangeRateCurrency> rates = List.of(
                new ExchangeRateCurrency(UUID.randomUUID(), LocalDate.of(2022, 5, 1), 5d, 4.9d, "USD", "dolar", "currency"),
                new ExchangeRateCurrency(UUID.randomUUID(), LocalDate.of(2022, 5, 2), 5.1d, 5d, "USD", "dolar", "currency"));

        given(exchangeRateRepository.countDatesBySearchSettings(searchSettingsDTO)).willReturn(5L);
        given(exchangeRateRepository.findDatesFromPageBySearchSettings(searchSettingsDTO,pageable))
                .willReturn(dates);
        given(exchangeRateRepository.findSelectedDates(eq(searchSettingsDTO), datesCaptor.capture()))
                .willReturn(rates);

        //when
        PageDTO pageDTO = searchAndFiltrationService.searchInCollection(searchSettingsDTO, pageable);
        List<DailyTableDTO> page = pageDTO.getTables();

        //then
        assertThat(datesCaptor.getValue()).isEqualTo(dates);
        verify(exchangeRateRepository).countDatesBySearchSettings(searchSettingsDTO);
        verify(exchangeRateRepository).findDatesFromPageBySearchSettings(searchSettingsDTO, pageable);
        verify(exchangeRateRepository).findSelectedDates(eq(searchSettingsDTO), eq(dates));
        assertThat(pageDTO.getNumberOfPages()).isEqualTo(3);
        assertThat(pageDTO.getTotalDailyTables()).isEqualTo(5);
        assertThat(page.size()).isEqualTo(2);
        page.forEach(table -> assertThat(table.getRates().size()).isEqualTo(1));
    }

    @Test
    void searchInCollection_returnsRequestedPage_givenSearchSettingsWithEmptySearchPhraseAndPageAble(){
        //given
        SearchAndFiltrationService searchAndFiltrationService = new SearchAndFiltrationService(exchangeRateRepository);
        SearchSettingsDTO searchSettingsDTO = new SearchSettingsDTO();
        searchSettingsDTO.setSearchPhrase("");
        Pageable pageable = PageRequest.of(0, 2);

        //when
        PageDTO pageDTO = searchAndFiltrationService.searchInCollection(searchSettingsDTO, pageable);

        //then
        assertThat(pageDTO.getNumberOfPages()).isEqualTo(0);
        assertThat(pageDTO.getTotalDailyTables()).isEqualTo(0);
        assertThat(pageDTO.getTables()).isEmpty();
    }

    @Test
    void searchInCollection_returnsRequestedPage_givenSearchSettingsWithNullSearchPhraseAndPageAble(){
        //given
        SearchAndFiltrationService searchAndFiltrationService = new SearchAndFiltrationService(exchangeRateRepository);
        SearchSettingsDTO searchSettingsDTO = new SearchSettingsDTO();
        Pageable pageable = PageRequest.of(0, 2);

        //when
        PageDTO pageDTO = searchAndFiltrationService.searchInCollection(searchSettingsDTO, pageable);

        //then
        assertThat(pageDTO.getNumberOfPages()).isEqualTo(0);
        assertThat(pageDTO.getTotalDailyTables()).isEqualTo(0);
        assertThat(pageDTO.getTables()).isEmpty();
    }*/



}