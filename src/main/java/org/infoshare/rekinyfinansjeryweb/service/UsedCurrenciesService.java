package org.infoshare.rekinyfinansjeryweb.service;

import com.infoshareacademy.services.NBPApiManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.infoshare.rekinyfinansjeryweb.dto.CurrencyTypeBucket;
import org.infoshare.rekinyfinansjeryweb.dto.PossibleCurrency;
import org.infoshare.rekinyfinansjeryweb.entity.Currency;
import org.infoshare.rekinyfinansjeryweb.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsedCurrenciesService {

    @Autowired
    private CurrencyRepository currencyRepository;
    public List<PossibleCurrency> getShortNamesOfCurrencies(NBPApiManager nbpApiManager, List<String> selectedCurrencies){
        List<Currency> currencies = currencyRepository.findAll();
        return currencies.stream().map(rate -> new PossibleCurrency(rate.getCode(), rate.getCategory(), selectedCurrencies.contains(rate.getCode())))
                .collect(Collectors.toList());
    }

    public List<CurrencyTypeBucket> getShortNamesOfCurrenciesSplitByCategory(NBPApiManager nbpApiManager, List<String> selectedCurrencies){
        List<Currency> currencies = currencyRepository.findAll();
        Map<String, CurrencyTypeBucket> categoryBuckets = new HashMap<>();
        currencies.stream().map(rate -> new PossibleCurrency(rate.getCode(), rate.getCategory(), selectedCurrencies.contains(rate.getCode())))
                .forEach(currency -> {
                    categoryBuckets.putIfAbsent(currency.getCategory(), new CurrencyTypeBucket(currency.getCategory(), new ArrayList<>()));
                    categoryBuckets.get(currency.getCategory()).getPossibleCurrencies().add(currency);
                });
        return categoryBuckets.values().stream().toList();
    }

    public List<String> getShortNamesOfCurrenciesForStats(){
        List<Currency> currencies = currencyRepository.findAll();
        return currencies.stream().map(Currency::getCode)
                .collect(Collectors.toList());
    }
}
