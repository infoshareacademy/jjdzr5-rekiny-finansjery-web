package org.infoshare.rekinyfinansjeryweb.service;

import org.infoshare.rekinyfinansjeryweb.entity.Currency;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRate;
import org.infoshare.rekinyfinansjeryweb.repository.ExchangeRateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChartServiceTest {

    @Mock
    ExchangeRateRepository exchangeRateRepository;

    @Test
    void createChartDataSet() {

        // given
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        String code = "USD";
        String category = "currency";
        double askPrice = 7.3635;
        double bidPrice = 6.987;

        Currency currencyUSD = new Currency(UUID.randomUUID(), code, code, category, List.of());

        List<ExchangeRate> rates = new ArrayList<>();
        ExchangeRate rate = new ExchangeRate(UUID.randomUUID(), currencyUSD, LocalDate.now(), askPrice, bidPrice);
        rates.add(rate);

        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDate lastDayOfMonth = LocalDate.of(year, month, LocalDate.of(year, month, 1).lengthOfMonth());

        when(exchangeRateRepository.findExchangeRatesByCurrency_CodeAndDateBetweenOrderByDateDesc(code, firstDayOfMonth, lastDayOfMonth)).thenReturn(rates);
        // when
        List<ExchangeRate> exchangeRates = exchangeRateRepository.findExchangeRatesByCurrency_CodeAndDateBetweenOrderByDateDesc(code, firstDayOfMonth, lastDayOfMonth);
        // then
        assertThat(exchangeRates).isNotEmpty();
        assertThat(exchangeRates.get(0).getCurrency()).isInstanceOf(Currency.class);
        assertThat(exchangeRates.get(0).getAskPrice()).isEqualTo(askPrice);
        assertThat(exchangeRates.get(0).getBidPrice()).isEqualTo(bidPrice);
        assertThat(exchangeRates.get(0).getDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void getChartData() {
    }

    @Test
    void testGetChartData() {
    }

    @Test
    void getChartsData() {
    }

    @Test
    void sendExchangeRatesForGivenPage() {
    }
}