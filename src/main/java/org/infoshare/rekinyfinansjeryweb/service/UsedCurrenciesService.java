package org.infoshare.rekinyfinansjeryweb.service;

import com.infoshareacademy.services.NBPApiManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
        return currencies.stream().map(rate -> new PossibleCurrency(rate.getCode(), selectedCurrencies.contains(rate.getCode())))
                .collect(Collectors.toList());
    }

    public List<String> getShortNamesOfCurrenciesForStats(){
        List<Currency> currencies = currencyRepository.findAll();
        return currencies.stream().map(Currency::getCode)
                .collect(Collectors.toList());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class PossibleCurrency{
        private String code;
        private boolean checked;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PossibleCurrency that = (PossibleCurrency) o;
            return checked == that.checked && Objects.equals(code, that.code);
        }

        @Override
        public int hashCode() {
            return Objects.hash(code, checked);
        }
    }
}
