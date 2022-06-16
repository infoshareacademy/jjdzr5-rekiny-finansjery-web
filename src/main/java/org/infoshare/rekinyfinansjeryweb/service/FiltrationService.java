package org.infoshare.rekinyfinansjeryweb.service;

import org.infoshare.rekinyfinansjeryweb.dto.DailyTableDTO;
import org.infoshare.rekinyfinansjeryweb.dto.ExchangeRateDTO;
import org.infoshare.rekinyfinansjeryweb.dto.FiltrationSettingsDTO;
import org.infoshare.rekinyfinansjeryweb.dto.PageDTO;
import org.infoshare.rekinyfinansjeryweb.entity.Currency;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRateCurrency;
import org.infoshare.rekinyfinansjeryweb.repository.CurrencyRepository;
import org.infoshare.rekinyfinansjeryweb.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class FiltrationService {

    @Autowired
    ExchangeRateRepository exchangeRateRepository;

    @Autowired
    CurrencyRepository currencyRepository;

    @Transactional
    public PageDTO getFilteredCollection(FiltrationSettingsDTO settings, Pageable pageable) {
        List<Currency> currencies;
        if(settings.getCurrency().size()>0) {
            currencies = currencyRepository.findCurrenciesWithCodes(settings.getCurrency());
        }
        else {
            currencies = List.of();
        }
        List<UUID> currenciesIds = currencies.stream().map(currency -> currency.getId()).collect(Collectors.toList());
        Long totalResultsOfFilter = exchangeRateRepository.countDatesByFilterSettings(settings, currenciesIds);
        List<LocalDate> dates = exchangeRateRepository.findDatesFromPageByFilterSettings(settings, currenciesIds, pageable);
        List<ExchangeRateCurrency> exchangeRateCurrencies =
                exchangeRateRepository.findPageByFilterSettings(settings, currenciesIds, dates);

        Map<LocalDate, DailyTableDTO> dailyTables = splitIntoDailyTables(exchangeRateCurrencies);

        dailyTables.values().forEach(table -> table.getRates().sort(Comparator.comparing(ExchangeRateDTO::getCode)));

        return new PageDTO((int)Math.ceil(totalResultsOfFilter/pageable.getPageSize())-1, totalResultsOfFilter,
                dailyTables.values().stream().sorted((t1, t2) -> t1.getDate().compareTo(t2.getDate()) * -1).toList());

        /*Optional<Long> totalResultsOfFilter = exchangeRateRepository.countExchangeRatesFromPeriod(settings.getDateMin(), settings.getDateMax());

        if(totalResultsOfFilter.isEmpty()){
            return new PageDTO(0, 0, new ArrayList<>());
        }

        List<LocalDate> requestedPage =
                exchangeRateRepository.findExchangeRatesFromPeriod(settings.getDateMin(), settings.getDateMax(), pageable);

        List<ExchangeRateCurrency> exchangeRateCurrencies =
                exchangeRateRepository.findExchangeRateJoinCurrencyByFilterSettings(settings, requestedPage);

        Map<LocalDate, DailyTableDTO> dailyTables = splitIntoDailyTables(exchangeRateCurrencies);

        dailyTables.values().forEach(table -> table.getRates().sort(Comparator.comparing(ExchangeRateDTO::getCode)));

        return new PageDTO((int)Math.ceil(totalResultsOfFilter.get()/pageable.getPageSize()), totalResultsOfFilter.get(),
                dailyTables.values().stream().sorted((t1, t2) -> t1.getDate().compareTo(t2.getDate()) * -1).toList());*/
    }

    private Map<LocalDate, DailyTableDTO> splitIntoDailyTables(List<ExchangeRateCurrency> exchangeRateCurrencies){
        Map<LocalDate, DailyTableDTO> dailyTables = new HashMap<>();
        exchangeRateCurrencies.forEach(table -> {
            dailyTables.putIfAbsent(table.getDate(), new DailyTableDTO(table.getDate(), new ArrayList<>()));
            if (table.getAskPrice() != null && table.getBidPrice() != null) {
                dailyTables.get(table.getDate()).getRates().add(new ExchangeRateDTO(table.getAskPrice(), table.getBidPrice(),
                        table.getCode(), table.getName(), table.getCategory()));
            }
        });
        return dailyTables;
    }
}
