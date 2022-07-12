package org.infoshare.rekinyfinansjeryweb.service;

import org.infoshare.rekinyfinansjeryweb.dto.ExchangeRateFormDTO;
import org.infoshare.rekinyfinansjeryweb.repository.CurrencyRepository;
import org.infoshare.rekinyfinansjeryweb.repository.ExchangeRateRepository;
import org.infoshare.rekinyfinansjeryweb.entity.Currency;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AdminTableService {

    @Autowired
    ExchangeRateRepository exchangeRateRepository;

    @Autowired
    CurrencyRepository currencyRepository;

    public void deleteTable(LocalDate date) {
        List<ExchangeRate> exchangeRates = exchangeRateRepository.findExchangeRatesByDate(date);
        exchangeRateRepository.deleteAll(exchangeRates);
        for (ExchangeRate exchangeRate : exchangeRates) {
            deleteCurrencyIfEmpty(exchangeRate.getCurrency().getCode());
        }
    }

    public void editTable(LocalDate date, LocalDate newDate) {
        List<ExchangeRate> exchangeRates = exchangeRateRepository.findExchangeRatesByDate(date);
        exchangeRates.forEach(rate -> rate.setDate(newDate));
        exchangeRateRepository.saveAll(exchangeRates);
    }

    public void addExchangeRate(ExchangeRateFormDTO exchangeRateForm) {
        Currency currency = currencyRepository.findByCode(exchangeRateForm.getCode());
        if (currency == null) {
            currency = saveCurrency(new Currency(), exchangeRateForm);
        }
        saveExchangeRate(new ExchangeRate(), currency, exchangeRateForm);
    }

    public void deleteExchangeRate(LocalDate date, String code) {
        exchangeRateRepository.delete(getExchangeRate(date, code));
        deleteCurrencyIfEmpty(code);
    }

    public void editExchangeRate(LocalDate date, String code, ExchangeRateFormDTO exchangeRateForm) {
        ExchangeRate exchangeRate = getExchangeRate(date, code);
        Currency editedCurrency = saveCurrency(currencyRepository.findByCode(code), exchangeRateForm);
        saveExchangeRate(exchangeRate, editedCurrency, exchangeRateForm);
    }

    public boolean tableExists(LocalDate date) {
        return !exchangeRateRepository.findExchangeRatesByDate(date).isEmpty();
    }

    public boolean exchangeRateExists(LocalDate date, String code) {
        return getExchangeRate(date, code) != null;
    }

    private Currency saveCurrency(Currency currency, ExchangeRateFormDTO exchangeRateForm) {
        currency.setCode(exchangeRateForm.getCode());
        currency.setName(exchangeRateForm.getCurrency());
        currency.setCategory(exchangeRateForm.getCategory());
        currencyRepository.save(currency);
        return currency;
    }

    private void saveExchangeRate(ExchangeRate exchangeRate, Currency currency, ExchangeRateFormDTO exchangeRateForm) {
        exchangeRate.setDate(exchangeRateForm.getDate());
        exchangeRate.setBidPrice(exchangeRateForm.getBid());
        exchangeRate.setAskPrice(exchangeRateForm.getAsk());
        exchangeRate.setCurrency(currency);
        exchangeRateRepository.save(exchangeRate);
    }

    private ExchangeRate getExchangeRate(LocalDate date, String code) {
        Currency currency = currencyRepository.findByCode(code);
        return exchangeRateRepository.findExchangeRateByCurrencyAndDate(currency, date);
    }

    private boolean deleteCurrencyIfEmpty(String code) {
        Currency currency = currencyRepository.findByCode(code);
        if (currency.getCurrencies().isEmpty()) {
            currencyRepository.delete(currency);
            return true;
        }
        return false;
    }
}