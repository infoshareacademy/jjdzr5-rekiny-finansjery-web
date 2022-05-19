package org.infoshare.rekinyfinansjeryweb.controller;

import org.infoshare.rekinyfinansjeryweb.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    ChartService chartService;

    String[] currencyCodes = {"GBP", "USD", "EUR", "CHF", "KRU"};

    @GetMapping
    public String showChart(Model model) {

        List<ChartService.ChartData> chartData = chartService.getChartData(currencyCodes[0]);
        model.addAttribute("chartData", chartData);
        return "index";
    }
}
