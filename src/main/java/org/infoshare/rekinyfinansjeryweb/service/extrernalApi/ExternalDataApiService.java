package org.infoshare.rekinyfinansjeryweb.service;

import org.infoshare.rekinyfinansjeryweb.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ExternalDataApiService {
    @Autowired
    CurrencyRepository currencyRepository;
    @Autowired
    ExchangeRatesTableRepository exchangeRatesTableRepository;
    @Autowired
    ExchangeRateRepository exchangeRateRepository;

    public void getData(){
        Currency currency = new Currency();
        currency.setCode("USD");
        currency.setName("dolar");
        currency.setCategory("waluta");

        ExchangeRatesTable exchangeRatesTable = new ExchangeRatesTable();
        exchangeRatesTable.setNo("000/12");
        exchangeRatesTable.setEffectiveDate(LocalDate.now());
        exchangeRatesTable.setTradingDate(LocalDate.now());

        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setAskPrice(2.0);
        exchangeRate.setBidPrice(2.2);
        exchangeRate.setCurrency(currency);
        exchangeRate.setTable(exchangeRatesTable);

        currencyRepository.save(currency);
        exchangeRatesTableRepository.save(exchangeRatesTable);
        exchangeRateRepository.save(exchangeRate);
    }
}
