package org.infoshare.rekinyfinansjeryweb.controller;

import com.infoshareacademy.domain.DailyExchangeRates;
import com.infoshareacademy.services.NBPApiManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class TestController {

    NBPApiManager nbpApiManager = NBPApiManager.getInstance();
    List<DailyExchangeRates> collectionsOfDailyExchangeRates = nbpApiManager.getCollectionsOfExchangeRates();


    @RequestMapping("/test")
    public String test(Model model) {
        model.addAttribute("collectionsOfExchangeRates", collectionsOfDailyExchangeRates);
        return "test";
    }
}
