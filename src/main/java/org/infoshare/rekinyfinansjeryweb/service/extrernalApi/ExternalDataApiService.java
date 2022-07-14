package org.infoshare.rekinyfinansjeryweb.service.extrernalApi;

import lombok.extern.slf4j.Slf4j;
import org.infoshare.rekinyfinansjeryweb.entity.Currency;
import org.infoshare.rekinyfinansjeryweb.entity.LastUpdate;
import org.infoshare.rekinyfinansjeryweb.repository.CurrencyRepository;
import org.infoshare.rekinyfinansjeryweb.repository.ExchangeRateRepository;
import org.infoshare.rekinyfinansjeryweb.repository.LastUpdateRepository;
import org.infoshare.rekinyfinansjeryweb.service.extrernalApi.nbpAdapter.NBPApiAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ExternalDataApiService {
    @Autowired
    CurrencyRepository currencyRepository;
    @Autowired
    ExchangeRateRepository exchangeRateRepository;
    @Autowired
    LastUpdateRepository lastUpdateRepository;
    @Autowired
    ExecutorService executorService;
    @Autowired
    private List<ExternalApiDataSourceInterface> externalApiDataSources;

    @Transactional
    public void getData(){
        List<Currency> currencies = currencyRepository.findAll();
        externalApiDataSources.forEach(dataSource -> executorService.submit(() -> synchronizeWithDataSource(dataSource, currencies)));
    }

    public void synchronizeWithDataSource(ExternalApiDataSourceInterface dataSource, List<Currency> currencies){
        log.info("Started updating "+dataSource.getApiName());
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
        log.info("Finished updating "+dataSource.getApiName());
    }
}
