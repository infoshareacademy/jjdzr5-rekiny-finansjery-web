package org.infoshare.rekinyfinansjeryweb.controller;

import com.infoshareacademy.domain.DailyExchangeRates;
import com.infoshareacademy.domain.ExchangeRate;
import com.infoshareacademy.services.NBPApiManager;
import org.infoshare.rekinyfinansjeryweb.formData.TableSettings;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/table")
public class TableController {
    @GetMapping("/{no}")
    public String singleTableView(@PathVariable("no") String no, Model model){
        Optional<DailyExchangeRates> dailyExchangeRates = NBPApiManager.getInstance().findDailyTable(no.replaceAll("_", "/"));
        if(dailyEgitxchangeRates.isPresent()){
            model.addAttribute("exchangeRates", dailyExchangeRates.get());
        }
        else{
            model.addAttribute("exchangeRates", null);
        }
        model.addAttribute("newDailyTable", new TableSettings());
        model.addAttribute("newCurrency", new ExchangeRate());
//        model.addAttribute("successMessage", "Success");
//        model.addAttribute("errorMessage", "Error");
        return "tableView";
    }
}
