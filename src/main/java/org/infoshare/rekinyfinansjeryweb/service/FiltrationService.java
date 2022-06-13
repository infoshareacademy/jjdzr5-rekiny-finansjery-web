package org.infoshare.rekinyfinansjeryweb.service;

import com.infoshareacademy.domain.DailyExchangeRates;
import com.infoshareacademy.services.DailyExchangeRatesFiltrationService;
import com.infoshareacademy.services.ExchangeRatesFiltrationService;
import org.infoshare.rekinyfinansjeryweb.dto.DailyTableDTO;
import org.infoshare.rekinyfinansjeryweb.dto.ExchangeRateDTO;
import org.infoshare.rekinyfinansjeryweb.dto.FiltrationSettingsDTO;
import org.infoshare.rekinyfinansjeryweb.dto.PageDTO;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRateCurrency;
import org.infoshare.rekinyfinansjeryweb.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class FiltrationService {

    @Autowired
    ExchangeRateRepository exchangeRateRepository;


    public PageDTO getFilteredCollection(FiltrationSettingsDTO settings, Pageable pageable) {

        Optional<Long> totalResultsOfFitler = exchangeRateRepository.countExchangeRatesFromPeriod(settings.getEffectiveDateMin(), settings.getEffectiveDateMax());

        if(totalResultsOfFitler.isEmpty()){
            return new PageDTO(0, 0, new ArrayList<>());
        }

        List<LocalDate> requestedPage =
                exchangeRateRepository.findExchangeRatesFromPeriod(settings.getEffectiveDateMin(), settings.getEffectiveDateMax(), pageable);

        List<ExchangeRateCurrency> exchangeRateCurrencies =
                exchangeRateRepository.findExchangeRateJoinCurrencyByFilterSettings(settings, requestedPage);

        Map<LocalDate, DailyTableDTO> dailyTables = new HashMap<>();
        exchangeRateCurrencies.forEach(table -> {
            dailyTables.putIfAbsent(table.getDate(), new DailyTableDTO(table.getDate(), new ArrayList<>()));
            if (table.getAskPrice() != null && table.getBidPrice() != null) {
                dailyTables.get(table.getDate()).getRates().add(new ExchangeRateDTO(table.getAskPrice(), table.getBidPrice(),
                        table.getCode(), table.getName()));
            }
        });
        return new PageDTO((int)Math.ceil(totalResultsOfFitler.get()/pageable.getPageSize()), totalResultsOfFitler.get(),
                dailyTables.values().stream().sorted((t1, t2) -> t1.getDate().compareTo(t2.getDate()) * -1).toList());
        /*List<ExchangeRatesTable> exchangeRatesTables = exchangeRatesTableRepository.findExchangeRatesTableByFilterSettings(settings);
        PagedListHolder<ExchangeRatesTable> pagedListHolder = getPage(exchangeRatesTables, pageable);
        List<String> filteredTables = pagedListHolder.getPageList().stream().map(table -> table.getNo()).collect(Collectors.toList());
        List<ExchangeRatesTableExchangeRateCurrency> tables = exchangeRatesTableRepository
                .findExchangeRatesTableJoinExchangeRateJoinCurrencyByFilterSettings(filteredTables, settings, pageable);

        Map<String, DailyTableDTO> dailyTables = new HashMap<>();
        tables.forEach(table -> {
            dailyTables.putIfAbsent(table.getNo(), new DailyTableDTO(table.getNo(), table.getEffectiveDate(),
                    table.getTradingDate(), new ArrayList<>()));
            if (table.getAskPrice() != null && table.getBidPrice() != null) {
                dailyTables.get(table.getNo()).getRates().add(new ExchangeRateDTO(table.getAskPrice(), table.getBidPrice(),
                        table.getCode(), table.getName()));
            }
        });
        int a = pagedListHolder.getPageCount();
        return new PageDTO(pagedListHolder.getPageCount(), exchangeRatesTables.size(),
                dailyTables.values().stream().sorted((t1, t2) -> t1.getEffectiveDate().compareTo(t2.getEffectiveDate()) * -1).toList());

         */
        //return new PageDTO(0, 0, new ArrayList<>());
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
