package org.infoshare.rekinyfinansjeryweb.controller;

import com.infoshareacademy.domain.DailyExchangeRates;
import com.infoshareacademy.domain.ExchangeRate;
import com.infoshareacademy.services.NBPApiManager;
import org.infoshare.rekinyfinansjeryweb.formData.DailyTableData;
import org.infoshare.rekinyfinansjeryweb.formData.CurrencyData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/table")
public class TableController {
    @GetMapping("/{no}")
    public String singleTableView(@PathVariable("no") String no, HttpServletRequest request, Model model) {
        Optional<DailyExchangeRates> dailyExchangeRates = NBPApiManager.getInstance().findDailyTable(no.replaceAll("_", "/"));
        if (dailyExchangeRates.isPresent()) {
            model.addAttribute("exchangeRates", dailyExchangeRates.get());
        } else {
            model.addAttribute("exchangeRates", null);
        }
        model.addAttribute("newDailyTable", new DailyTableData());
        model.addAttribute("newCurrency", new CurrencyData());
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (inputFlashMap != null) {
            for (Map.Entry<String, ?> entry : inputFlashMap.entrySet()) {
                model.addAttribute(entry.getKey(), entry.getValue());
            }
        }
        return "tableView";
    }
}
