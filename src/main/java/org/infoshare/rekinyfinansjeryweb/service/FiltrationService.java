package org.infoshare.rekinyfinansjeryweb.service;

import com.infoshareacademy.domain.DailyExchangeRates;
import com.infoshareacademy.services.DailyExchangeRatesFiltrationService;
import com.infoshareacademy.services.ExchangeRatesFiltrationService;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRatesTableExchangeRateCurrency;
import org.infoshare.rekinyfinansjeryweb.dto.FiltrationSettingsDTO;
import org.infoshare.rekinyfinansjeryweb.repository.*;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRatesTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FiltrationService {
    @Autowired
    ExchangeRatesTableRepository exchangeRatesTableRepository;

    @Autowired
    ExchangeRateRepository exchangeRateRepository;


    public List<ExchangeRatesTable> getFilteredCollection(FiltrationSettingsDTO settings) {
        //List<ExchangeRatesTable> tables = exchangeRatesTableRepository.findAll();
        List<ExchangeRatesTableExchangeRateCurrency> tables = exchangeRatesTableRepository.findExchangeRatesTableByFilterSettings(settings);
        /*tables.forEach(table -> {
            System.out.println(table.getNo());
            table.getRates().forEach(rate -> {
                System.out.println(rate.getAskPrice());
                System.out.println(rate.getBidPrice());
                System.out.println(rate.getCurrency().getCode());
            });
            System.out.println("===================================================================");
        });
        */
        /*DailyExchangeRatesFiltrationService dailyExchangeRatesFiltrationService = NBPApiManager.getInstance().getDailyExchangeRatesService();
        return filterCollection(dailyExchangeRatesFiltrationService, settings);*/
        return exchangeRatesTableRepository.findAll();
    }

    public List<DailyExchangeRates> getFilteredCollectionFromList(List<DailyExchangeRates> dailyExchangeRates, FiltrationSettingsDTO settings) {
        DailyExchangeRatesFiltrationService dailyExchangeRatesFiltrationService = new DailyExchangeRatesFiltrationService(dailyExchangeRates);
        return filterCollection(dailyExchangeRatesFiltrationService, settings);
    }

    public List<DailyExchangeRates> filterCollection(DailyExchangeRatesFiltrationService dailyExchangeRatesFiltrationService, FiltrationSettingsDTO settings) {
        filterByEffectiveDateFrom(dailyExchangeRatesFiltrationService, settings);
        filterByEffectiveDateTo(dailyExchangeRatesFiltrationService, settings);
        filterByTradingDateFrom(dailyExchangeRatesFiltrationService, settings);
        filterByTradingDateTo(dailyExchangeRatesFiltrationService, settings);
        filterByAskPriceFrom(dailyExchangeRatesFiltrationService, settings);
        filterByAskPriceTo(dailyExchangeRatesFiltrationService, settings);
        filterByBidPriceFrom(dailyExchangeRatesFiltrationService, settings);
        filterByBidPriceTo(dailyExchangeRatesFiltrationService, settings);
        filterByCode(dailyExchangeRatesFiltrationService, settings);

        List<DailyExchangeRates> dailyExchangeRates = dailyExchangeRatesFiltrationService.getDailyExchangeRates();
        dailyExchangeRates.sort((t1, t2)-> t1.getEffectiveDate().compareTo(t2.getEffectiveDate())*-1);
        return dailyExchangeRates;
    }

    private DailyExchangeRatesFiltrationService filterByEffectiveDateFrom(DailyExchangeRatesFiltrationService dailyExchangeRatesFiltrationService, FiltrationSettingsDTO settings) {
        if (settings.getEffectiveDateMin() == null) {
            return dailyExchangeRatesFiltrationService;
        }
        return dailyExchangeRatesFiltrationService.filterByEffectiveDateFrom(settings.getEffectiveDateMin());
    }

    private DailyExchangeRatesFiltrationService filterByEffectiveDateTo(DailyExchangeRatesFiltrationService dailyExchangeRatesFiltrationService, FiltrationSettingsDTO settings) {
        if (settings.getEffectiveDateMax() == null) {
            return dailyExchangeRatesFiltrationService;
        }
        return dailyExchangeRatesFiltrationService.filterByEffectiveDateTo(settings.getEffectiveDateMax());
    }

    private DailyExchangeRatesFiltrationService filterByTradingDateFrom(DailyExchangeRatesFiltrationService dailyExchangeRatesFiltrationService, FiltrationSettingsDTO settings) {
        if (settings.getTradingDateMin() == null) {
            return dailyExchangeRatesFiltrationService;
        }
        return dailyExchangeRatesFiltrationService.filterByTradingDateFrom(settings.getTradingDateMin());
    }

    private DailyExchangeRatesFiltrationService filterByTradingDateTo(DailyExchangeRatesFiltrationService dailyExchangeRatesFiltrationService, FiltrationSettingsDTO settings) {
        if (settings.getTradingDateMax() == null) {
            return dailyExchangeRatesFiltrationService;
        }
        return dailyExchangeRatesFiltrationService.filterByTradingDateTo(settings.getTradingDateMax());
    }

    private DailyExchangeRatesFiltrationService filterByAskPriceFrom(DailyExchangeRatesFiltrationService dailyExchangeRatesFiltrationService, FiltrationSettingsDTO settings) {
        if (settings.getAskPriceMin() == null) {
            return dailyExchangeRatesFiltrationService;
        }
        return dailyExchangeRatesFiltrationService.forEachDay(dailyExchangeRates -> new ExchangeRatesFiltrationService(dailyExchangeRates.
                getRates()).
                filterBySellPriceFrom(settings.getAskPriceMin()));
    }

    private DailyExchangeRatesFiltrationService filterByAskPriceTo(DailyExchangeRatesFiltrationService dailyExchangeRatesFiltrationService, FiltrationSettingsDTO settings) {
        if (settings.getAskPriceMax() == null) {
            return dailyExchangeRatesFiltrationService;
        }
        return dailyExchangeRatesFiltrationService.forEachDay(dailyExchangeRates -> new ExchangeRatesFiltrationService(dailyExchangeRates.
                getRates()).
                filterBySellPriceTo(settings.getAskPriceMax()));
    }

    private DailyExchangeRatesFiltrationService filterByBidPriceFrom(DailyExchangeRatesFiltrationService dailyExchangeRatesFiltrationService, FiltrationSettingsDTO settings) {
        if (settings.getBidPriceMin() == null) {
            return dailyExchangeRatesFiltrationService;
        }
        return dailyExchangeRatesFiltrationService.forEachDay(dailyExchangeRates -> new ExchangeRatesFiltrationService(dailyExchangeRates.
                getRates()).
                filterByBuyPriceFrom(settings.getBidPriceMin()));
    }

    private DailyExchangeRatesFiltrationService filterByBidPriceTo(DailyExchangeRatesFiltrationService dailyExchangeRatesFiltrationService, FiltrationSettingsDTO settings) {
        if (settings.getBidPriceMax() == null) {
            return dailyExchangeRatesFiltrationService;
        }
        return dailyExchangeRatesFiltrationService.forEachDay(dailyExchangeRates -> new ExchangeRatesFiltrationService(dailyExchangeRates.
                getRates()).
                filterByBuyPriceTo(settings.getBidPriceMax()));
    }

    private DailyExchangeRatesFiltrationService filterByCode(DailyExchangeRatesFiltrationService dailyExchangeRatesFiltrationService, FiltrationSettingsDTO settings) {
        List<String> selectedCurrencies = settings.getCurrency();
        if (settings.getCurrency().isEmpty()) {
            return dailyExchangeRatesFiltrationService;
        }
        return dailyExchangeRatesFiltrationService.forEachDay(dailyExchangeRates ->
                new ExchangeRatesFiltrationService(dailyExchangeRates.getRates())
                        .filterByShortName(selectedCurrencies)
        );
    }
}
