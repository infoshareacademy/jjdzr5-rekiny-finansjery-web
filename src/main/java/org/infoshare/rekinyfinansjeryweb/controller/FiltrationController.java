package org.infoshare.rekinyfinansjeryweb.controller;

import com.infoshareacademy.services.NBPApiManager;
import org.infoshare.rekinyfinansjeryweb.controller.controllerComponents.ListToPagesSplitter;
import org.infoshare.rekinyfinansjeryweb.formData.ExchangeRateForm;
import org.infoshare.rekinyfinansjeryweb.formData.DailyTableForm;
import org.infoshare.rekinyfinansjeryweb.data.MyUserPrincipal;
import org.infoshare.rekinyfinansjeryweb.formData.SearchSettings;
import org.infoshare.rekinyfinansjeryweb.repository.entity.ExchangeRatesTable;
import org.infoshare.rekinyfinansjeryweb.service.FiltrationService;
import org.infoshare.rekinyfinansjeryweb.service.UsedCurrenciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.ArrayList;
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
                                Model model,
                                @AuthenticationPrincipal MyUserPrincipal principal) {
        List<ExchangeRatesTable> collection = collectionFiltrationService.getFilteredCollection(settings);

        ListToPagesSplitter.splitIntoPages(collection, model, pageable);
        model.addAttribute("filtrationSettings", settings);
        model.addAttribute("possibleCurrencies", usedCurrenciesService.getShortNamesOfCurrencies(NBPApiManager.getInstance(), settings.getCurrency()));
        model.addAttribute("newDailyTable", new DailyTableForm());
        model.addAttribute("newCurrency", new ExchangeRateForm());

        if(principal != null) {
            model.addAttribute("listOfPreferences", new ArrayList<>(principal.getUser().getSavedFiltrationSettings().keySet()));
        }
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (inputFlashMap != null) {
            for (Map.Entry<String, ?> entry : inputFlashMap.entrySet()) {
                model.addAttribute(entry.getKey(), entry.getValue());
            }
        }
        return "filtration";
    }
}
