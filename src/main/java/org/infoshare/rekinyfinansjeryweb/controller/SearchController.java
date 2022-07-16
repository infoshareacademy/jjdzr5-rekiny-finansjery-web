package org.infoshare.rekinyfinansjeryweb.controller;

import com.infoshareacademy.domain.ExchangeRate;
import com.infoshareacademy.services.NBPApiManager;
import org.infoshare.rekinyfinansjeryweb.controller.controllerComponents.ListToPagesSplitter;
import org.infoshare.rekinyfinansjeryweb.dto.DailyTableFormDTO;
import org.infoshare.rekinyfinansjeryweb.dto.ExchangeRateFormDTO;
import org.infoshare.rekinyfinansjeryweb.entity.user.MyUserPrincipal;
import org.infoshare.rekinyfinansjeryweb.dto.PageDTO;
import org.infoshare.rekinyfinansjeryweb.dto.SearchSettingsDTO;
import org.infoshare.rekinyfinansjeryweb.service.SearchAndFiltrationService;
import org.infoshare.rekinyfinansjeryweb.service.UsedCurrenciesService;
import org.infoshare.rekinyfinansjeryweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/search")
public class SearchController {

    public final static int ELEMENTS_PER_PAGE = 5;

    @Autowired
    SearchAndFiltrationService searchAndFiltrationService;
    @Autowired
    UsedCurrenciesService usedCurrenciesService;
    @Autowired
    UserService userService;


    @GetMapping
    public String displayTables(@ModelAttribute SearchSettingsDTO settings,
                                Pageable pageable,
                                Model model,
                                @AuthenticationPrincipal MyUserPrincipal principal,
                                @RequestParam Optional<Long> page) {

        PageDTO collection = searchAndFiltrationService.searchInCollection(settings, pageable, page);

        ListToPagesSplitter.splitIntoPages(collection, model, pageable);
        model.addAttribute("filtrationSettings", settings);
        model.addAttribute("possibleCurrencies", usedCurrenciesService.getShortNamesOfCurrenciesSplitByCategory(settings.getCurrency()));
        model.addAttribute("newDailyTable", new DailyTableFormDTO());
        model.addAttribute("newCurrency", new ExchangeRateFormDTO());

        if(principal != null) {
            model.addAttribute("listOfPreferences", new ArrayList<>(userService.getSavedFiltrationSettings().keySet()));
        }
        return "search";
    }
}
