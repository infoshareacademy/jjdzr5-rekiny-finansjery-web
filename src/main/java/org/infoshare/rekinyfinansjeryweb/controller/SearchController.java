package org.infoshare.rekinyfinansjeryweb.controller;

import com.infoshareacademy.domain.DailyExchangeRates;
import com.infoshareacademy.domain.ExchangeRate;
import com.infoshareacademy.services.NBPApiManager;
import org.infoshare.rekinyfinansjeryweb.controller.controllerComponents.ListToPagesSplitter;
import org.infoshare.rekinyfinansjeryweb.formData.DailyTableForm;
import org.infoshare.rekinyfinansjeryweb.data.MyUserPrincipal;
import org.infoshare.rekinyfinansjeryweb.formData.SearchSettings;
import org.infoshare.rekinyfinansjeryweb.service.SearchService;
import org.infoshare.rekinyfinansjeryweb.service.UsedCurrenciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

    public final static int ELEMENTS_PER_PAGE = 5;

    @Autowired
    SearchService searchService;
    @Autowired
    UsedCurrenciesService usedCurrenciesService;

    @GetMapping
    public String displayTables(@ModelAttribute SearchSettings settings,
                                Pageable pageable,
                                Model model,
                                @AuthenticationPrincipal MyUserPrincipal principal) {

        List<DailyExchangeRates> collection = searchService
                .searchInCollection(settings);

        ListToPagesSplitter.splitIntoPages(collection, model, pageable);
        model.addAttribute("filtrationSettings", settings);
        model.addAttribute("possibleCurrencies", usedCurrenciesService.getShortNamesOfCurrencies(NBPApiManager.getInstance(), settings.getCurrency()));
        model.addAttribute("newDailyTable", new DailyTableForm());
        model.addAttribute("newCurrency", new ExchangeRate());

        if(principal != null) {
            model.addAttribute("listOfPreferences", new ArrayList<>(principal.getUser().getSavedFiltrationSettings().keySet()));
        }
        return "search";
    }
}
