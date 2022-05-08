package org.infoshare.rekinyfinansjeryweb.controller;

import com.infoshareacademy.domain.DailyExchangeRates;
import com.infoshareacademy.services.DailyExchangeRatesFiltrationService;
import com.infoshareacademy.services.ExchangeRatesFiltrationService;
import com.infoshareacademy.services.NBPApiManager;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String showChart(@PathVariable("code") String code, Model model) {

       List<ChartData> chartData = getChartData(code);
//       chartData.forEach(System.out::println);
        model.addAttribute("chartData", chartData);
        return "chart";
    }

    @GetMapping("/{code}/{year}/{month}")
    public String showChart(@PathVariable("code") String code, @PathVariable("year") int year, @PathVariable("month") int month, Model model) {

        List<ChartData> chartData = getChartData(code, year, month);
        model.addAttribute("chartData", chartData);
        return "chart";
    }

    @GetMapping("/history/{code}")
    public String showHistory(@PathVariable("code") String code, Model model) {

        List<ChartData> chartData = getChartData(code);
        model.addAttribute("chartData", chartData);
        return "history";
    }

    private List<ChartData> createChartDataSet(DailyExchangeRatesFiltrationService ratesCollectionProvider, String code){
        List<DailyExchangeRates> exchangeRateTablesForOneCurrency = ratesCollectionProvider.forEachDay(dailyExchangeRates -> new ExchangeRatesFiltrationService(dailyExchangeRates.
                getRates()).
                filterByShortName(Arrays.asList(code))).getDailyExchangeRates();


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

    public record ChartData(
            LocalDate effectiveDate,
            LocalDate tradingDate,
            Double askPrice,
            Double bidPrice
    ) {
        //empty
    }

}