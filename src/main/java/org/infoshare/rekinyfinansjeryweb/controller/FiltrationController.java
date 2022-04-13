package org.infoshare.rekinyfinansjeryweb.controller;

import com.infoshareacademy.domain.DailyExchangeRates;
import com.infoshareacademy.services.ExchangeRatesFiltrationService;
import com.infoshareacademy.services.NBPApiManager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.infoshare.rekinyfinansjeryweb.service.CollectionFiltrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tables")
public class FiltrationController {
    public final static int ELEMENTS_PER_PAGE = 25;

    @Autowired
    CollectionFiltrationService collectionFiltrationService;

    @GetMapping
    public String displayTables(@ModelAttribute FiltrationSettings settings,
                                @Nullable @RequestParam(defaultValue = "1") Integer page,
                                HttpServletRequest request,
                                Model model){
        System.out.println(page);

        List<DailyExchangeRates> collection = collectionFiltrationService.getFilteredCollection(settings);

        List<PossibleCurrency> possibleCurrencies = getShortNamesOfCurrencies(NBPApiManager.getInstance(), settings.getCurrency());

        int numberOfPages = getNumberOfPages(collection);
        if(page > numberOfPages){
            page = 1;
        }

        model.addAttribute("numberOfElements", collection.size());
        model.addAttribute("path", getPathWithoutPageNumber(request));
        model.addAttribute("filtrationSettings", settings);
        model.addAttribute("buttons", getPageButtons(request, page, getNumberOfPages(collection)));
        model.addAttribute("collectionsOfExchangeRates", getPage(page, collection));
        model.addAttribute("possibleCurrencies", possibleCurrencies);
        return "filtration";
    }

    public int getNumberOfPages(List<DailyExchangeRates> collection){
        return (int)Math.ceil((double)collection.size() / ELEMENTS_PER_PAGE);
    }

    public List<DailyExchangeRates> getPage(int page, List<DailyExchangeRates> collection){
        if((page-1)*ELEMENTS_PER_PAGE > collection.size()-1){
            return new ArrayList<>();
        }
        if(page*ELEMENTS_PER_PAGE > collection.size()){
            return collection.subList((page-1)*ELEMENTS_PER_PAGE, collection.size());
        }
        return collection.subList((page-1)*ELEMENTS_PER_PAGE, page*ELEMENTS_PER_PAGE);
    }

    public List<PossibleCurrency> getShortNamesOfCurrencies(NBPApiManager nbpApiManager, List<String> selectedCurrencies){
        return nbpApiManager.getCollectionsOfExchangeRates()
                .stream()
                .findFirst()
                .map(exchangeRates -> exchangeRates
                .getRates()
                .stream()
                .map(rate -> new PossibleCurrency(rate.getCode(), selectedCurrencies.contains(rate.getCode())))
                .collect(Collectors.toList()))
                .orElseGet(ArrayList::new);
    }

    public String getPathWithoutPageNumber(HttpServletRequest request){
        if(request.getQueryString()==null){
            return request.getRequestURL().toString() + "?page=";
        }
        int startOfPageVariable = request.getQueryString().indexOf("page=");
        if(startOfPageVariable == 0){
            return request.getRequestURL().toString() + "?" + request.getQueryString().substring(0, request.getQueryString().indexOf("page=")) + "&page=";
        }
        if(startOfPageVariable > 0){
            return request.getRequestURL().toString() + "?" + request.getQueryString().substring(0, request.getQueryString().indexOf("page=")-1) + "&page=";
        }
        return request.getRequestURL().toString() + "?" + request.getQueryString() + "&page=";
    }

    public List<PageButton> getPageButtons(HttpServletRequest request, int page, int numberOfPages){
        String rawUrl = getPathWithoutPageNumber(request);
        List<PageButton> buttons = new ArrayList<>();
        int windowSize = 4;

        int rangeStart = page - windowSize;
        int rangeEnd = page + windowSize;
        if(rangeStart < 1){
            rangeStart = 1;
            rangeEnd += windowSize - page + 1;
        }
        if(rangeEnd > numberOfPages){
            rangeEnd = numberOfPages;
            rangeStart -= windowSize - (numberOfPages-page);
        }
        if(rangeStart < 1) {
            rangeStart = 1;
            rangeEnd = numberOfPages;
        }
        for(int i=rangeStart; i<=rangeEnd; i++){
            if(i==page){
                buttons.add(new PageButton(String.valueOf(i), rawUrl+i, true));
            }
            else{
                buttons.add(new PageButton(String.valueOf(i), rawUrl+i, false));
            }
        }

        if(buttons.size()>=1 && !buttons.get(0).getText().equals("1")){
            buttons.add(0, new PageButton("<<", rawUrl+1, false));
        }
        if(buttons.size()>=1 && !buttons.get(buttons.size()-1).getText().equals(String.valueOf(numberOfPages))){
            buttons.add(new PageButton(">>", rawUrl+numberOfPages, false));
        }
        return buttons;
    }

    @Data
    public class FiltrationSettings{
        public FiltrationSettings(){
            currency = new ArrayList<>();
        }
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate effectiveDateMin;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate effectiveDateMax;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate tradingDateMin;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate tradingDateMax;
        private Double bidPriceMin;
        private Double bidPriceMax;
        private Double askPriceMin;
        private Double askPriceMax;
        private List<String> currency;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class PageButton{
        private String text;
        private String url;
        private boolean present;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class PossibleCurrency{
        private String code;
        private boolean checked;
    }
}
