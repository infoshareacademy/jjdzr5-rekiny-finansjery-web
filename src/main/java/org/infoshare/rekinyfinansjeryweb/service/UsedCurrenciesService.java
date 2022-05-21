package org.infoshare.rekinyfinansjeryweb.service;

import com.infoshareacademy.services.NBPApiManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.infoshare.rekinyfinansjeryweb.controller.FiltrationController;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsedCurrenciesService {
    public List<PossibleCurrency> getShortNamesOfCurrencies(NBPApiManager nbpApiManager, List<String> selectedCurrencies){
        Set<PossibleCurrency> currencySet = new HashSet<>();
        nbpApiManager
            .getCollectionsOfExchangeRates()
            .forEach(dailyExchangeRates -> {
                currencySet.addAll(
                    dailyExchangeRates
                        .getRates()
                        .stream()
                        .map(rate -> new PossibleCurrency(rate.getCode(), selectedCurrencies.contains(rate.getCode())))
                        .collect(Collectors.toSet()));
            });

        return currencySet.stream().toList();
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
