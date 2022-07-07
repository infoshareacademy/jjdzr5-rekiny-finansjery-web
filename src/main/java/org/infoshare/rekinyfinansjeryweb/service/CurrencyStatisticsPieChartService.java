package org.infoshare.rekinyfinansjeryweb.service;

import org.infoshare.rekinyfinansjeryweb.dto.CurrencyStatisticsDTO;
import org.infoshare.rekinyfinansjeryweb.dto.CurrencyStatisticsPieChartElementDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CurrencyStatisticsPieChartService {

    CurrencyStatisticsClientService currencyStatisticsClientService;

    public CurrencyStatisticsPieChartService(CurrencyStatisticsClientService currencyStatisticsClientService) {
        this.currencyStatisticsClientService = currencyStatisticsClientService;
    }

    public List<CurrencyStatisticsPieChartElementDTO> createPieChartDataSet() {
        List<CurrencyStatisticsDTO> allStatsList = currencyStatisticsClientService.getRecentResults();
        Map<String, Long> aggregatedCurrencyStats = new HashMap<>();
        allStatsList.forEach(currencyStatistic -> {
            String code = currencyStatistic.getCode();
            aggregatedCurrencyStats.putIfAbsent(code, 0L);
            aggregatedCurrencyStats.put(code, aggregatedCurrencyStats.get(code) + currencyStatistic.getCounter());
        });

        return aggregatedCurrencyStats.entrySet().stream().map((entry) -> new CurrencyStatisticsPieChartElementDTO(entry.getKey(), entry.getValue())).toList();
    }

}
