package org.infoshare.rekinyfinansjeryweb.service.extrernalApi;

import org.infoshare.rekinyfinansjeryweb.entity.Currency;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRatesTable;
import org.infoshare.rekinyfinansjeryweb.entity.LastUpdate;
import org.infoshare.rekinyfinansjeryweb.repository.*;
import org.infoshare.rekinyfinansjeryweb.service.extrernalApi.ApiRequestResult;
import org.infoshare.rekinyfinansjeryweb.service.extrernalApi.nbpAdapter.NBPApiAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ExternalDataApiService {
    @Autowired
    CurrencyRepository currencyRepository;
    @Autowired
    ExchangeRatesTableRepository exchangeRatesTableRepository;
    @Autowired
    ExchangeRateRepository exchangeRateRepository;
    @Autowired
    LastUpdateRepository lastUpdateRepository;

    @Transactional
    public void getData(){
        List<Currency> currencies = currencyRepository.findAll();
        NBPApiAdapter nbpApiAdapter = new NBPApiAdapter();
        Optional<LastUpdate> lastUpdate = lastUpdateRepository.findBySourceName(nbpApiAdapter.getApiName());
        ApiRequestResult result = nbpApiAdapter.getResultData(currencies, lastUpdate);
        currencyRepository.saveAll(result.getCurrencies());
        exchangeRatesTableRepository.saveAll(result.getExchangeRatesTables());
        exchangeRatesTableRepository.save(new ExchangeRatesTable(null, "sadsad", LocalDate.now().minusDays(1), LocalDate.now().minusDays(1), new ArrayList<>()));
        exchangeRateRepository.saveAll(result.getExchangeRates());
        if(lastUpdate.isPresent()){
            LastUpdate thisUpdate = lastUpdate.get();
            thisUpdate.setUpdateTime(LocalDateTime.now());
            lastUpdateRepository.save(thisUpdate);
        }
        else {
            LastUpdate thisUpdate = new LastUpdate(null, nbpApiAdapter.getApiName(), LocalDateTime.now());
            lastUpdateRepository.save(thisUpdate);
        }
    }
}
