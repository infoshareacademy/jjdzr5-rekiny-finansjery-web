package org.infoshare.rekinyfinansjeryweb.service;

import com.infoshareacademy.domain.DailyExchangeRates;
import com.infoshareacademy.services.DailyExchangeRatesFiltrationService;
import com.infoshareacademy.services.ExchangeRatesFiltrationService;
import com.infoshareacademy.services.NBPApiManager;
import org.infoshare.rekinyfinansjeryweb.formData.FiltrationSettings;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FiltrationService {
    public List<DailyExchangeRates> getFilteredCollection(FiltrationSettings settings) {
        DailyExchangeRatesFiltrationService dailyExchangeRatesFiltrationService = NBPApiManager.getInstance().getDailyExchangeRatesService();
        return filterCollection(dailyExchangeRatesFiltrationService, settings);
    }

    public List<DailyExchangeRates> getFilteredCollectionFromList(List<DailyExchangeRates> dailyExchangeRates, FiltrationSettings settings) {
        DailyExchangeRatesFiltrationService dailyExchangeRatesFiltrationService = new DailyExchangeRatesFiltrationService(dailyExchangeRates);
        return filterCollection(dailyExchangeRatesFiltrationService, settings);
    }

    public List<DailyExchangeRates> filterCollection(DailyExchangeRatesFiltrationService dailyExchangeRatesFiltrationService, FiltrationSettings settings) {
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

    private DailyExchangeRatesFiltrationService filterByEffectiveDateFrom(DailyExchangeRatesFiltrationService dailyExchangeRatesFiltrationService, FiltrationSettings settings) {
        if (settings.getEffectiveDateMin() == null) {
            return dailyExchangeRatesFiltrationService;
        }
        return dailyExchangeRatesFiltrationService.filterByEffectiveDateFrom(settings.getEffectiveDateMin());
    }

    private DailyExchangeRatesFiltrationService filterByEffectiveDateTo(DailyExchangeRatesFiltrationService dailyExchangeRatesFiltrationService, FiltrationSettings settings) {
        if (settings.getEffectiveDateMax() == null) {
            return dailyExchangeRatesFiltrationService;
        }
        return dailyExchangeRatesFiltrationService.filterByEffectiveDateTo(settings.getEffectiveDateMax());
    }

    private DailyExchangeRatesFiltrationService filterByTradingDateFrom(DailyExchangeRatesFiltrationService dailyExchangeRatesFiltrationService, FiltrationSettings settings) {
        if (settings.getTradingDateMin() == null) {
            return dailyExchangeRatesFiltrationService;
        }
        return dailyExchangeRatesFiltrationService.filterByTradingDateFrom(settings.getTradingDateMin());
    }

    private DailyExchangeRatesFiltrationService filterByTradingDateTo(DailyExchangeRatesFiltrationService dailyExchangeRatesFiltrationService, FiltrationSettings settings) {
        if (settings.getTradingDateMax() == null) {
            return dailyExchangeRatesFiltrationService;
        }
        return dailyExchangeRatesFiltrationService.filterByTradingDateTo(settings.getTradingDateMax());
    }

    private DailyExchangeRatesFiltrationService filterByAskPriceFrom(DailyExchangeRatesFiltrationService dailyExchangeRatesFiltrationService, FiltrationSettings settings) {
        if (settings.getAskPriceMin() == null) {
            return dailyExchangeRatesFiltrationService;
        }
        return dailyExchangeRatesFiltrationService.forEachDay(dailyExchangeRates -> new ExchangeRatesFiltrationService(dailyExchangeRates.
                getRates()).
                filterBySellPriceFrom(settings.getAskPriceMin()));
    }

    private DailyExchangeRatesFiltrationService filterByAskPriceTo(DailyExchangeRatesFiltrationService dailyExchangeRatesFiltrationService, FiltrationSettings settings) {
        if (settings.getAskPriceMax() == null) {
            return dailyExchangeRatesFiltrationService;
        }
        return dailyExchangeRatesFiltrationService.forEachDay(dailyExchangeRates -> new ExchangeRatesFiltrationService(dailyExchangeRates.
                getRates()).
                filterBySellPriceTo(settings.getAskPriceMax()));
    }

    private DailyExchangeRatesFiltrationService filterByBidPriceFrom(DailyExchangeRatesFiltrationService dailyExchangeRatesFiltrationService, FiltrationSettings settings) {
        if (settings.getBidPriceMin() == null) {
            return dailyExchangeRatesFiltrationService;
        }
        return dailyExchangeRatesFiltrationService.forEachDay(dailyExchangeRates -> new ExchangeRatesFiltrationService(dailyExchangeRates.
                getRates()).
                filterByBuyPriceFrom(settings.getBidPriceMin()));
    }

    private DailyExchangeRatesFiltrationService filterByBidPriceTo(DailyExchangeRatesFiltrationService dailyExchangeRatesFiltrationService, FiltrationSettings settings) {
        if (settings.getBidPriceMax() == null) {
            return dailyExchangeRatesFiltrationService;
        }
        return dailyExchangeRatesFiltrationService.forEachDay(dailyExchangeRates -> new ExchangeRatesFiltrationService(dailyExchangeRates.
                getRates()).
                filterByBuyPriceTo(settings.getBidPriceMax()));
    }

    private DailyExchangeRatesFiltrationService filterByCode(DailyExchangeRatesFiltrationService dailyExchangeRatesFiltrationService, FiltrationSettings settings) {
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
