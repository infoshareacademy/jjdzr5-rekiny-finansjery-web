package org.infoshare.rekinyfinansjeryweb.service;

import com.infoshareacademy.domain.DailyExchangeRates;
import com.infoshareacademy.domain.ExchangeRate;
import com.infoshareacademy.services.*;
import org.infoshare.rekinyfinansjeryweb.controller.FiltrationController;
import org.infoshare.rekinyfinansjeryweb.controller.SearchController;
import org.infoshare.rekinyfinansjeryweb.form.SearchSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SearchService {

    @Autowired
    FiltrationService collectionFiltrationService;

    public List<DailyExchangeRates> searchInCollection(SearchSettings settings){
        DailyExchangeRatesSearchService dailyExchangeRatesSearchService = NBPApiManager.getInstance().getDailyExchangeSearchRatesService();
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

        return dailyExchangeRates;
    }

    private List<DailyExchangeRates> searchTables(DailyExchangeRatesSearchService dailyExchangeRatesSearchService, SearchSettings settings){
        return dailyExchangeRatesSearchService.searchWidely(settings.getSearchPhrase());
    }

    private List<DailyExchangeRates> searchCurrencies(DailyExchangeRatesSearchService dailyExchangeRatesSearchService, SearchSettings settings) {
        return dailyExchangeRatesSearchService.forEachDay(exchangeRates -> exchangeRates.setRates(new ExchangeRatesSearchService(exchangeRates
                .getRates())
                .searchWidely(settings.getSearchPhrase())));
    }
}
