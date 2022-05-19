package org.infoshare.rekinyfinansjeryweb.controller;

import com.infoshareacademy.domain.DailyExchangeRates;
import com.infoshareacademy.domain.ExchangeRate;
import com.infoshareacademy.services.NBPApiManager;
import org.infoshare.rekinyfinansjeryweb.controller.controllerComponents.ListToPagesSplitter;
import org.infoshare.rekinyfinansjeryweb.data.CurrencyData;
import org.infoshare.rekinyfinansjeryweb.form.SearchSettings;
import org.infoshare.rekinyfinansjeryweb.data.DailyTableData;
import org.infoshare.rekinyfinansjeryweb.service.FiltrationService;
import org.infoshare.rekinyfinansjeryweb.service.UsedCurrenciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/tables")
public class FiltrationController {


    public static final int ELEMENTS_PER_PAGE = 5;

    @Autowired
    FiltrationService collectionFiltrationService;

    @Autowired
    UsedCurrenciesService usedCurrenciesService;

    @GetMapping
    public String displayTables(@ModelAttribute SearchSettings settings,
                                Pageable pageable,
                                HttpServletRequest request,
                                Model model) {
        List<DailyExchangeRates> collection = collectionFiltrationService.getFilteredCollection(settings);

        ListToPagesSplitter.splitIntoPages(collection, model, pageable);
        model.addAttribute("filtrationSettings", settings);
        model.addAttribute("possibleCurrencies", usedCurrenciesService.getShortNamesOfCurrencies(NBPApiManager.getInstance(), settings.getCurrency()));
        model.addAttribute("newDailyTable", new DailyTableData());
        model.addAttribute("newCurrency", new CurrencyData());
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (inputFlashMap != null) {
            for (Map.Entry<String, ?> entry : inputFlashMap.entrySet()) {
                model.addAttribute(entry.getKey(), entry.getValue());
            }
        }
        return "filtration";
    }
}
