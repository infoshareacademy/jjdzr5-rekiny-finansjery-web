package org.infoshare.rekinyfinansjeryweb.controller;

import com.infoshareacademy.services.NBPApiManager;
import org.infoshare.rekinyfinansjeryweb.controller.controllerComponents.ListToPagesSplitter;
import org.infoshare.rekinyfinansjeryweb.dto.*;
import org.infoshare.rekinyfinansjeryweb.service.SearchAndFiltrationService;
import org.infoshare.rekinyfinansjeryweb.entity.user.MyUserPrincipal;
import org.infoshare.rekinyfinansjeryweb.service.UsedCurrenciesService;
import org.infoshare.rekinyfinansjeryweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/tables")
public class FiltrationController {


    public static final int ELEMENTS_PER_PAGE = 5;

    @Autowired
    SearchAndFiltrationService collectionFiltrationService;

    @Autowired
    UserService userService;

    @Autowired
    UsedCurrenciesService usedCurrenciesService;

    @GetMapping
    public String displayTables(@ModelAttribute SearchSettingsDTO settings,
                                Pageable pageable,
                                HttpServletRequest request,
                                Model model,
                                @AuthenticationPrincipal MyUserPrincipal principal) {
        PageDTO collection = collectionFiltrationService.getFilteredCollection(settings, pageable);



        ListToPagesSplitter.splitIntoPages(collection, model, pageable);
        model.addAttribute("filtrationSettings", settings);
        model.addAttribute("possibleCurrencies", usedCurrenciesService.getShortNamesOfCurrencies(NBPApiManager.getInstance(), settings.getCurrency()));
        model.addAttribute("newDailyTable", new DailyTableFormDTO());
        model.addAttribute("newCurrency", new ExchangeRateFormDTO());

        if(principal != null) {
            model.addAttribute("listOfPreferences", new ArrayList<>(userService.getSavedFiltrationSettings().keySet()));
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
