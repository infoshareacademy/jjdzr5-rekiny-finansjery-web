package org.infoshare.rekinyfinansjeryweb.controller.controllerComponents;

import com.infoshareacademy.domain.DailyExchangeRates;
import com.infoshareacademy.services.NBPApiManager;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import java.util.List;

public class ListToPagesSplitter<T> {
    public static void splitIntoPages(List collection, Model model, Pageable pageable){
        PagedListHolder<DailyExchangeRates> pageElements = new PagedListHolder<>(collection);
        pageElements.setPageSize(pageable.getPageSize());
        pageElements.setPage(pageable.getPageNumber());

        model.addAttribute("numberOfElements", collection.size());
        model.addAttribute("pageSize", pageable.getPageSize());
        model.addAttribute("pagesAmount", pageElements.getPageCount());
        model.addAttribute("pageActive", pageable.getPageNumber());
        model.addAttribute("pageContent", pageElements.getPageList());
    }
}
