package org.infoshare.rekinyfinansjeryweb.service;

import lombok.extern.slf4j.Slf4j;
import org.infoshare.rekinyfinansjeryweb.dto.CurrencyStatisticsDTO;
import org.infoshare.rekinyfinansjeryweb.dto.CurrencyStatisticsPieChartElementDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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

        try {
            return aggregatedCurrencyStats.entrySet().stream().map((entry) -> new CurrencyStatisticsPieChartElementDTO(entry.getKey(), entry.getValue())).toList();
        } catch (Exception e) {
            log.info("Problem with contacting the Stat service, returning empty List instead");
            return List.of();
        }
    }


}
