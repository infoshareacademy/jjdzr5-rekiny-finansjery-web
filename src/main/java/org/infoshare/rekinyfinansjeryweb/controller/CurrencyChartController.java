package org.infoshare.rekinyfinansjeryweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CurrencyChartController {

    @RequestMapping("/chart")
    public String currencyChart() {
        return "currency_chart";
    }
}
