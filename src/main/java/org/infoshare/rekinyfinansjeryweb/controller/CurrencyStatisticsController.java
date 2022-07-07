package org.infoshare.rekinyfinansjeryweb.controller;


import org.infoshare.rekinyfinansjeryweb.dto.SearchSettingsDTO;
import org.infoshare.rekinyfinansjeryweb.service.CurrencyStatisticsClientService;
import org.infoshare.rekinyfinansjeryweb.service.CurrencyStatisticsPieChartService;
import org.infoshare.rekinyfinansjeryweb.service.UsedCurrenciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Method;
import java.util.List;

@Controller
@RequestMapping("/stats")
public class CurrencyStatisticsController {

    @Autowired
    CurrencyStatisticsClientService currencyStatisticsClientService;

    @Autowired
    CurrencyStatisticsPieChartService currencyStatisticsPieChartService;

    @Autowired
    UsedCurrenciesService usedCurrenciesService;

    SearchSettingsDTO settings;

    String[] currencyCodes = new String[] {"NOK", "GBP", "USD"};

    @RequestMapping("/all")
    public String showAllStats(Model model) {
        model.addAttribute("results", currencyStatisticsClientService.getAllResults());
        return "chart_stats";
    }

    @RequestMapping()
    public String showRecentStats(Model model) {
        model.addAttribute("results", currencyStatisticsPieChartService.createPieChartDataSet());
        model.addAttribute("codes", currencyCodes);
        return "pie_stats";
    }

    @RequestMapping("currency/{code}")
    public String showCurrencyStatsForCurrencyUsingPath(Model model, @PathVariable String code) {
        model.addAttribute("results", currencyStatisticsClientService.getOneResultByCode(code));
        model.addAttribute("codes", currencyCodes);
        return "chart_stats";
    }

    @RequestMapping("currency/")
    public String showCurrencyStatsForCurrencyUsingParam(Model model, @RequestParam String code) {
        model.addAttribute("results", currencyStatisticsClientService.getOneResultByCode(code));
        model.addAttribute("codes", currencyCodes);
//        model.addAttribute("possibleCurrencies", usedCurrenciesService.getShortNamesOfCurrencies(NBPApiManager.getInstance(), settings.getCurrency()));
        return "chart_stats";
    }

    @RequestMapping("currency/{month}/{year}")
    public String showCurrencyStatsforSelectedMonth(Model model, @PathVariable int month, @PathVariable int year) {
        model.addAttribute("results", currencyStatisticsClientService.getOneResultByMonthAndYear(month, year));
        return "chart_stats";
    }

    @RequestMapping("currency/{month}/{year}/{code}")
    public String showSingleCurrencyStatsFromSelectedMonthForCurrency(Model model, @PathVariable int month, @PathVariable int year, @PathVariable String code) {
        model.addAttribute("results", currencyStatisticsClientService.getOneResultByCodeAndMonthAndYear(month, year, code));
        return "chart_stats";
    }

    @RequestMapping("/increase/{code}")
    public String increaseCountForSelectedCurrency(@PathVariable String code, Model model) {
        model.addAttribute("results", currencyStatisticsClientService.increaseCount(List.of(code)));
        return "chart_stats";
    }
}