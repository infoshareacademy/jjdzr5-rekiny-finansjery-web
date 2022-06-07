package org.infoshare.rekinyfinansjeryweb.service;

import com.infoshareacademy.domain.DailyExchangeRates;
import com.infoshareacademy.domain.ExchangeRate;
import com.infoshareacademy.services.*;
import org.infoshare.rekinyfinansjeryweb.dto.SearchSettingsDTO;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRatesTable;
import org.infoshare.rekinyfinansjeryweb.repository.ExchangeRatesTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SearchService {

    @Autowired
    FiltrationService collectionFiltrationService;

    @Autowired
    ExchangeRatesTableRepository exchangeRatesTableRepository;

    public List<ExchangeRatesTable> searchInCollection(SearchSettingsDTO settings){
        /*DailyExchangeRatesSearchService dailyExchangeRatesSearchService = NBPApiManager.getInstance().getDailyExchangeSearchRatesService();
        List<DailyExchangeRates> result = new ArrayList<>();
        if(settings.getSearchType() == null || settings.getSearchType().equals("") || settings.getSearchPhrase()==null || settings.getSearchPhrase().equals("")){
            return result;
        }
        switch (settings.getSearchType()){
            case "currency":
                result = searchCurrencies(dailyExchangeRatesSearchService, settings);
                break;
            case "table":
                result = searchTables(dailyExchangeRatesSearchService, settings);
                break;
        }

        List<DailyExchangeRates> dailyExchangeRates = collectionFiltrationService.getFilteredCollectionFromList(result, settings);
        dailyExchangeRates.sort((t1, t2)-> t1.getEffectiveDate().compareTo(t2.getEffectiveDate())*-1);

        return dailyExchangeRates;*/
        return exchangeRatesTableRepository.findAll();
    }

    public DailyExchangeRates getLastExchangeRates(){
        return NBPApiManager.getInstance().getCollectionsOfExchangeRates().stream()
                .sorted((o1, o2) -> o2.getEffectiveDate().compareTo(o1.getEffectiveDate()))
                .findFirst().get();
    }
    public ExchangeRate getCurrencyOfLastExchangeRates(String code) {
        return getLastExchangeRates().getRates().stream()
                .filter(e -> e.getCode().equals(code))
                .findFirst().orElse(null);
    }

    private List<DailyExchangeRates> searchTables(DailyExchangeRatesSearchService dailyExchangeRatesSearchService, SearchSettingsDTO settings){
        return dailyExchangeRatesSearchService.searchWidely(settings.getSearchPhrase());
    }

    private List<DailyExchangeRates> searchCurrencies(DailyExchangeRatesSearchService dailyExchangeRatesSearchService, SearchSettingsDTO settings) {
        return dailyExchangeRatesSearchService.forEachDay(exchangeRates -> exchangeRates.setRates(new ExchangeRatesSearchService(exchangeRates
                .getRates())
                .searchWidely(settings.getSearchPhrase())))
                .stream()
                .filter(table -> table.getRates().size() > 0)
                .collect(Collectors.toList());
    }
}
