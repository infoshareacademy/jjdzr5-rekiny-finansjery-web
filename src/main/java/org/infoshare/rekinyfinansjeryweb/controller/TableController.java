package org.infoshare.rekinyfinansjeryweb.controller;

import org.infoshare.rekinyfinansjeryweb.dto.DailyTableDTO;
import org.infoshare.rekinyfinansjeryweb.dto.DailyTableFormDTO;
import org.infoshare.rekinyfinansjeryweb.dto.ExchangeRateDTO;
import org.infoshare.rekinyfinansjeryweb.dto.ExchangeRateFormDTO;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRateCurrency;
import org.infoshare.rekinyfinansjeryweb.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/table")
public class TableController {
    @Autowired
    ExchangeRateRepository exchangeRateRepository;

    @GetMapping("/{date}")
    public String singleTableView(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, HttpServletRequest request, Model model) {
        List<ExchangeRateCurrency> response = exchangeRateRepository.findExchangeRatesCurrenciesByDate(date);
        List<ExchangeRateDTO> exchangeRates = response.stream()
                .map(rate -> new ExchangeRateDTO(rate.getAskPrice(), rate.getBidPrice(), rate.getCode(), rate.getName(), rate.getCategory()))
                .collect(Collectors.toList());
        if (exchangeRates.isEmpty()) {
            model.addAttribute("exchangeRates", null);
        } else {
            model.addAttribute("exchangeRates", new DailyTableDTO(date, exchangeRates));
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