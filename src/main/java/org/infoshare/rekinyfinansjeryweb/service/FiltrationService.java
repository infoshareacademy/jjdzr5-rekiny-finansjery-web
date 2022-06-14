package org.infoshare.rekinyfinansjeryweb.service;

import org.infoshare.rekinyfinansjeryweb.dto.DailyTableDTO;
import org.infoshare.rekinyfinansjeryweb.dto.ExchangeRateDTO;
import org.infoshare.rekinyfinansjeryweb.dto.FiltrationSettingsDTO;
import org.infoshare.rekinyfinansjeryweb.dto.PageDTO;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRateCurrency;
import org.infoshare.rekinyfinansjeryweb.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Component
public class FiltrationService {

    @Autowired
    ExchangeRateRepository exchangeRateRepository;

    @Transactional
    public PageDTO getFilteredCollection(FiltrationSettingsDTO settings, Pageable pageable) {

        Optional<Long> totalResultsOfFilter = exchangeRateRepository.countExchangeRatesFromPeriod(settings.getEffectiveDateMin(), settings.getEffectiveDateMax());

        if(totalResultsOfFilter.isEmpty()){
            return new PageDTO(0, 0, new ArrayList<>());
        }

        List<LocalDate> requestedPage =
                exchangeRateRepository.findExchangeRatesFromPeriod(settings.getEffectiveDateMin(), settings.getEffectiveDateMax(), pageable);

        List<ExchangeRateCurrency> exchangeRateCurrencies =
                exchangeRateRepository.findExchangeRateJoinCurrencyByFilterSettings(settings, requestedPage);

        Map<LocalDate, DailyTableDTO> dailyTables = splitIntoDailyTables(exchangeRateCurrencies);

        dailyTables.values().forEach(table -> table.getRates().sort(Comparator.comparing(ExchangeRateDTO::getCode)));

        return new PageDTO((int)Math.ceil(totalResultsOfFilter.get()/pageable.getPageSize()), totalResultsOfFilter.get(),
                dailyTables.values().stream().sorted((t1, t2) -> t1.getDate().compareTo(t2.getDate()) * -1).toList());
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
