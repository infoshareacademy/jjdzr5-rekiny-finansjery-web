package org.infoshare.rekinyfinansjeryweb.service.extrernalApi;

import org.infoshare.rekinyfinansjeryweb.entity.Currency;
import org.infoshare.rekinyfinansjeryweb.entity.LastUpdate;
import org.infoshare.rekinyfinansjeryweb.repository.CurrencyRepository;
import org.infoshare.rekinyfinansjeryweb.repository.ExchangeRateRepository;
import org.infoshare.rekinyfinansjeryweb.repository.LastUpdateRepository;
import org.infoshare.rekinyfinansjeryweb.service.extrernalApi.nbpAdapter.NBPApiAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class ExternalDataApiService {
    @Autowired
    CurrencyRepository currencyRepository;
    @Autowired
    ExchangeRateRepository exchangeRateRepository;
    @Autowired
    LastUpdateRepository lastUpdateRepository;

    @Autowired
    ExternalApiDataSourceInterface nbpApi;

    @Transactional
    public void getData(){
        List<Currency> currencies = currencyRepository.findAll();
        synchronizeWithDataSource(nbpApi, currencies);
    }

    public void synchronizeWithDataSource(ExternalApiDataSourceInterface dataSource, List<Currency> currencies){
        Optional<LastUpdate> lastUpdate = lastUpdateRepository.findBySourceName(dataSource.getApiName());
        ApiRequestResult result = dataSource.getResultData(currencies, lastUpdate);
        currencyRepository.saveAll(result.getCurrencies());
        exchangeRateRepository.saveAll(result.getExchangeRates());
        if(lastUpdate.isPresent()){
            LastUpdate thisUpdate = lastUpdate.get();
            thisUpdate.setUpdateTime(LocalDateTime.now());
            lastUpdateRepository.save(thisUpdate);
        }
        else {
            LastUpdate thisUpdate = new LastUpdate(null, dataSource.getApiName(), LocalDateTime.now());
            lastUpdateRepository.save(thisUpdate);
        }
    }
}
