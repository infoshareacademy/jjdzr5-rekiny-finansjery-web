package org.infoshare.rekinyfinansjeryweb.controller;


import org.infoshare.rekinyfinansjeryweb.service.CurrencyStatisticsClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/stats")
public class CurrencyStatisticsController {

    @Autowired
    CurrencyStatisticsClientService currencyStatisticsClientService;

    @RequestMapping()
    public String showStats(Model model){
        currencyStatisticsClientService.increaseCount(List.of("USD"));
        model.addAttribute("results", currencyStatisticsClientService.getResults());
        return "stats";
    }

}
