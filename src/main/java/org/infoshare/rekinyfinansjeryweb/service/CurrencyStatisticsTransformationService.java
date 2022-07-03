package org.infoshare.rekinyfinansjeryweb.service;

import org.infoshare.rekinyfinansjeryweb.dto.CurrencyStatisticsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CurrencyStatisticsTransformationService {

    CurrencyStatisticsClientService currencyStatisticsClientService;

    public CurrencyStatisticsTransformationService(CurrencyStatisticsClientService currencyStatisticsClientService) {
        this.currencyStatisticsClientService = currencyStatisticsClientService;
    }

    public List<CurrencyStatisticsDTO> calculateCurrencyStats() {
        List<CurrencyStatisticsDTO> allStatsList = currencyStatisticsClientService.getRecentResults();
        Map<LocalDate, Long> aggregatedCurrencyStats = new HashMap<>();
        allStatsList.forEach(currencyStatistic -> {
            LocalDate date = currencyStatistic.getDate();
            aggregatedCurrencyStats.putIfAbsent(date, 0L);
            aggregatedCurrencyStats.put(date, aggregatedCurrencyStats.get(date) + currencyStatistic.getCounter());
        });

        return aggregatedCurrencyStats.entrySet().stream().map((entry) -> new CurrencyStatisticsDTO("SUM", entry.getKey(), entry.getValue())).toList();
    }

}
