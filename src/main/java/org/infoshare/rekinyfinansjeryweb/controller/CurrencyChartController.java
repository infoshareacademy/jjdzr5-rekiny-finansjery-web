package org.infoshare.rekinyfinansjeryweb.controller;

import com.infoshareacademy.domain.DailyExchangeRates;
import com.infoshareacademy.services.DailyExchangeRatesFiltrationService;
import com.infoshareacademy.services.ExchangeRatesFiltrationService;
import com.infoshareacademy.services.NBPApiManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/currency")
@RequiredArgsConstructor
public class CurrencyChartController {

    @GetMapping("/{code}")
    public String showChart(@PathVariable("code") String code) {

       List<ChartData> chartData = getChartData(code);
       chartData.forEach(System.out::println);

        return "chart";
    }

    @GetMapping("/history/{code}")
    public String showHistory(@PathVariable("code") String code) {

        List<ChartData> chartData = getChartData(code);
        chartData.forEach(System.out::println);

        return "history";}

    public List<ChartData> getChartData(String code){
        DailyExchangeRatesFiltrationService ratesCollectionProvider = NBPApiManager.getInstance().getDailyExchangeRatesService();

        List<DailyExchangeRates> exchangeRateTablesForOneCurrency = ratesCollectionProvider.forEachDay(dailyExchangeRates -> new ExchangeRatesFiltrationService(dailyExchangeRates.
                getRates()).
                filterByShortName(Arrays.asList(code))).getDailyExchangeRates();


        return exchangeRateTablesForOneCurrency.stream().map(table -> new ChartData(table.getEffectiveDate(),
                table.getTradingDate(),
                table.getRates().get(0).getAsk(),
                table.getRates().get(0).getBid())
        ).collect(Collectors.toList());
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