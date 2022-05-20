package org.infoshare.rekinyfinansjeryweb.controller;

import org.infoshare.rekinyfinansjeryweb.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    ChartService chartService;

    List<String> currencies = new ArrayList<>(Arrays.asList("GBP", "USD", "EUR", "CHF"));

    @GetMapping
    public String showChart(Model model) {

        model.addAttribute("currencies", currencies);
        model.addAttribute("code", "EUR");

        model.addAttribute("chartsData", chartService.getChartsData(currencies));
        return "index";
    }
}
