package org.infoshare.rekinyfinansjeryweb.controller;

import com.infoshareacademy.domain.DailyExchangeRates;
import com.infoshareacademy.services.NBPApiManager;
import org.infoshare.rekinyfinansjeryweb.controller.controllerComponents.ListToPagesSplitter;
import org.infoshare.rekinyfinansjeryweb.form.SearchSettings;
import org.infoshare.rekinyfinansjeryweb.form.TableSettings;
import org.infoshare.rekinyfinansjeryweb.service.CollectionService;
import org.infoshare.rekinyfinansjeryweb.service.FiltrationService;
import org.infoshare.rekinyfinansjeryweb.service.UsedCurrenciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequestMapping("/tables")
public class FiltrationController {


    public static final int ELEMENTS_PER_PAGE = 5;

    @Autowired
    FiltrationService collectionFiltrationService;

    @Autowired
    UsedCurrenciesService usedCurrenciesService;

    @Autowired
    CollectionService collectionService;

    @GetMapping
    public String displayTables(@ModelAttribute SearchSettings settings,
                                Pageable pageable,
                                Model model) {
        List<DailyExchangeRates> collection = collectionFiltrationService.getFilteredCollection(settings);

        ListToPagesSplitter.splitIntoPages(collection, model, pageable);
        model.addAttribute("filtrationSettings", settings);
        model.addAttribute("possibleCurrencies", usedCurrenciesService.getShortNamesOfCurrencies(NBPApiManager.getInstance(), settings.getCurrency()));
        model.addAttribute("newDailyTable", new TableSettings());
        return "filtration";
    }

    @PostMapping
    public RedirectView postNewTable(@ModelAttribute TableSettings settings) {
        collectionService.addTable(settings);
        return new RedirectView("/tables");
    }

    @PostMapping("/delete-table/{no}")
    public RedirectView deleteTable(@PathVariable("no") String no) {
        collectionService.deleteTable(no.replace('_', '/'));
        return new RedirectView("/tables");
    }
}
