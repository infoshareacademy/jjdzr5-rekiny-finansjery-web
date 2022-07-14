package org.infoshare.rekinyfinansjeryweb.service;

import lombok.Data;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRate;
import org.infoshare.rekinyfinansjeryweb.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;


@Service
public class ChartService {

    final static int LIMIT = 30;
    @Autowired
    ExchangeRateRepository exchangeRateRepository;

    public List<ChartData> createChartDataSet(List<ExchangeRate> rates) {
        return rates.stream().map(exchangeRate -> new ChartData(exchangeRate.getDate(), exchangeRate.getAskPrice(), exchangeRate.getBidPrice())).sorted(Comparator.comparing(e -> e.date))
                .toList();

    }

    public List<ChartData> getChartData(String code, int year, int month) {

        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDate lastDayOfMonth = LocalDate.of(year, month, LocalDate.of(year, month, 1).lengthOfMonth());

        return createChartDataSet(exchangeRateRepository.findExchangeRatesByCurrency_CodeAndDateBetweenOrderByDateDesc(code, firstDayOfMonth, lastDayOfMonth));
    }

    public List<ChartData> getChartData(String code) {
        return createChartDataSet(exchangeRateRepository.findExchangeRatesByCurrency_CodeOrderByDateDesc(code, PageRequest.of(0, LIMIT)));
    }

    public List<ChartsData> getChartsData(List<String> codes) {

        List<ChartsData> chartsData = new ArrayList<>();

        for (String code : codes) {
            chartsData.add(new ChartsData(getChartData(code), code));
        }
        return chartsData;
    }

    public List<ChartData> sendExchangeRatesForGivenPage(String code, Pageable pageable){
        return createChartDataSet(exchangeRateRepository.findExchangeRatesByCurrency_CodeOrderByDateDesc(code, pageable));
    }

    @Data
    public class ChartsData {

        List<ChartData> chartsData;
        String code;

        public ChartsData(List<ChartData> chartsData, String code) {
            this.chartsData = chartsData;
            this.code = code;
        }
    }


    public record ChartData(
            LocalDate date,
            Double askPrice,
            Double bidPrice
    ) {
        //empty
    }

}
