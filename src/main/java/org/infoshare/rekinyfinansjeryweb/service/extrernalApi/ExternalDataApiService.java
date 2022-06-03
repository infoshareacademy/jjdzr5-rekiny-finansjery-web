package org.infoshare.rekinyfinansjeryweb.service;

import org.infoshare.rekinyfinansjeryweb.repository.*;
import org.infoshare.rekinyfinansjeryweb.service.extrernalApi.ApiRequestResult;
import org.infoshare.rekinyfinansjeryweb.service.extrernalApi.nbpAdapter.NBPApiAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
        NBPApiAdapter nbpApiAdapter = new NBPApiAdapter();
        ApiRequestResult result = nbpApiAdapter.getResultData();
        currencyRepository.saveAll(result.getCurrencies());
        exchangeRatesTableRepository.saveAll(result.getExchangeRatesTables());
        exchangeRateRepository.saveAll(result.getExchangeRates());

    }
}
