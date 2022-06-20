package org.infoshare.rekinyfinansjeryweb.service;

import com.infoshareacademy.domain.ExchangeRate;
import org.infoshare.rekinyfinansjeryweb.dto.*;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRateCurrency;
import org.infoshare.rekinyfinansjeryweb.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Component
public class SearchAndFiltrationService {

    @Autowired
    ExchangeRateRepository exchangeRateRepository;

    @Transactional
    public PageDTO getFilteredCollection(FiltrationSettingsDTO settings, Pageable pageable) {
        Long totalResultsOfFilter = exchangeRateRepository.countDatesByFilterSettings(settings);
        List<LocalDate> dates = exchangeRateRepository.findDatesFromPageByFilterSettings(settings, pageable);
        List<ExchangeRateCurrency> exchangeRateCurrencies =
                exchangeRateRepository.findPageBySearchSettings(settings, dates);

        return convertResultsIntoPageDTO(totalResultsOfFilter, exchangeRateCurrencies, pageable);
    }

    public PageDTO searchInCollection(SearchSettingsDTO settings, Pageable pageable){
        Long totalResultsOfFilter = exchangeRateRepository.countDatesBySearchSettings(settings);
        List<LocalDate> dates = exchangeRateRepository.findDatesFromPageBySearchSettings(settings, pageable);
        List<ExchangeRateCurrency> exchangeRateCurrencies =
                exchangeRateRepository.findPageBySearchSettings(settings, dates);

        return convertResultsIntoPageDTO(totalResultsOfFilter, exchangeRateCurrencies, pageable);
    }

    public ExchangeRate getCurrencyOfLastExchangeRates(String currency){
        return new ExchangeRate("MOCK", "MOCK", 2, 2);
    }

    public List<ExchangeRate> getLastExchangeRates(){
        return List.of(new ExchangeRate("MOCK", "MOCK", 2, 2));
    }

    private PageDTO convertResultsIntoPageDTO(Long totalResultsOfFilter, List<ExchangeRateCurrency> exchangeRateCurrencies, Pageable pageable){
        Map<LocalDate, DailyTableDTO> dailyTables = splitIntoDailyTables(exchangeRateCurrencies);

        dailyTables.values().forEach(table -> table.getRates().sort(Comparator.comparing(ExchangeRateDTO::getCode)));

        return new PageDTO((int)Math.ceil((double)totalResultsOfFilter/pageable.getPageSize())-1, totalResultsOfFilter,
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
