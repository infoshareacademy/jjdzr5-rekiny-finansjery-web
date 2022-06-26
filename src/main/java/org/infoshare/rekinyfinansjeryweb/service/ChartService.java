package org.infoshare.rekinyfinansjeryweb.service;

import com.infoshareacademy.domain.DailyExchangeRates;
import com.infoshareacademy.services.DailyExchangeRatesFiltrationService;
import com.infoshareacademy.services.ExchangeRatesFiltrationService;
import com.infoshareacademy.services.NBPApiManager;
import org.infoshare.rekinyfinansjeryweb.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;



@Service
public class ChartService {

    @Autowired
    ExchangeRateRepository exchangeRateRepository;

    public List<ChartData> createChartDataSet(DailyExchangeRatesFiltrationService ratesCollectionProvider, String code){
        List<DailyExchangeRates> exchangeRateTablesForOneCurrency = ratesCollectionProvider.forEachDay(dailyExchangeRates -> exchangeRateRepository.findExchangeRatesByCode(code);
//TODO - tu się robi jakiś bajzel

                new ExchangeRatesFiltrationService(dailyExchangeRates
                .getRates())
                .filterByShortName(Arrays.asList(code)))
                .getDailyExchangeRates();


        return exchangeRateTablesForOneCurrency.stream().map(table -> new ChartData(table.getEffectiveDate(),
                table.getTradingDate(),
                table.getRates().get(0).getAsk(),
                table.getRates().get(0).getBid())
        ).collect(Collectors.toList());
    }

   public List<ChartData> getChartData(String code, int year, int month){
        DailyExchangeRatesFiltrationService ratesCollectionProvider = NBPApiManager.getInstance().getDailyExchangeRatesService();
        ratesCollectionProvider
                .filterByTradingDateFrom(LocalDate.of(year, month, 1))
                .filterByTradingDateTo(LocalDate.of(year, month, LocalDate.of(year, month, 1).lengthOfMonth()));
        return createChartDataSet(ratesCollectionProvider, code);
    }

    public List<ChartData> getChartData(String code){
        DailyExchangeRatesFiltrationService ratesCollectionProvider = NBPApiManager.getInstance().getDailyExchangeRatesService();
        return createChartDataSet(ratesCollectionProvider, code);
    }

    public List<ChartsData> getChartsData(List<String> codes){
        return codes.stream()
                .map(code -> new ChartsData(createChartDataSet(NBPApiManager.getInstance().getDailyExchangeRatesService(), code), code))
                .collect(Collectors.toList());
    }

    public record ChartsData(List<ChartData> chartsData, String code) {
        //empty
    }
    public record ChartData(
            LocalDate effectiveDate,
            LocalDate tradingDate,
            Double askPrice,
            Double bidPrice
    ) {
        //empty
    }

}
