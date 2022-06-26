package org.infoshare.rekinyfinansjeryweb.service;

import com.infoshareacademy.domain.DailyExchangeRates;
import com.infoshareacademy.services.DailyExchangeRatesFiltrationService;
import com.infoshareacademy.services.ExchangeRatesFiltrationService;
import com.infoshareacademy.services.NBPApiManager;
import lombok.Data;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRate;
import org.infoshare.rekinyfinansjeryweb.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ChartService {

    final static int LIMIT = 30;
    @Autowired
    ExchangeRateRepository exchangeRateRepository;

    public List<ChartData> createChartDataSet(List<ExchangeRate> rates) {
        return rates.stream().map(exchangeRate -> new ChartData(exchangeRate.getDate(), exchangeRate.getAskPrice(), exchangeRate.getBidPrice()))
                .toList();

    }

    public List<ChartData> getChartData(String code, int year, int month) {

        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDate lastDayOfMonth = LocalDate.of(year, month, LocalDate.of(year, month, 1).lengthOfMonth());

        return createChartDataSet(exchangeRateRepository.findExchangeRateByCodeAndDateBetweenOrderByDateDesc(code, firstDayOfMonth, lastDayOfMonth));
    }

    public List<ChartData> getChartData(String code) {
        return createChartDataSet(exchangeRateRepository.findExchangeRatesByCodeOrderByDateDesc(code, LIMIT));
    }

    public List<ChartsData> getChartsData(List<String> codes) {

        Map<String, ChartsData> chartsData = new HashMap<>();
        exchangeRateRepository.findMultipleExchangeRatesByCodes(codes, PageRequest.of(0, LIMIT)).forEach(exchangeRate -> {
            String key = exchangeRate.getCurrency().getCode();
            chartsData.putIfAbsent(key, new ChartsData(key));
            chartsData.get(key).addChartData(new ChartData(exchangeRate.getDate(), exchangeRate.getAskPrice(), exchangeRate.getBidPrice()));
        });
        return chartsData.values().stream().toList();
    }

    @Data
    public class ChartsData {

        List<ChartData> chartsData;
        String code;

        public ChartsData(String code) {
            this.code = code;
            chartsData = new ArrayList<>();
        }

        public void addChartData(ChartData chartData) {
            chartsData.add(chartData);
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
