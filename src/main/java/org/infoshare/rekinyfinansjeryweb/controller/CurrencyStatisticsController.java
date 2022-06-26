package org.infoshare.rekinyfinansjeryweb.controller;


import org.infoshare.rekinyfinansjeryweb.service.CurrencyStatisticsClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/stats")
public class CurrencyStatisticsController {

    @Autowired
    CurrencyStatisticsClientService currencyStatisticsClientService;

    @RequestMapping("/all")
    public String showAllStats(Model model) {
        model.addAttribute("results", currencyStatisticsClientService.getAllResults());
        return "stats";
    }

    @RequestMapping()
    public String showRecentStats(Model model) {
        model.addAttribute("results", currencyStatisticsClientService.getRecentResults());
        return "stats";
    }

    @RequestMapping("currency/{code}")
    public String showCurrencyStatsForCurrency(Model model, @PathVariable String code) {
        model.addAttribute("results", currencyStatisticsClientService.getOneResultByCode(code));
        return "stats";
    }

    @RequestMapping("currency/{month}/{year}")
    public String showCurrencyStatsforSelectedMonth(Model model, @PathVariable int month, @PathVariable int year) {
        model.addAttribute("results", currencyStatisticsClientService.getOneResultByMonthAndYear(month, year));
        return "stats";
    }

    @RequestMapping("currency/{month}/{year}/{code}")
    public String showSingleCurrencyStatsFromSelectedMonthForCurrency(Model model, @PathVariable int month, @PathVariable int year, @PathVariable String code) {
        model.addAttribute("results", currencyStatisticsClientService.getOneResultByCodeAndMonthAndYear(month, year, code));
        return "stats";
    }

    @RequestMapping("/increase/{code}")
    public String increaseCountForSelectedCurrency(@PathVariable String code) {
        currencyStatisticsClientService.increaseCount(List.of(code));
        return "stats";
    }
}