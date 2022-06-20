package org.infoshare.rekinyfinansjeryweb.service;

import com.infoshareacademy.domain.DailyExchangeRates;
import com.infoshareacademy.domain.ExchangeRate;
import org.infoshare.rekinyfinansjeryweb.dto.ExchangeRateFormDTO;
import org.infoshare.rekinyfinansjeryweb.dto.DailyTableFormDTO;
import org.infoshare.rekinyfinansjeryweb.repository.CurrencyRepository;
import org.infoshare.rekinyfinansjeryweb.repository.ExchangeRateRepository;
import org.infoshare.rekinyfinansjeryweb.entity.Currency;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    ExchangeRatesTableRepository exchangeRatesTableRepository;

    @Autowired
    ExchangeRateRepository exchangeRateRepository;

    @Autowired
    CurrencyRepository currencyRepository;

    public void addTable(DailyTableForm dailyTableForm) {
        ExchangeRatesTable exchangeRatesTable = new ExchangeRatesTable();
        exchangeRatesTable.setNo(dailyTableForm.getNo());
        exchangeRatesTable.setEffectiveDate(dailyTableForm.getEffectiveDate());
        exchangeRatesTable.setTradingDate(dailyTableForm.getTradingDate());

        exchangeRatesTableRepository.save(exchangeRatesTable);
    }

    public void deleteTable(String tableNo) {
        ExchangeRatesTable exchangeRatesTable = exchangeRatesTableRepository.findByNo(tableNo);
        exchangeRatesTableRepository.delete(exchangeRatesTable);
    }

    public void editTable(String tableNo, DailyTableForm dailyTableForm) {
        Optional<ExchangeRatesTable> exchangeRatesTable = Optional.of(exchangeRatesTableRepository.findByNo(tableNo));
        exchangeRatesTable.ifPresentOrElse(table -> {
            table.setNo(dailyTableForm.getNo());
            table.setEffectiveDate(dailyTableForm.getEffectiveDate());
            table.setTradingDate(dailyTableForm.getTradingDate());

            exchangeRatesTableRepository.save(table);
        }, () -> {
            //TODO Add logger
        });
    }

    public void addExchangeRate(String tableNo, ExchangeRateForm exchangeRateForm) {
        ExchangeRate exchangeRate = new ExchangeRate();
        Currency currency = saveCurrency(new Currency(), exchangeRateForm);
        exchangeRate.setDailyTable(exchangeRatesTableRepository.findByNo(tableNo));
        saveExchangeRate(exchangeRate, currency, exchangeRateForm);
    }

    public void deleteExchangeRate(String tableNo, String code) {
        exchangeRateRepository.delete(getExchangeRate(tableNo, code));
    }

    public void editExchangeRate(String tableNo, String code, ExchangeRateForm exchangeRateForm) {
        ExchangeRate exchangeRate = getExchangeRate(tableNo, code);
        Currency editedCurrency = saveCurrency(currencyRepository.findByCode(code), exchangeRateForm);
        saveExchangeRate(exchangeRate, editedCurrency, exchangeRateForm);
    }

    public boolean tableExists(String tableNo) {
        return exchangeRatesTableRepository.findByNo(tableNo) != null;
    }

    public boolean exchangeRateExists(String tableNo, String code) {
        return getExchangeRate(tableNo, code) != null;
    }

    private Currency saveCurrency(Currency currency, ExchangeRateForm exchangeRateForm) {
        currency.setCode(exchangeRateForm.getCode());
        currency.setName(exchangeRateForm.getCurrency());
        currency.setCategory("currency");
        currencyRepository.save(currency);
        return currency;
    }

    private void saveExchangeRate(ExchangeRate exchangeRate, Currency currency, ExchangeRateForm exchangeRateForm) {
        exchangeRate.setBidPrice(exchangeRateForm.getBid());
        exchangeRate.setAskPrice(exchangeRateForm.getAsk());
        exchangeRate.setCurrency(currency);
        exchangeRateRepository.save(exchangeRate);
    }

    private ExchangeRate getExchangeRate(String tableNo, String code) {
        ExchangeRatesTable exchangeRatesTable = exchangeRatesTableRepository.findByNo(tableNo);
        Currency currency = currencyRepository.findByCode(code);
        return exchangeRateRepository.findByCurrencyAndTable(currency, exchangeRatesTable);
    }
}