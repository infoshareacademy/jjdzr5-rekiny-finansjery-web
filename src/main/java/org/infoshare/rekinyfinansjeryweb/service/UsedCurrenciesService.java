package org.infoshare.rekinyfinansjeryweb.service;

import com.infoshareacademy.services.NBPApiManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.infoshare.rekinyfinansjeryweb.controller.FiltrationController;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsedCurrenciesService {
    public List<PossibleCurrency> getShortNamesOfCurrencies(NBPApiManager nbpApiManager, List<String> selectedCurrencies){
        return nbpApiManager.getCollectionsOfExchangeRates()
                .stream()
                .findFirst()
                .map(exchangeRates -> exchangeRates
                        .getRates()
                        .stream()
                        .map(rate -> new PossibleCurrency(rate.getCode(), selectedCurrencies.contains(rate.getCode())))
                        .collect(Collectors.toList()))
                .orElseGet(ArrayList::new);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class PossibleCurrency{
        private String code;
        private boolean checked;
    }
}
