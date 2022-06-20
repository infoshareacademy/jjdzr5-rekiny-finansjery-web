package org.infoshare.rekinyfinansjeryweb.controller;

import org.infoshare.rekinyfinansjeryweb.repository.entity.ExchangeRatesTable;
import org.infoshare.rekinyfinansjeryweb.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.infoshare.rekinyfinansjeryweb.dto.DailyTableFormDTO;
import org.infoshare.rekinyfinansjeryweb.dto.ExchangeRateFormDTO;
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

    @Autowired
    TableService tableService;

    @GetMapping("/{no}")
    public String singleTableView(@PathVariable("no") String no, HttpServletRequest request, Model model) {

        Optional<ExchangeRatesTable> dailyExchangeRates = tableService.findTable(no.replaceAll("_", "/"));

        if (dailyExchangeRates.isPresent()) {
            model.addAttribute("exchangeRates", dailyExchangeRates.get());
        } else {
            model.addAttribute("exchangeRates", null);
        }
        model.addAttribute("newDailyTable", new DailyTableFormDTO());
        model.addAttribute("newCurrency", new ExchangeRateFormDTO());
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (inputFlashMap != null) {
            for (Map.Entry<String, ?> entry : inputFlashMap.entrySet()) {
                model.addAttribute(entry.getKey(), entry.getValue());
            }
        }
        return "tableView";
    }
}
